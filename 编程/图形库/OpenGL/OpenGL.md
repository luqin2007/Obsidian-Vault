OpenGL 是一套用于屏幕渲染的 C/S 架构的 C 库，只定义 API 规范，具体通常由显卡驱动或操作系统实现。
- 服务端：负责渲染的部分，通常由显卡执行
- 客户端：CPU 和代码中的程序部分

早期 OpenGL 使用立即渲染模式，即固定管线。GL 3.2 后，默认使用核心渲染模式，即使用可编程管线。

OpenGL 本体可以看做是一个巨大的状态机，整个 OpenGL 的状态称为 OpenGL 上下文。

> 通常来说，OpenGL 只作为渲染工具，配套还有其他相关库辅助使用
>
> * [GLFW](https://www.glfw.org)：创建窗口并初始化 OpenGL 上下文
> * [GLAD](https://glad.dav1d.de)：检查和使用扩展
> * [GLM](https://github.com/g-truc/glm)：OpenGL Mathematics，OpenGL 的数学（尤其是矩阵）库
> * [[Assimp]]：模型加载库

- [[环境配置]]
- [[GL 窗口]]
- [[GLSL]]
- [[纹理]]
- [[变换]]
- [[3D 坐标空间]]
- [[Phong 光照模型]]
- [[光源]]
- [[深度测试]]
- [[模板测试]]
- [[混合]]
- [[面剔除]]
- [[帧缓冲]]
- [[立方体贴图]]
- [[缓冲区]]
- [[几何着色器]]
- [[实例化渲染]]
- [[间接渲染]]
- [[抗锯齿与多重采样]]
- [[Blinn-Phong 光照模型]]
- [[Gamma 校正]]

# 参考

```cardlink
url: https://learnopengl-cn.github.io/
title: "主页 - LearnOpenGL CN"
description: "http://learnopengl.com 系列教程的简体中文翻译"
host: learnopengl-cn.github.io
```

‍
