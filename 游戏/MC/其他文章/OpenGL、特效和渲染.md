# *第一章 MC渲染基础**

## **OpenGL、坐标变换、贴图**

在这一章，我们并不会接触任何实际的Modding代码。然而，这一章所描述的基本概念却**至关重要**，在从粒子特效到物品、方块渲染的范畴内，这些在MC中关于渲染的基本方法都在无时无刻的被应用着。总而言之，打好基础是十分重要的，所以请打起精神来哦~
由于个人才疏学浅，教程的说明难免有错漏之处，还请各位及时指出咯~> <

**注意：OpenGL的部分略有难度，如果觉得理解困难可以多读几次或者选择性略过。**



## **OpenGL，lwjgl和镶嵌器**

MC的整个渲染引擎，基于开源游戏API：[lwjgl](https://www.mcbbs.net/plugin.php?id=link_redirect&target=http%3A%2F%2Fwww.lwjgl.org%2F)的[OpenGL](https://www.mcbbs.net/plugin.php?id=link_redirect&target=http%3A%2F%2Fzh.wikipedia.org%2Fzh-cn%2FOpenGL)部分而搭建。这也就意味着MC的渲染过程中，会大量的用到OpenGL的方法（所以，在之前就写过GL程序的同学有福了~~）。

不过，MC并不趋向于让使用者全部直接调用OpenGL的方法。为了处理光照、法线贴图等等属性的绘制，MC将OpenGL的绘制函数进行了一个包装，也就是Tessellator（镶嵌器）类。所有添加顶点、绘制多边形的操作，都需要通过这个类来进行。



在一个绘制过程中，我们可以这样获取Tessellator的实例：

```java
Tessellator t = Tessellator.instance;
```

然后，通过

```java
t.startDrawing(int MODE);
```

来开始一次绘制动作。

在绘制动作中，你可以设定当前绘制动作的颜色、设置面的法向量，以及添加一个顶点等等：

```java
t.setColorRGBA_F(r, g, b, a);
t.setNormal(u, v, w);
t.addVertexWithUV(x, y, z, u0, v0);
// ……
```

在状态设置结束，添加了顶点之后，结束整个绘制过程：

```java
t.draw();
```

此时，Tessellator会将计算好的渲染数据**传递给OpenGL，进行实际的绘制**。



**说明：Tessellator的方法**

*t.startDrawing(MODE)*：使用指定的绘制模式**开始绘制**。绘制模式和OpenGL的glBegin（MODE）中所使用的常量一模一样，有GL_QUADS, GL_LINES, GL_TRIANGLE_FAN等等，详见[这里](https://www.mcbbs.net/plugin.php?id=link_redirect&target=https%3A%2F%2Fwww.opengl.org%2Fsdk%2Fdocs%2Fman2%2Fxhtml%2FglBegin.xml)

*t.setColorOpaque(), setNormal(), ...*：设置当前绘制过程的**状态量**。这也就意味着如果使用Tessellator进行这些状态的设定的话，同一个绘制call中，不同的顶点是无法使用不同的颜色和法向量值的。如果需要多种状态值，则推荐不同状态值分成不同的绘制call。



以下是一个使用Tessellator绘制一个红色，透明度50%，法向量为(0, 0, 1)的矩形的例子：

```java
Tessellator t = Tessellator.instance;
t.startDrawing(GL11.GL_QUADS); //或t.startDrawingQuads()
t.setColorRGBA_F(1.0F, 0.0F, 0.0F, 0.5F);
t.setNormal(0, 0, 1);
t.addVertex(0.0, 0.0, 0.0);
t.addVertex(1.0, 0.0, 0.0);
t.addVertex(1.0, 1.0, 0.0);
t.addVertex(0.0, 1.0, 0.0);
t.draw(); //结束绘制call
```



##   **释放潜力：使用OpenGL进行其他的图形操作**

在介绍了Tessellator之后，我们已经了解了MC中基本的图形绘制是如何进行的。然而只凭借Tessellator，我们能做到的事是相当受限的。要对我们所绘制的东西进行变换、混合、遮罩操作……我们都需要用到OpenGL的原生函数。
在lwjgl中，OpenGL的库函数被包装在GL[XX](XX是GL版本号）类中。你只需要访问这些类的公有函数就可以调用ＧＬＡＰＩ了。
实际上，大部分的GL函数在**GL11**类当中。


**基本的变换操作**
在MCMod中（以及其他所有的游戏渲染过程中），我们最常遇到的问题就是对绘制的图形进行移动和变换的问题。例如：将在原点绘制好的子弹实体平移到当前实体的位置；根据时间流逝的长度让光束旋转一个角度，等等。在OpenGL中，我们可以进行如下的几种变换：
·**平移** 将所有顶点坐标平移（Δx, Δy, Δz）个单位
-对应函数：***glTranslate\*(dx, dy, dz);***
·**旋转** 将所有顶点绕某个轴，旋转α度。
-对应函数：***glRotate\*(α, u, v, w);***
·**缩放** 将所有顶点关于原点，缩放scale倍。
-对应函数：**glScale\*(scaleX, scaleY, scaleZ);**
关于这些OpenGL变换函数的具体效果和操作方法，请参见网上各种各样的OpenGL参考和教程。我相信随便一个教程都说的比我好>. <

**变换操作和变换的复合（啥？代码要倒着读？！）**
通过以上的三个变换函数，我们已经可以对绘制出来的东西干很多有趣的事了。比如说，下面的代码让我们绘制的东西绕（0, 0, 0），关于Y轴周期性旋转：  

```java
GL11.glRotated(Minecraft.getSystemTime() / 100, 0, 1, 0);
t.startDrawingQuads();
//...
t.draw();
```

  注意，我们的变换方法是在**开始绘制之前**调用的。这是因为每个变换方法实际上都在GL内部**存储了一个状态**。而在绘制的那一刻，GL就会把**之前指定的变换状态进行综合**，再把这个综合的变换结果应用在所有顶点上。所以如果你在绘制之后再调用变换函数的话，就太迟了！（如果你学过线性代数的话，实际上我们在对一个变换矩阵不停的做矩阵乘法）。

一个变换通常是远远不够用的，我们经常想要对一个物体进行很多的**复合变换**。而在进行复合变换的时候，有些神奇的代码规则是你不得不注意的。
例如，下面的代码让绘制的物体**绕中心周期旋转，然后再将它往Y轴正方向移动一个单位**：  

```java
GL11.glTranslated(0, 1, 0);
GL11.glRotated(Minecraft.getSystemTime() / 100, 0, 1, 0);
//你的绘制代码
```

  是的，**变换的顺序和代码的顺序是反过来的**！如果你知道一点关于矩阵的东西，并且想要探究到底是为什么的话，可以读读下面的扩展阅读；否则，请你牢牢的记住这个结论：**在渲染过程中，任何和GL坐标变换的代码应该放在绘制过程上方，而且实际变换顺序和方法的调用顺序恰好相反**。



**扩展阅读：矩阵乘法，左乘和右乘**  

>根据线性代数和计算机图形学知识，我们知道：**一切基本坐标变换都可以以矩阵变换的形式表示**。
>在OpenGL计算中中，任意点的坐标都是用**列向量**表示的：
>
>**![img](https://attachment.mcbbs.net/forum/201502/07/234725gast2z9v3b1atbkt.png)** 
>
>在进行变换的时候，我们只需要把变换矩阵乘以列向量矩阵就可以得到结果的坐标。例如，偏移量为Δx，Δy，Δz的变换矩阵是：
>
>![img](https://attachment.mcbbs.net/forum/201502/07/234728s58k1789039tr212.png) 
>
>**两者相乘，得到结果：**
>
>![img](https://attachment.mcbbs.net/forum/201502/07/234728iz883p8m997booz7.png) 
>
>根据矩阵运算的结合律，所有的变换矩阵**可以在乘以列向量之前进行乘法的复合之后，再乘以列向量**，得到的是和这些矩阵分别乘以列向量相同的结果。也就是：
>
>**T1·T2·T3·...·Tn·V = (T1·T2·T3·...·Tn)·V**
>
>而后一种方法的运算量明显会小很多，因此OpenGL在内部会预先把我们指定的变换矩阵全部相乘，只保存一个总的变换矩阵。
>在添加新的变换矩阵的时候，OpenGL进行的是右乘（也就是把新的矩阵放到当前变换矩阵的最右边，两者相乘）。如果我们依次添加T1，T2，T3，……，我们得到的最终的变换结果是这样的：
>
>**(T1·T2·T3·...·Tn）·V**
>
>让我们从另一个角度看待这个运算：**(T1·(T2·(...·(Tn·V))))**
>
>用语言解释这个运算过程就是：用Tn乘以V得到坐标V1，用Tn-1乘以V1得到坐标V2，……最后得到V’。由于矩阵乘法的运算性质，惊人的事发生了：**实际变换的顺序和我们指定变换矩阵的顺序是完全相反的**！
>参考：
>
>[http://stackoverflow.com/questions/2258910/opengl-scale-then-translate-and-how/2259263#2259263](https://www.mcbbs.net/plugin.php?id=link_redirect&target=http%3A%2F%2Fstackoverflow.com%2Fquestions%2F2258910%2Fopengl-scale-then-translate-and-how%2F2259263%232259263)
>[http://en.wikipedia.org/wiki/Transformation_matrix](https://www.mcbbs.net/plugin.php?id=link_redirect&target=http%3A%2F%2Fen.wikipedia.org%2Fwiki%2FTransformation_matrix)



**变换的复合（记得推栈和弹栈！）**
假设在渲染过程中，我们想要进行一定的复合变换。比如说：让风车的扇叶部分周期旋转，而支架部分固定不动，该怎么办呢？最简单方法是这样的：
（这里假设mainPart和fan都是一个类似模型的类的实例，调用其draw函数会把面绘制出来）

```java
mainPart.draw();
GL11.glTranslated(0, offsetY, 0); //将扇叶移动到正确的位置上
GL11.glRotated(Math.cos(Minecraft.getSystemTime() / 100D), 1, 0, 0); //让扇叶关于中心旋转
fan.draw();
```

  这是基于一个重要的姿势：**坐标变换函数仅对调用之后发生的绘制call有影响**。



然而，假设你为了更好的渲染效果，给这个风车加了两层不同的转动，那么又该怎么绘制呢？  

```java
mainPart.draw();
GL11.glTranslated(0, offsetY, 0); //将扇叶移动到正确的位置上
GL11.glRotated(Minecraft.getSystemTime() / 100D, 1, 0, 0); //让扇叶关于中心旋转
fan.draw();
GL11.glRotated(Minecraft.getSystemTime() / 80D, 1, 0, 0); //让扇叶2关于中心旋转（速度不同）
fan2.draw();
```

  很容易可以意识到，这个方法得到的并不是你想要的效果。fan2的旋转**同时受到了两次旋转变换的影响**，导致了复合变换。
如果我们想要让每个部分**进行分别的变换动作**，**而不让它们影响到彼此**，该怎么办呢？

***答案是下面的代码：***  

```java
GL11.glMatrixMode(GL11.GL_MODELVIEW); //设置当前活跃的矩阵为ModelView（模型视角）矩阵
mainPart.draw();
GL11.glTranslated(0, offsetY, 0); //将扇叶移动到正确的位置上
GL11.glPushMatrix(); //保存
GL11.glRotated(Minecraft.getSystemTime() / 100D, 1, 0, 0); //旋转A
fan.draw();
GL11.glPopMatrix(); //还原
GL11.glPushMatrix(); //保存
GL11.glRotated(Minecraft.getSystemTime() / 80D, 1, 0, 0); //旋转B
fan2.draw();
GL11.glPopMatrix(); //还原
```

  为什么这样可以做到保存状态呢？接下来的篇幅介绍了glPushMatrix和glPopMatrix这对孪生兄弟。





**知识点：glPushMatrix和glPopMatrix**

OpenGL提供了便捷的恢复其某一时刻的部分状态的方法：**推栈和弹栈**。
在这里，我们使用***glPushMatrix***和***glPopMatrix***来存储和恢复当前活跃矩阵的状态。 活跃的矩阵可以用***glMatrixMode（MODE）***指定。除了**Modelview**（模视变换）矩阵，还有**Projection**（投影）矩阵，用来设置**摄像机位置/投影方法**。
你可以把这对函数的功能想象成**对一个栈**的操作（实际上就是……），就像这样：  

![img](https://attachment.mcbbs.net/forum/201502/07/234726iql4z1plusd1bllf.jpg) 

  **glPushMatrix()**和**glPopMatrix()**必须成对使用，**否则会造成栈的上溢和下溢**。



在你的渲染器里面，mc一般会给你传递类似这样的参数：  

```java
public void render(double x, double y, double z, ......)
```

所以对应的，在你的代码里，肯定会有这么一行：

```java
GL11.glTranslated(x, y, z);
```

这个状态设置**既会作用于你当前的绘制语句，也会作用于你的整个绘制函数结束之后的绘制语句**。MC并没有在外面帮你储存变换信息。因此，如果你在绘制的过程中忘记pushMatrix和popMatrix，那么之后的绘制就遭殃了。你**可能看到一堆实体跟随着你自己创建的实体谜之移动**……



**因此，强烈建议把渲染代码写成如下形式：**

```java
//其他状态设置
GL11.glPushMatrix();
//坐标变换
//绘制
GL11.glPopMatrix();
//恢复设置的状态
```

这个注意点也适用于**其他所有的GL状态设置****请务必在绘制完成后将状态复原**



## **总结**

在这一章中，我们简要的介绍了ＭＣ中绘制的基本方法：**OpenGL原生函数**和**镶嵌器**的配合。其中，镶嵌器处理**顶点的基本绘制和一些光照属性**，而OpenGL则处理**这以外的所有工作**（变换，贴图，alpha混合……）。因此，OpenGL基础对MCMod的渲染是至关重要的。我们首先介绍了Tessellator和其基本的**绘制多边形的方法**，然后简要的介绍了OpenGL中最最常用的几个函数：**坐标变换、矩阵的存储与复原**。在你的渲染代码的结尾，一定要记得**将设置过的状态复原**。

尽管都是枯燥繁琐的理论，但是这些东西和实际的实现碰撞起来的时候，才往往能迸发出意想不到的火花呢。那么，准备好进一步往前探索了么？