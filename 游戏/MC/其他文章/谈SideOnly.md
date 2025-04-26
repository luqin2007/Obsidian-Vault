**本文章转自 WeAthFolD's Inverted Field (http://weathfold.moe)，二次转载请征求作者许可。**

​        好久没写技术分享或者教程类的东西了，在最近总算是抽空写了一篇自己一直想整理的题目。SideOnly的问题，但凡是稍大的Mod项目都会遇到。在多次被坑的基础上，咱整理出了一些SideOnly相关的讨论和通常的处理经验，希望能对各位Modder有些用吧 >)

# 概述

​        SideOnly是一个Mod编程中至关重要的概念，它对Mod的服务端稳定性有至关重要的影响，却又很难搞对。本文会对SideOnly作一些讨论，并介绍一些和它相关的设计技巧和方法。

# SideOnly

​        服务端兼容是一个在Minecraft modding中很重要的话题。Minecraft的运行基于客户端/服务端模式，并且客户端和服务端有很大一部分代码是共享的。在客户端，由于有单人模式的需求，所以我们几乎可以访问到服务端的所有代码；然而在服务端中，关于客户端的代码，很多是不需要而且不应该被访问的——比如说渲染、声音、GUI支持。这些支持通常要引用一些第三方库——比如javax，lwjgl等，而这些库在服务器环境中并不需要也不存在。

​        在开发Mod时，我们的Mod会同时运行于服务端和客户端，它们共享的却是同一套代码段。为了不让Mod在服务端运行时（错误的）访问或者加载客户端部分的依赖，FML为我们提供了一些必要的工具——其中最主要的两个是SidedProxy和SideOnly。

​        SidedProxy解决的主要是在加载阶段进行C/S分别加载的需求。通过使用 `@SidedProxy` 注解标记需要的代理实例，可以实现在客户端和服务端加载时分别构造不同的对象，并且通过接口的多态特性实现客户端的独立加载。

​        然而，只是在注册阶段进行分别的处理并不能满足需求。这是因为，在运行时我们常常需要根据运行端执行一些独立的逻辑。在这个过程中，对客户端代码的引用几乎是不可避免的，比如，如果我想在右键点击某个物品时，打开一个GUI：  

```java
public MyItem extends Item {
    public void onItemRightClick(EntityPlayer player, ItemStack stack, ...) {
        if (player.worldObj.isRemote) { // 如果在客户端，打开GUI...
            // 首先获取客户端游戏实例
            Minecraft minecraft = Minecraft.getMinecraft(); // 在服务器崩溃：不存在的类定义
            minecraft.displayGuiScreen(new MyGui());
        }
    }
}
```

​        上面这段代码，会导致服务器在加载或运行阶段漂亮的崩溃。虽然if语句块中的代码并不会在服务端执行，但是这段代码的字节码仍然留在了类文件中。在加载这个类，或者直接/间接引用这个方法的某个时刻**[1]**，java会需要尝试去解析这个方法所引用的类，而这显然会引起一个**NoClassDefFoundError**，导致游戏崩溃。

***[1]***：在后文“崩溃的时机”一节对具体细节有详细的讨论。

怎么解决这个问题呢？这时候就要用到FML提供的**@SideOnly**注解了。以下是其javadoc的翻译：  

>@SideOnly 注解让其标记的对象只在给定的Side可用。这个注解通常来说只供Forge和FML内部使用，在Mod类中，只应该在通常的机制，比如说 SideProxy 失效时使用。注意，本注解只会作用于其直接标记的对象。这段代码： ```@SideOnly public MyField field = new MyField();``` 不会工作，因为字段的加载和字段声明是分开的，在无效端中，加载块将无法找到这个字段的声明，导致崩溃。

```java
public MyItem extends Item {
    @SideOnly(Side.CLIENT)
    public void onItemRightClick(EntityPlayer player, ItemStack stack, ...) {
        if (player.worldObj.isRemote) { // 如果在客户端，打开GUI...
            // 首先获取客户端游戏实例
            Minecraft minecraft = Minecraft.getMinecraft(); // 在服务器崩溃：不存在的类定义
            minecraft.displayGuiScreen(new MyGui());
        }
    }
}
```

​        只需要对该方法加上这样的一个标记，它就会在服务端运行时被移除，只在服务端存在。我们也不会因为对客户端类的错误引用而引起崩溃了。

## SideOnly的细节

​        在这个注解的注释中，只说明了它会“让标记的对象变为仅在某端可用”，而没有说明具体的细节。对于每种不同的标记类型，它的行为实际上都有所不同：

- 对类：当在错误端尝试加载一个类时，会抛出一个异常，阻止这个类的加载。
- 对方法：会让这个方法在运行时被擦除。也就是，它的方法签名和字节码都不会包含在运行时的类中。
- 对字段：会让这个字段的**声明**在运行时被擦除。然而，它的**加载语句**并不会被擦除。

​        关于这些行为，值得进行一些附加的说明。

​        首先是对类的行为：一个类尝试被加载，当且仅当它被另外一个类引用。也就是说，不合法的跨端引用会导致崩溃，这也是上一节示例中造成崩溃的原因。

​        对方法来说，这个方法的所有内容被擦除，这意味着方法中不合法的跨端引用也随之不存在了。对方法标记@SideOnly可以避免跨端引用带来的崩溃。

​        对字段来说，需要牢记的就是它的**加载语句不会被擦除**，所以一个SideOnly的字段**只能被初始化为默认值**。如果要对SideOnly的字段进行加载，必须在运行时对端进行判断，然后调用一个SideOnly方法手动加载它。

