# **关于MinecraftForge声音播放和加载机制的深入研究**

  yahoo，这里是狼的说，一只普通的modder~
之前在写mod的过程中，还没有深究过Minecraft的声音系统，而是按照标准的创建json索引声音信息、再对World实例调用world.playSound(...)的一般方法来实现的。
这种方法虽然很简便，但是在功能上也很简陋。比如说咱想实现一个**循环播放多少时间然后自动停止**的声音，然而根本没有看到这样的接口……
出于种种原因，今天咱打算**深入分析一遍MC的声音播放机制**，顺便锻炼代码阅读能力（雾）。
也希望自己的这次研究能对各位modder有所助益，少走手动反混淆和乱造轮子的弯路～～
好啦，**我们开始吧**！



P.S.本篇文章属于略微高级的主题。请确认你有基本的OO编程、Mod编程的知识。
P.S.S.本篇文章比起教程来说还是更加具有参考或者杂文的风格。如果你需要学习声音的简单播放，看完【**World接口分析**】部分就好了。不过如果能看完整个部分的话，绝对会对mc的声音加载机制有深刻的理解的，当做一次小小的精神旅行又有什么不好呢？^^

注：本贴中的一切代码研究都基于**MinecraftForge 10.12.2.1121**开发环境，不对所有的版本具有普遍性。



## **1、普通的声音加载和播放方法回顾**

在1.7后，Minecraft的声音加载机制发生了比较大的变化。
首先，你需要在你的mod包中的assets/xxx/中添加一个sounds.json，来索引（预加载）声音文件，它看起来大概是这样的：  

```json

{
"deny": {"category": "master", "sounds": [{"name": "deny", "stream": false}]},
"elec.weak": {"category": "master", "sounds": [{"name": "elec/arc_weak", "stream": false}]},
"elec.strong": {"category": "master", "sounds": [{"name": "elec/arc_strong", "stream": false}]},
"elec.mineview": {"category": "master", "sounds": [{"name": "elec/mineview", "stream": false}]},
...
}
```

之后，如果需要播放声音的话，只需要在有任一一个Entity或者World实例的情况下，调用它们的

```java
playSoundXXX(String sndName, ...)
```

  方法即可。
注册的每一个声音的名称是*"<namespace>:<sound_name>"*，其中namespace是你mod的资源命名空间的名字（在上面的实例中是xxx），sound_name是json里面每一个元素的键名("deny", "elec.weak", ...)。



**Entity提供的接口：**
**entity.playSound(String name, float volume, pitch)**
在实体的位置播放声音，后两个参数分别是音量和音高。
展开它的源代码，可以发现它调用的其实是World.playSoundAtEntity(...)。
注意：其实EntityPlayer进行了一个重载，它调用的是World.playSoundToNearExcept(...)。所以实际上，如果你在服务端调用这个函数的话，在自己的客户端是听不到声音的。（谁知道为什么这么设计呢╮(╯▽╰)╭）



**World提供的接口：**
***World.playSoundAtEntity(Entity entity, String name, float volume, float pitch)***
在实体的位置播放一个声音。

***World.playSound(double x, double y, double z, String name, float volume, float pitch, boolean wtf)***
在指定的位置播放一个声音，但是多出了一个**奇怪的布尔参数**，并且forge没有给出注释。我们等会会详细考察这个参数的用途。
***World.playSoundEffect(double x, double y, double z, String name, float volume, float pitch)***
在指定的位置播放一个……声音效果？但是声音效果又是什么？它和声音又有什么区别呢？

**World.playSoundToNearExcept(EntityPlayer player, String name, double x, double y, double z, float volume, float pitch)**
玩家实例特化的播放声音方法。好像不会对玩家本身所在的客户端产生声音播放的样子。

可以看到，从这里开始就已经产生**歧义**了。如果我要播放一个声音，到底是该用*playSound*还是*playSoundEffect*呢？如果两个都可以用，它们之间有什么区别呢？*playSound*函数中的那个奇怪的布尔参数是干什么用的呢？*playSoundAtEntity*和*playSoundEffect*的效果会不会有区别呢？莫慌，接下来让我们**从最顶层的代码开始，一步一步深入**，了解MC的声音播放机制是怎么实现的。



