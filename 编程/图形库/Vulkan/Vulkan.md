Vulkan 是 [[../OpenGL/OpenGL|OpenGL]] 的继任者，相比 OpenGL 有更加现代化的抽象，相比 Direct3D 原生跨平台。
- [[环境配置]]
- [[GLFW]]
# Vulkan 对象创建

Vulkan 对象创建通常分为两步：
- 创建对应的 `XxxCreateInfo` 对象，提供对应的信息
- 调用 `CreateXxx` 方法，传入 info 结构体，返回该对象

> [!note] 对应 C 头文件，创建方法为 `vkCreateXxx`，需要传入一个 `VkXxx` 的指针

> [!tip] Cpp 头文件创建的 Info 对象中包含一系列 set 方法，可链式调用，推荐使用方法而不是直接设置值
# Vulkan 对象关系

- 环境相关
	Vulkan 实例 Instance：包含一系列 Vulkan 运行必须的状态和信息的变量
- 设备相关
	物理设备 PhysicalDevice：表示一个 Vulkan 可调用的设备，通常指显卡。该对象只能获取设备相关信息，不能直接控制
	逻辑设备 Device：编程层面上与物理设备交互的对象，管理物理设备中的资源（内存缓冲区、管线等）
- 绘制相关
	管道 Queue：CPU 侧（代码）向 GPU 侧（设备）提交指令的通道
	命令缓冲 CommandBuffer：用于存放一组 CPU 向 GPU 提交的指令，可批量向 GPU 提交指令
	命令池 CommandPool：管理命令缓冲，向 GPU 提交命令缓冲中的所有命令
	交换链 Swapchain：选择 GPU 中的图像用于显示
	Surface：用于输出图像到设备显示
	交换链图像 Image：交换链中渲染的图片，是一种图像
	图像视图 ImageView：访问图像 Image 的方式

# 渲染过程

1. 创建 [[Vulkan 实例]]，作为 Vulkan 应用的根节点
2. 选择物理[[设备]]，创建逻辑设备，创建队列
3. 创建 [[Surface 与交换链]]
4. 创建[[交换链图像]]
5. 编译并加载[[管线和着色器|着色器]]
6. 创建[[渲染管线]]
7. 创建命令池和[[命令缓冲区]]
8. [[开始渲染]] *~~至此终于画出三角形了~~*
# 其他对象

- [[销毁 Vulkan]]
- [[基础渲染循环]]
	- [[即时帧]]
	- [[队列族所有权转移]]
- [[同步原语]]
	- [[栅栏]]
	- [[信号量]]
	- [[管线屏障]]
	- [[事件]]
- 内存与缓冲区
	- [[设备内存]] DeviceMemory
	- [[缓冲区]] Buffer
	- [[图像]] Image
- 
- [[描述符]]
- [[采样器]]
- [[查询]]
- [[着色器编译]]
- [[新版本特性]]
- [[无图像帧缓冲]]
- [[动态渲染]]
- [[基础示例]]
- [[简单示例]]
# 参考

```cardlink
url: https://space.bilibili.com/256768793/lists/404887
title: "单身剑法传人的个人空间-单身剑法传人个人主页-哔哩哔哩视频"
description: "哔哩哔哩单身剑法传人的个人空间，提供单身剑法传人分享的视频、音频、文章、动态、收藏等内容，关注单身剑法传人账号，第一时间了解UP主动态。没事造轮子。QQ吹水群905217220"
host: space.bilibili.com
```

```cardlink
url: https://easyvulkan.github.io/
title: "首页 — EasyVulkan"
host: easyvulkan.github.io
```

```cardlink
url: https://geek-docs.com/vulkan/vulkan-tutorial/vulkan-tutorial-index.html
title: "Vulkan 教程|极客教程"
description: "Vulkan 教程，Vulkan是Khronos Group(OpenGL标准的维护组织)开发的一个新API，它提供了对现代显卡的一个更好的抽象，与OpenGL和Direct3D等现有api相比，Vulkan可以更详细的向显卡描述你的应用程序打算做什么，从而可以获得更好的性能和更小的驱动开销。Vulkan的设计理念与Direct3D 12和Metal基本类似，但Vulkan作为OpenGL的替代者"
host: geek-docs.com
```