## 其他类型的跨端引用

​        在前文中，我们讨论的主要是**对SideOnly类的引用**。除此之外还有两种类型：**方法对SideOnly字段的引用**和**方法对SideOnly方法的引用**（字段对方法的引用并不存在，因为加载块也是方法的一部分）。

​        相较于对类的引用，对方法或对字段引用造成崩溃的条件则较为松散：当且仅当你在错误端**实际执行了存在无效引用的代码**，才会造成崩溃。

​        比如说，下面的代码是正确的：  

```java
public void onItemUse(World world, ...) {
    if (world.isRemote) { // 在客户端打开GUI，但是为了避免对类的跨端引用，把实现写到一个SideOnly方法中
        openTheGui();
    } else {
        ... // 进行一些服务端的工作
    }
}

@SideOnly(Side.CLIENT)
private void openTheGui() {
    Minecraft.getMinecraft().displayGuiScreen(new MyEpicGui());
}
```

​        以上这种技巧被我称作***side hack***，在日常编程中非常常用。在编写一些客户端相关的逻辑时可能经常会用到它。

## 崩溃的时机

​        对于一个类引用另一个类造成的崩溃，其具体崩溃时机是不确定的。根据[Java语言规范12.1.2节]([http://docs.oracle.com/javase/sp ... -12.html#jls-12.1.2](http://www.mcbbs.net/plugin.php?id=link_redirect&target=http%3A%2F%2Fdocs.oracle.com%2Fjavase%2Fspecs%2Fjls%2Fse8%2Fhtml%2Fjls-12.html%23jls-12.1.2))所述：

> The resolution step is optional at the time of initial linkage. An implementation may resolve symbolic references from a class or interface that is being linked very early, even to the point of resolving all symbolic references from the classes and interfaces that are further referenced, recursively.  ... The only requirement on when resolution is performed is that any errors detected during resolution must be thrown at a point in the program where some action is taken by the program that might, **directly or indirectly, require linkage to the class or interface involved in the error**.

​       也就是说，这个类可能在被加载时立刻崩溃，也可能在调用方法时崩溃，具体的时机是由JRE的实现来决定的……无论如何，对SideOnly类的引用是一个危险的定时炸弹，永远不要在没有标记SideOnly的域或者方法上这么做。

​       另外，在之前的示例中，我们只讨论了一种对类的引用——在代码体中的引用。正式的来说，对类的引用包含如下几种类型：

- 字段类型直接/间接引用某类
- 方法签名（参数列表/返回值）直接/间接引用某类
- 在代码体中直接/间接引用某类

​       这三种引用会带来的问题都是相同的。  

## 总结

​       SideOnly是用来避免**错误的跨端引用**的一种基础方法。通过对方法或字段标记SideOnly，可以在另一端运行时消除它们的存在，从而消除错误的跨端引用造成的崩溃。

​       如果在某些情况下，需要在运行时在某端调用一些会引用SideOnly类的方法，可以使用**side hack**的技巧来防止崩溃。

​       需要注意的是，SideOnly的所有行为都是**运行时**的。在编译期对错误的跨端引用不会进行任何检查和提示。这意味着我们在编写代码时一定要对跨端引用小心谨慎。  

# 设计准则

以下是一些我在开发过程中总结出的应对SideOnly的经验：

1. 少用SideOnly

    SideOnly无法在编译期提供任何检查。当你的mod代码数量提高时，很难保证在每一个细节都在开发时处理完美。所以最好的方法就是尽可能减少对它的使用。

2. 设计清楚C/S的交互逻辑

    将跨端调用的情形控制到最小，只保留必要的桥接部分——比如说打开Gui的情形，在控制代码处只引用`Minecraft`类和Gui类，剩余的处理工作交由Gui类本身完成（它访问客户端的类是安全的）。进行良好的设计对条目1本身也有很大的帮助。

3. 在跨端交互逻辑非常多时，考虑将客户端/服务端模块分离

    Forge的 SidedProxy 本身就是这种情况的一个特例。如果在某些系统里，跨端交互的密度非常大，以至于客户端的东西和服务端的完全混在了一起，可以考虑通过某些方法把客户端的逻辑独立出来。具体怎么做就要看那个系统的需求了。（AcademyCraft的1.0.0版本的技能系统在这方面做了一些[积极的尝试](https://github.com/LambdaInnovat ... gClientContext.java) :-)

4. It's fundamentally flawed!

    可是……我们真的不可能完全在开发时消灭SideOnly问题！因为这个问题在客户端测试的时候很难暴露出来，所以很有可能积压到开发后期，进行服务端测试的时候才会暴露出来。而SideOnly提供的报错信息，有时候又很难追查到真正的出错点……总之，必须承认，SideOnly这个机制虽然可用，但是存在着很多问题。

# 走的更远？

我们真的可以在编译期完全消除SideOnly带来的崩溃问题。

根据上面的讨论，SideOnly的崩溃会在以下两种情况下发生：

* 在非SideOnly方法中引用了SideOnly类 （一定会崩溃）
* 在非SideOnly方法/字段中引用了SideOnly方法 （可能会崩溃，如果没有进行运行端判断）

我们只要检查一个类对SideOnly的方法/字段/类的引用，就可以直接判断出这个方法的调用是否安全。我们可以以以下两种方式实现这个机制：

* gradle的task（静态检查并报错）
* eclipse/idea的插件（警告，自动代码修复）

如果有时间，我是完全想试试做一下这件事的。当然也欢迎任何有兴趣的人去完成它。我相信这会是对Mod开发的一个极大帮助>_>