**1、从零开始：World接口分析**
那么，我们还是直接从和客户端程序员进行交互的**World实例提供的方法**开始研究。
首先，你应该有关于**C/S模型**的基本知识。在Minecraft游戏的时候，不论是单人还是多人，总有两个World实例在同时运行。一个是客户端的World，负责还原世界的视觉效果等；一个是服务端的World，负责处理世界的”真正“计算。**声音最终肯定是在客户端播放**的，因此我们可以做出如下的推测：服务端的声音播放不会真正的干事，它只会**通知**所有客户端的世界去进行播放声音的工作；而客户端的世界**真正执行**播放声音的工作。有了这点基本假设，我们就可以开始阅读代码了。
客户端的世界类是WorldClient，服务端的是WorldServer，它们的基类是World。进一步观察可以发现，WorldServer**没有覆盖World的任何方法**。因此，我们只需要注意两个地方：World和WorldClient。



**·World.playSound方法**
首先看World类，惊了：它的playSound方法居然是空的！这也就直接意味着，**在服务端调用playSound方法是什么都不会发生的**。
然而，在WorldClient端有它的一个重载。这是经过手动反混淆之后的客户端声音播放代码：  

```java
public void playSound(double x, double y, double z, String name, float volume, float pitch, boolean delay) {
    double distSq = this.mc.renderViewEntity.getDistanceSq(x, y, z); //计算当前玩家到播放声音的位置的距离
    //创建声音播放实例信息
    PositionedSoundRecord psr = new PositionedSoundRecord(new ResourceLocation(name), volume, pitch, (float)x, (float)y, (float)z); 
    
    if (delay && distSq > 100.0D) { //如果声音播放有延迟，并且声音距离和玩家距离大于10
        double d = Math.sqrt(d3) / 40.0D;
        this.mc.getSoundHandler().playDelayedSound(psr, (int)(d * 20.0D)); //回调soundHandler，在(距离*0.5）tick后播放声音
    } else {
        this.mc.getSoundHandler().playSound(psr); //回调soundHandler，播放声音
    }
}
```

  现在我们知道那个奇怪的布尔参数是啥了：**是否延迟播放声音**。
可以看到，这段代码已经开始调用到一个名叫*SoundHandler*的类了。所有关于声音的进一步处理都会在*SoundHandler*类里进行。
**总结：World.playSound在server端没有意义，在client端则会进一步执行播放动作。**



**·World.playSoundEffect方法**
这个方法在*WorldClient*也没有被覆盖，所以它在客户端和服务端的行为是一致的。……真的是这样么？
我们看看*playSoundEffect*的代码吧：  

```java
public void playSoundEffect(double x, double y, double z, String name, float volume, float pitch) {
    for (int i = 0; i < this.worldAccesses.size(); ++i) { //遍历所有worldAccess实例
        ((IWorldAccess)this.worldAccesses.get(i)).playSound(name, x, y, z, volume, pitch);
    }
}
```

可以看到，这段代码自己并没有做什么有意思的事。相对的，它把锅**丢给了自己所拥有的IWorldAccess**，让它执行了播放声音这个具体的动作。
IWorldAccess只是一个接口，实现它的又是什么东西呢？笔者在客户端和服务端分别打印了World.worldAccesses列表的内容，发现是这样的：
客户端：[net.minecraft.client.renderer.RenderGlobal]
服务端：[net.minecraft.world.WorldManager]
哦。所以，这个方法在客户端和服务端的行为还是有很大不同的。在服务端，它调用了**WorldManager.playSound**；在客户端，它调用了**RenderGlobal.playSound**。
让我们接下来看看这两个类的playSound又具体干了什么吧——
**服务端：**
这是WorldManager里的代码：

```java
public void playSound(String name, double x, double y, double z, float volume, float pitch) {
    this.mcServer.getConfigurationManager().sendToAllNear(
        x, y, z, volume > 1.0F ? (double)(16.0F * volume) : 16.0D, this.theWorldServer.provider.dimensionId, 
        new S29PacketSoundEffect(name, x, y, z, volume, pitch));
}
```

可以看到，这个方法的本质是发送packet（网络包）到附近的客户端，让它们播放声音。它创建了一个S29PacketSoundEffect包（包含声音播放的一切必要信息），然后把它发送给了那个x, y, z位置附近的玩家。
那么这个包被谁处理了呢？经过一番跳转，发现在*NetHandlerPlayerClient*类里做了这个包的处理（也仅有这个类做了）：

