Vulkan 创建窗口的过程与 OpenGL 过程相似，区别在于：
- 禁用 OpenGL 上下文：`GLFW_CLIENT_API` 为 `GLFW_NO_API`，且不设置 OpenGL 上下文和模式
- 不需要 GLAD

> [!attention] 但后面初始化 Vulkan 比 OpenGL 麻烦的多

```cpp title:glfwWindow.cpp
GLFWwindow *pWindow;
GLFWmonitor *pMonitor;
const char* windowTitle = "EasyVK";

/// 初始化窗口
/// \param size 窗口大小
/// \param fullScreen 全屏
/// \param isResizable 可拉伸
/// \param limitFrameRate 帧数限制是否不超过刷新率
/// \return 是否成功初始化
bool InitializeWindow(VkExtent2D size, bool fullScreen = false, bool isResizable = true, bool limitFrameRate = true) {
    // 初始化 GLFW
    if (!glfwInit()) {
        std::cout << "[ InitializeWindow ] ERROR\n"
                     "Failed to initialize GLFW!\n";
        return false;
    }
    // 禁用 OpenGL 上下文
    glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
    glfwWindowHint(GLFW_RESIZABLE, isResizable);
    // 创建窗口
    pMonitor = glfwGetPrimaryMonitor();
    const GLFWvidmode *mode = glfwGetVideoMode(pMonitor);
    if (fullScreen) {
        pWindow = glfwCreateWindow(mode->width, mode->height, windowTitle, pMonitor, nullptr);
    } else {
        pWindow = glfwCreateWindow(size.width, size.height, windowTitle, nullptr, nullptr);
    }
    if (!pWindow) {
        std::cout << "[ InitializeWindow ] ERROR\n"
                     "Failed to create a glfw window!\n";
        glfwTerminate();
        return false;
    }

    return true;
}
```

![[../OpenGL/GL 窗口|GL 窗口]]
# FPS

```cpp
//
// Created by lq200 on 25-3-12.
//

#include <iostream>

#include "GLFW/glfw3.h"

class Fps {
public:
    Fps() : dframe(-1), time1(0), last_fps(0) {
        time0 = glfwGetTime();
    }

    // 每次渲染时运行
    void loop();
    // 获取上次计算的 FPS
    double fps() const;

private:
    double time0, time1;
    int dframe;
    double last_fps;
};

void Fps::loop() {
    dframe++;
    time1 = glfwGetTime();

    double dt = time1 - time0;
    // 每间隔 1s 计算一次帧率
    if (dt >= 1) {
        last_fps = dframe / dt;
        time0 = time1;
        dframe = 0;
    }
}

double Fps::fps() const {
    return last_fps;
}
```
# 切换全屏与窗口

全屏与窗口使用 `glfwSetWindowMonitor` 设置，`refreshRate` 表示刷新率

```cpp
// 全屏
void glfwWindow::setFullScreen() {
    auto mode = glfwGetVideoMode(pMonitor);
    glfwSetWindowMonitor(pWindow, pMonitor, 0, 0, mode->width, mode->height, mode->refreshRate);
}

// 窗口化
void glfwWindow::setWindowScreen(VkOffset2D position, VkExtent2D size) {
    auto mode = glfwGetVideoMode(pMonitor);
    glfwSetWindowMonitor(pWindow, nullptr, position.x, position.y, size.width, size.height, mode->refreshRate);
}
```
