# 从零开始的MC特效

> 本教程转自 [[Tutorial\][G-Second][应该全核心]从零开始的MC特效](http://www.mcbbs.net/thread-783358-1-1.html)
>  原作者是我 (小声

### 目录:

- 导读
- 数学与Minecraft的融合
- 利用数学在Minecraft中画一个圆
- 利用数学在Minecraft中画一个球

------

##### 导读

本教程需要读者有一定的空间想象能力(因为我也懒得画图了233)
 本教程使用的 Spigot1.10.2-R0.1-SNAPSHOT 核心
 在阅读之前请确保你具有高中数学必修4和Java基础的知识
 (没有我也会适当的解释的)

<初中生>: 如果你是初中的话，别慌，你有函数的概念就可以读懂本教程(应该吧...)
 <高中生>: 如果你还未学到关于上面的两本书，别慌学到了再来看也行233 (雾

------

##### 数学与Minecraft的融合

首先我们都知道Minecraft是一个3D游戏，所以它就有了XYZ这三个轴，那么我们可以看如下的一张图来了解一下



![img](https:////upload-images.jianshu.io/upload_images/8109631-2d19dbfb979d9c1c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/500/format/webp)

image

**本教程暂不涉及关于Y轴的相关内容，所以我们可以先从平面直角坐标系来分析**

##    ![img](https:////upload-images.jianshu.io/upload_images/8109631-bdac487be24902d3.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1000/format/webp)  image 

##### 利用数学在Minecraft中画一个圆

**以下内容需要sin函数与cos函数的相关知识！**
 首先呢我们先来看一张图（自己用word画的2333）
 



![img](https:////upload-images.jianshu.io/upload_images/8109631-0332f34619e797e3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/470/format/webp)

image



**分析:**
 首先这个坐标系有一个**单位圆**(半径为1的圆)，然后我们看到**角α为30°**，之后**点P的横坐标为 √3/2 纵坐标为 1/2**

然后我们再看下图



![img](https:////upload-images.jianshu.io/upload_images/8109631-17c456f2fb78ae8e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/349/format/webp)

image

那么我们是否可以这么认为，点P的横坐标其实是 cos(30°) 而纵坐标就是 sin(30°)呢？

> 补充: 如果你已经有了参数方程的概念那么，这里你可以忽略了 —— 2019/1/12

> PI 代表圆周率π, 之后π = 180° (别问我为什么，课堂上自己学去233)*

**P(cos(30°), sin(30°))， 弧度制: P(cos(PI/6), sin(PI/6))**

那么P的横坐标和纵坐标都是可以利用函数 cos和sin 求出，那么我们为什么不可以**遍历一下把360°全部都给算出**呢？所以我看写出下方的代码这样我们就可以把一周角里所有的角度都给遍历了一便，并且我们都算出了**每个角度所对应的cos值和sin值**吧，然后我们需要把他们作用到Minecraft当中

```
// 我们把玩家脚下的location作为是原点O 
Location location = player.getLocation(); 
for (int degree = 0; degree < 360; degree++) {
    double radians = Math.toRadians(degree);
    double x = Math.cos(radians);
    double y = Math.sin(radians);
}
```

那么在上图的for循环语句块中我们有两个变量 x y，也就是 **P(x, y)** 吧，之后我们回头看一下for循环语句块外的那个**变量location**，那个我们可以理解成是在上图中的**原点O**，那么我们做个假设，我们需要把点P转换成MC中的Location要怎么做？，其实很简单

```
location.add(x, 0, y);
```

我们把location的X轴假想为0, Z轴假想为0（这里的X轴和Z轴指的是Minecraft中的那两个轴）即**图中原点O为(0, 0)**，那么**在Minecraft中不可能任何时候原点的X Z轴都是0**，所以我们需要做相加的操作

（上面可能会听得一头雾水，简单来说当**原点O不为(0, 0)时**，假设为(2, 2)，那么我们要做的是给**玩家的周围建立圆**吧，那么这时候点P的坐标应该为 **P(2 + x, 2 + y)）**

> 要是还听不懂的话那就去喝杯茶，洗个澡吧2333

那么我们可以做以下的操作了

```
location.add(x, 0D, y);
// 播放粒子
location.getWorld.playEffect(location, Effect.HAPPY_VILLAGER, 1);
// 为什么要减？因为我们要确保原点是不变的状态才可以哦~
location.subtract(x, 0D, y);
```

游戏内效果





![img](https:////upload-images.jianshu.io/upload_images/8109631-512ddf977900c346.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1000/format/webp)

游戏内的效果

完整代码

```
// 我们把玩家脚下的location作为是原点O
Location location = player.getLocation();
for (int degree = 0; degree < 360; degree++) {
    double radians = Math.toRadians(degree);
    double x = Math.cos(radians);
    double y = Math.sin(radians);
    location.add(x, 0, y);
    location.getWorld().playEffect(location, Effect.HAPPY_VILLAGER, 1);
    location.subtract(x, 0, y);
}
```

##### 利用数学在Minecraft中画一个球

首先我们来观察一下sin的**函数图像**，具体如下



![img](https:////upload-images.jianshu.io/upload_images/8109631-c004142745f40231.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/450/format/webp)

图 2-1

从上图可以看出 sin函数 始终在 1~-1 之间徘徊，所以我们认为它是有**周期性**的，那么这跟球的生成有什么联系呢？我们看下图



![img](https:////upload-images.jianshu.io/upload_images/8109631-0a5961213221bee9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/609/format/webp)

图 2-2

首先这是一个球对吧，然后呢我在球上画了几个**横截面(**才不是什么旋风**)**出来，那么通过上图我们是不是可以得出一个结论，一个球体其实是由**无数个圆**构成的？只是**它们的半径不同**对吧。那这跟sin函数有啥联系呢？

首先我们回到sin的函数图像，我们看**当x在0~π之间时**连起来的y轴是不是像一个半圆啊？而且它们的**半径(这里的半径可以理解为sin函数中的y轴)**也是不同的，那么我们是不是可以这么认为，我们只需要 **0 ~ \**\**π** 之间的x值，然后代入函数当中就可以求出对应的y轴的值了？

那么 **0 ~ π** 是什么值呢？其实在上面的圆中我就讲过 **π=180°，**所以我们求得其实就是 **sin(0 ~ 180°)。**

那么有了上面的思路我们可以**求出每个圆的半径**对吧，那么我们写出下面的代码

```
for (double i = 0; i < 180; i++) {
    double radians = Math.toRadians(i);     
    double radius = Math.sin(radians);
}
```

在上方的代码当中我们求出了一个球中每个圆的半径, 但是我们还需要考虑一件事，我们是不是要规定一下每个圆之间的距离啊？

那么我们引入**cos**的函数图像
 



![img](https:////upload-images.jianshu.io/upload_images/8109631-1c1ac82cccdbfd35.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/407/format/webp)

图 2-3



从上图可以看出 f(x) = cos(x),
 当**x=0**时, f(x)则为1.
 当**x=π**时，f(x)则为-1.

那么我跟sin的函数图像联系一下, 在上面的代码中我们发现，radius的值是从小到大再到小，那么我们想一下，如果半径是小的那么那个圆是也是小的，而我们要画的圆是**从上往下**画的（观察图 2-2）对吧，所以我们是不是要给那个**最小的圆的y轴是最高的**？（没看懂？喝杯茶吧）而**cos函数**就可以帮我们达到这一点，所以我们写出以下的代码

`double y = Math.cos(radians);`
 那么这样我们就可以获得**当前for循环时**那个圆的高度了。

在上面的结构中我们得到了当前圆的**半径和高度**，那么我们要怎么通过这两个东西画出来呢？

我们在第三章画圆时曾经做过这么一个操作

```
for (int degree = 0; degree < 360; degree++) {
    double radians = Math.toRadians(degree);    
    double x = Math.cos(radians);     
    double y = Math.sin(radians);
}
```

上方的代码中我们只能制造出一个**半径为1**的圆，那么我们想扩大它的半径需要怎么做？

我们这里又引入一个函数**y=Asin(ωx + φ)** *(这里的sin也可以为cos)*，其实这个函数跟sin函数差不多只不过多了几个变量，那么这里我们只需要考虑A的值，

为什么呢？我们来看一下这个函数在数学上的定义：

- *φ（初相位）：决定波形与X轴位置关系或横向移动距离（左加右减）*
- *ω：决定周期（最小正周期T=2π/|ω|）*
- **A：决定峰值（即纵向拉伸压缩的倍数）**

**由于这里我们只考虑A所以我们可以把上方的函数简写为 y = Asin(x)，假设我们的A为2，那就是sin(x) \* 2了，那么反应在函数图像上是这样的**





![img](https:////upload-images.jianshu.io/upload_images/8109631-d8fa9a9d746e0d0c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/450/format/webp)

**image**



那么有了上面的概念**我们不妨使用 Math.cos(x) \* 半径 来扩大本次循环时所对应的半径**，所以我们写出以下的代码

```
for (double j = 0; j < 360; j ++) {
    // 依然需要做角度转弧度的操作 
    double radiansCircle = Math.*toRadians*(j);     
    double x = Math.*cos*(radiansCircle) * radius;     
    double z = Math.*sin*(radiansCircle) * radius;
}
```

那么这样就可以控制好本次循环我们需要这个圆多少半径了，那么我们写好之后就可以放在Minecraft中看看效果

完整代码:

```
for (double i = 0; i < 180; i++) {
    // 依然要做角度与弧度的转换
    double radians = Math.toRadians(i);
    // 计算出来的半径
    double radius = Math.sin(radians);
    double y = Math.cos(radians);
    for (double j = 0; j < 360; j++) {
        // 依然需要做角度转弧度的操作
        double radiansCircle = Math.toRadians(j);
        double x = Math.cos(radiansCircle) * radius;
        double z = Math.sin(radiansCircle) * radius;
        location.add(x, y, z);
        location.getWorld().playEffect(location, Effect.HAPPY_VILLAGER, 1);
        location.subtract(x, y, z);
    }
}
```

游戏内的效果





![img](https:////upload-images.jianshu.io/upload_images/8109631-78b42b28cadfa8ba.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/916/format/webp)

效果

然后你就会发现你的游戏卡得一匹2333，因为我们是在360°全方位的进行渲染粒子的操作2333，但实际业务中我们可能并不需要做这种需求，那么我们就需要做一个关于跳跃的操作呗，我们看下面的代码

```
for (double i = 0; i < 180; i += 180 / 6) {
    // 依然要做角度与弧度的转换
    double radians = Math.toRadians(i);
    // 计算出来的半径
    double radius = Math.sin(radians);
    double y = Math.cos(radians);
    for (double j = 0; j < 360; j += 180 / 6) {
        // 依然需要做角度转弧度的操作
        double radiansCircle = Math.toRadians(j);
        double x = Math.cos(radiansCircle) * radius;
        double z = Math.sin(radiansCircle) * radius;
        location.add(x, y, z);
        location.getWorld().playEffect(location, Effect.HAPPY_VILLAGER, 1);
        location.subtract(x, y, z);
    }
}
```

跟上面的代码不同的是我在遍历的时候修改了**步长(step)**，那这一个有什么讲究呢？我们在每一次循环给 i 和 j就加的是**30**了对吧，而不是自加1，

那么我们看**第一层循环**，这一层循环控制的步长其实是我们其实需要多少圈，为什么呢？我们看下面的图来理解一下



![img](https:////upload-images.jianshu.io/upload_images/8109631-c5aa3f61a470cab2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/248/format/webp)

image

这里我为了方便读者理解我把图 2-1 旋转了一下，上图我们假想黑点是玩家的location，那么那几个红点就是我们把**步长**修改后所得到的产物





 那么第二层循环我修改的步长又是什么意思呢？我们也拿张图来理解一下



![img](https:////upload-images.jianshu.io/upload_images/8109631-0163144e4c0e42f6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/529/format/webp)

image

上图中每个角的度数都是30°，那么我修改了步长之后是不是我只会在这几个黑点上面做playEffect()的操作了？（看不懂的话喝口水再来看233）

修改了步长后游戏内的效果:





![img](https:////upload-images.jianshu.io/upload_images/8109631-46416920308dc784.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/838/format/webp)

image

#### 结语

内容依然是挺少的。。希望能教给读者一些东西吧233