```java
public void handleSoundEffect(S29PacketSoundEffect p_147255_1_) {
    this.gameController.theWorld.playSound(
        p_147255_1_.func_149207_d(),
        p_147255_1_.func_149211_e(),
        p_147255_1_.func_149210_f(), 
        p_147255_1_.func_149212_c(), 
        p_147255_1_.func_149208_g(), 
        p_147255_1_.func_149209_h(), 
        false);
}
```

  我就不反混淆了，这有点心塞。。总之，接到这个包以后，mc在客户端调用了*world.playSound*方法。



**客户端：**
这是RenderGlobal里的代码：（吐槽：这取名方式，渲染器为啥要处理音效播放啊天啊噜）  

```java
public void playSound(String par1Str, double par2, double par4, double par6, float par8, float par9) {}
```

  惊了，再次惊了。RenderGlobal里的playSound竟然是一个**空方法**！
这也就意味着，你在客户端调用World.playSoundEffect是**毫无意义**的！

**总结：World.playSoundEffect在client端没有意义，在server则会进行发包，让所有附近的client都被调用World.playSound方法。**



**·playSoundAtEntity方法**
这个方法中可以看到这样的代码：  

```java
for (int i = 0; i < this.worldAccesses.size(); ++i) {
    ((IWorldAccess)this.worldAccesses.get(i)).playSound(
        par2Str, 
        par1Entity.posX,
        par1Entity.posY - (double)par1Entity.yOffset,
        par1Entity.posZ,
        par3,
        par4);
}
```

  那么可以明白，这个方法只是绕了个弯子调用了*IWorldAccess.playSound*方法而已，它和*World.playSoundEffect*方法完全相同，因此可以忽略它。



**·playSoundToNearExcept方法**
用和之前相似的分析可以发现，这个方法在client端不进行任何动作，在server端则是对除调用玩家实例以外的附近玩家发送了一个S29PacketSoundEffect包。所以，它的功能**和playSoundEffect是相近的**，只不过它确实会不让声音在那个玩家处播放。



是不是有点晕了？停几分钟总结一下吧。基本上：

*playSoundAtEntity*和*playSoundToNearExcept***可以忽略**，而*playSoundEffect***只在服务端**有动作（发包），*playSound***只在客户端**有动作。
吐槽：Forge你什么鬼畜命名方式！！这么起名字谁知道用哪个啊！不分析肯定会用错啊摔！



## **2、渐入佳境：SoundHandler和SoundManager**

经过上面对World的分析，我们知道了这么几件事：
·要播放声音，你应该在客户端调用**World.playSound**，或在服务端调用***World.playSoundEffect***。
·不论哪种播放方式，调用链最终都会到达***WorldClient.playSound***，在那里，*Minecraft.getSoundHandler().playSound(...)*方法会被调用。
也就是说，我们接下来只需要研究*getSoundHandler()*返回值所代表的类——**SoundHandler**就可以了。

而SoundHandler专注丢锅100年：  

```java
/**
 * Play a sound
 */
public void playSound(ISound isound) {
    this.sndManager.playSound(isound);
}

```

它直接把ISound实例丢给了自己保存的*sndManager*，其他啥都没干。
等等，ISound是啥？
回到之前WorldClient的代码：

```java
//创建声音播放实例信息
PositionedSoundRecord psr = new PositionedSoundRecord(new ResourceLocation(name), volume, pitch, (float)x, (float)y, (float)z);
```

  哈，这样就明白了。*PositionSoundRecord*是一个实现了*ISound*的类，而MC用这个类来处理一般的声音播放信息，所以传过来的时候，就已经是一个ISound接口了。



看看ISound接口提供了什么信息：  

```java
public interface ISound
{
    ResourceLocation getPositionedSoundLocation();
    boolean canRepeat();
    int getRepeatDelay();
    float getVolume();
    float getPitch();
    float getXPosF();
    float getYPosF();
    float getZPosF();
    ISound.AttenuationType getAttenuationType();
}
```

  声音的ResourceLocation，坐标，音量音高。等等，貌似还有些有趣的参数……**衰减类型、是否重复播放、重复播放的延迟**？卧槽声音系统还可以干这些事啊！
研究到这里咱也是有些小期待了，如果能用mc自带的接口做循环播放什么的那真是**事半功倍**啊。好了，继续继续~



接下来我们回到正题，看SoundManager的*playSound*方法。
呃，这个方法……很臭很长，所以我们不贴代码了，直接贴简化过的伪代码。这样可以更清楚看到调用的过程：
*注意：略去了一些无关紧要的错误检测。尽管如此代码还是很令人烦躁，你可以直接略过看分析，影响不大。  

```java

public void playSound(ISound snd) {
    SoundEventAccessorComposite accessor = this.sndHandler.getSound(snd.getPositionedSoundLocation());
    if (accessor == null) {
        警告输出：未注册的声音;
    } else {
        SoundPoolEntry entry = accessor.getEntry(); //***从之前的accessor获取声音的 资源文件
        if (entry == SoundHandler.missing_sound) {
             警告输出：注册了声音，但是没有找到;
        } else {
            float volume = snd.getVolume();
            float wtf = 16.0F; //谜之参数，可能是衰减系数之类？
            if (volume > 1.0F) wtf *= volume;
            SoundCategory category = accessor.getSoundCategory(); //获取声音分类
            float normalizedVolume = this.getNormalizedVolume(snd, entry, category); //获取标准化后的音量
            double normalizedPitch = (double)this.getNormalizedPitch(snd, entry); //获取标准化后的音高
            ResourceLocation res = entry.getSoundPoolEntryLocation(); //获取声音资源路径
            if (normalizedVolume == 0.0F) {
                logger.debug(marker, "Skipped playing sound {}, volume was zero.", new Object[] {res});
             } else {
                boolean isLooping = snd.canRepeat() && snd.getRepeatDelay() == 0; //这个声音是否需要一直不停的循环
                String randUUID = UUID.randomUUID().toString(); //*****获取一个随机的UUID，作为这次声音播放的唯一标识符
                //Create the buffer for this rand UUID.
                if (entry.isStream()) { //这个声音需要作为流播放（较长的声音，比如唱片）
                    this.sndSystem.newStreamingSource(false, randUUID, getURLForSoundResource(res), res.toString(), isLooping, snd.getXPosF(), snd.getYPosF(), snd.getZPosF(), snd.getAttenuationType().getTypeInt(), wtf);
                    MinecraftForge.EVENT_BUS.post(new PlayStreamingSourceEvent(this, snd, randUUID));
                } else { //这个声音直接丢入缓存被播放（一般声音，音效）
                     this.sndSystem.newSource(false, randUUID, getURLForSoundResource(res), res.toString(), isLooping, snd.getXPosF(), snd.getYPosF(), snd.getZPosF(), snd.getAttenuationType().getTypeInt(), wtf);
                     MinecraftForge.EVENT_BUS.post(new PlaySoundSourceEvent(this, snd, randUUID));
                }

                logger.debug(marker, "Playing sound {} for event {} as channel {}", new Object[] {entry.getSoundPoolEntryLocation(), accessor.getSoundEventLocation(), randUUID});
                //设置声音系统的状态
                this.sndSystem.setPitch(randUUID, (float)normalizedPitch);
                this.sndSystem.setVolume(randUUID, normalizedVolume);
                this.sndSystem.play(randUUID);
                
                //更新一些查询信息（当前播放的声音表等）
                this.playingSoundsStopTime.put(randUUID, Integer.valueOf(this.playTime + 20));
                this.playingSounds.put(randUUID, snd);
                this.playingSoundPoolEntries.put(snd, entry);
                 if (category != SoundCategory.MASTER) {
                    this.categorySounds.put(category, randUUID);
                }
                if (snd instanceof ITickableSound) {
                     this.tickableSounds.add((ITickableSound)snd);
                }
            }
        }
    }
}

```

  实际上，所有的播放动作就在这一个函数中被完成了。尽管代码有点长，但是我们还是归纳出了它大概干了什么。**有几点是我们必须注意到的**：
1 它从SoundHandler中获取了一个***SoundEventAccessorComposite***，并且由它来进行一些资源存在性的判断。
2 它使用一个UUID来标示每一个播放事件。
3 它将这个播放的声音的信息**更新到了一些表**上，这表明它后面应该还需要**检查并且更新播放信息**。
4 它使用*getURLForSoundResource*来获取***\*真正的\****声音文件流，传递给声音系统。

我们首先来看看2和3。进一步观察SoundManager类，发现在*updateAllSounds()*里会更新当前播放的声音的信息，伪代码大概是这样的：  

```java
public void updateAllSounds() {
    // 遍历tickableSounds表，并且更新所有TickableSound的声音信息（如果停止播放了，则循环播放或移除）;
    // 遍历playingSounds表，并且更新所有playingSound的信息（如果停止播放了，则循环播放或移除);
    // 遍历delayedSounds表，如果某个声音打达到了播放的时间，开始播放;
}
```

  果然和我们在3中的猜测相符。注意到里面的*TickableSound*处理。这也就意味着我们可以**在任意时刻改变一个声音的特性，包括音量和音高**。以及，一般的Sound和TickableSound都可以循环播放（虽然有可能很麻烦，不过mc**提供了这样的接口**）。

那么，声音系统在明面上干的事大概就到此为止了。然而，我们还有最后的一根线没有串联起来：**疑点1**。*SoundEventAccessorComposite*是什么？它是在哪里创建的？这个疑点也把它和最初的”声音缓存“的概念联系起来。**json文件是在哪里处理的？处理以后变成了什么**？
接下来，我们就会步入声音系统分析的最后一个部分：一般声音的加载方法。



## **3、柳暗花明：一般声音的加载方法。**

于是，我们开始追踪SoundEventAccessorComposite这个类的来龙去脉。其实楼主在这个逻辑里绕了半天，曾一度被mc的代码所困而不可自拔……总之，很绕啦。所以我们还是直接看分析的结果吧：
首先，*SoundHandler.getSound(ResourceLocation src)*会回调它的sndRegistry实例的getObject(src)方法。这个方法会直接查一个HashMap，返回对应的结果。
其次，我们发现这个sndRegistry在SoundHandler的**注册事件**中被修改了：在S***oundHandler.onResourceManagerReload(IResourceManager par1ResourceManager)***方法中，它打开了每一个资源空间所对应的sounds.json，并且遍历加载了里面的所有声音，其核心调用代码是：  

```java
this.loadSoundResource(new ResourceLocation(s, (String)entry.getKey()), (SoundList)entry.getValue());
```

所以，soundList被传到了**loadSoundResource**
方法中，这个方法又干了什么呢？它的代码又臭又长，这时候我们就要请出伪代码大法：　

```java
private void loadSoundResource(ResourceLocation location, SoundList soundList) {
    SoundEventAccessorComposite soundeventaccessorcomposite;

    if (this.sndRegistry.containsKey(location) && !soundList.canReplaceExisting()) { //如果该声音已经存在
        soundeventaccessorcomposite = (SoundEventAccessorComposite)this.sndRegistry.getObject(p_147693_1_);
    } else {
        soundeventaccessorcomposite = new SoundEventAccessorComposite(location, 1.0D, 1.0D, soundList.getSoundCategory());
        this.sndRegistry.registerSound(soundeventaccessorcomposite); //如果是新创建的SEAC，注册到sndRegistry里
    }

    for (final SoundList.SoundEntry soundentry : soundList) { //遍历soundList
        Object object = 根据soundentry的EntryType，创建一个正确的ISoundEventAccessor;
        soundeventaccessorcomposite.addSoundToEventPool((ISoundEventAccessor)object); //加入
    }
}
```

总结一下：获取（如果已经存在并且不覆盖）或者创建一个SoundEventAccessorComposite，遍历这个SoundList里的所有entry，并且**将它们正确加载以后加入这个composite当中**。并且，这个SoundEventAccessorComposite被加入sndRegistry的表中，以供之后**每次播放的查询**。
到此为止，整个声音播放路径的分析就告一段落了，而SoundEventAccessorComposite的作用也变得明晰：它允许存储一堆真正（真的代表一个声音路径）的ISoundEventAccessor，并且在实际播放的时候**随机的返回一个声音**。如果你往更深的地方探究，这里的随机算法实际上是**有权重**的，不过咱们就先不深究了>)



  **总结，未完问题的探究**
到这里，我们终于对Minecraft的声音播放机制有了一个完整的认识。它的调用路径大概如下：
（服务端：***World.playSoundEffect***）->客户端：***World.playSound***->***Minecraft.sndHandler.playSound***->***SoundHandler.sndManager.playSound***->从sndRegistry获取注册好的SoundEventAccessorComposite，用它**随机获取一个音效进行播放**
注册链：***SoundHandler.loadSoundResource(...)***->***Json反序列化***->一堆<ResourceLocation, SoundList->推送到**SoundHandler.sndRegistry**
更新和回收链：**SoundHandler.updateAllSounds()**->各个表的更新

不深入探究的话或许永远没有办法看到事物背后的本质。尽管笔者研究了一晚上肝到凌晨两点，不过能把这一整个系统梳理通感觉还是非常开心的。不知道乃看到这里的时候感觉如何呢？
最后，我们来解决一下我们一开始留下的问题吧：  



**如何实现一个循环播放多少时间然后自动停止的声音？**



由现在的知识，我们知道，Minecraft**自己已经提供了一个循环播放的接口**。然而，我们却没有明确的**通过World**访问这个接口的方法。要使用循环播放功能，我们必须**实现一个自己的ISound接口，并且在canRepeat()方法处返回true**。当然，这带来一个很显然的问题：

**·你没法使用MC原生的网络同步，也就是说，你要么只在本机播放自定义声音，要么自己写同步的逻辑。**

所以，要写一个自定义声音的同步就意味着你得自己写一个信息包……开始变得有些烦人了呢。不过，即便如此，上面的工作还是在理论上变得可行了。并且通过对系统完整的分析，我们基本可以确定这是最简单的方法。



实际上，对于MC自己的自定义声音，它也是这么处理的，比如说MovingSoundMinecart，就**只是在客户端被播放**的。



之前在说updateAllSounds的时候，我们提到了一个遍历tickableSounds表的动作，却没有提到它具体处理的是什么。
实际上，它处理的是ITickableSound对象。ITickableSound是一类可以**每个tick进行一定更新动作的声音**。它可以更新自己的音量和音高，并且声音系统会**实时的**将这些更新更新到SoundSytem中。
MC原生的矿车移动的音效就是这么实现的（在每个tick判断矿车移动的速度，并且提升或降低音量）。
用这个接口可以实现一些比较复杂的声效，但是代价是，**你得自己写同步，或者完全放弃同步**。



于是到这里，这个帖子基本可以告一段落了。我们从最上层的World的声音调用接口开始，一路分析到了SoundHandler的播放机制和声音加载机制。MC的代码**并不是完美**的，比如说自定义声音得全部自己处理同步，就是它的缺陷之一。相信读到这里的你，已经对MC的声音播放系统有了全面的了解~嘛嘛，应该会觉得有所收获的吧？



那么，这篇分析贴就到此为止了~以后有时间的话咱还会陆续写一些类似的技术分享的哟！民那桑，再会~



## **最后：黑MC**

**你知道矿车的音效是怎么处理的么？**？？？

```java
/**
 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
 */
public void mountEntity(Entity par1Entity) {
    super.mountEntity(par1Entity);

    if (par1Entity instanceof EntityMinecart)
    {
        this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)par1Entity));
    }
}
```

说真的，看到这段代码的时候我**差点没把桌子掀了**。
对于初学者来说这可能是绝对合理的代码，但是如果是对面向对象有经验的人，绝对会捂脸爆头痛哭……
**·这段代码耦合度极高。**
**·这段代码将不相关的内容（坐上实体和播放声音）强行揉到了一起。**
对于只有一个实体有自定义声音的情况，那可能还好。但是万一以后你要增加更多的有自定义声音的实体呢？

```java

if(entity instanceof EntityA) {
...
} else if(entity instanceof EntityB) {
...
} else if(entity instanceof EntityC) {
...
} else if(entity instanceof EntityC) {
...
}
...

```

你不会真的想写这样的代码吧。
这样的代码的最大坏处就是你**把执行同一功能的代码分散到了许多难以维护的地方**。假设某一天你需要删掉这个声音的功能，你真的能想起来它在【坐上实体的函数】里么？
MC的代码有的时候写的真的很糟糕，所以**参考需谨慎**。

> C++大法好，退mod坑保平安。（雾

总之，大家在modding的时候还是要注意自己的代码的简洁清晰度，在制作内容的时候也务必养成作为一个程序员的好代码习惯哦~~



Fin
如果这篇文章的内容对乃有所帮助的话，还请支持一下哟~~=3=



https://www.mcbbs.net/thread-429752-1-1.html