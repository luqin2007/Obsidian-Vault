通过 OpenGL 配合 GLFW，GLAD 创建并显示一个纯色窗口

1. 初始化 GLFW 并使用 `glfwWindowHint` 设置窗口上下文使用的 OpenGL 版本
    1. `GLFW_CONTEXT_VERSION_MAJOR`，`GLFW_CONTEXT_VERSION_MINOR` 选择 OpenGL 版本
    2. `GLFW_OPENGL_PROFILE` 选择模式，`GLFW_OPENGL_CORE_PROFILE` 表示使用 Core 模式，默认兼容模式
    3. 若操作系统为 MacOS，还需要添加 `GLFW_OPENGL_FORWARD_COMPAT` 为 `GL_TRUE` 兼容
2. 创建窗口，注意此时不要显示窗口
3. 初始化 GLAD
4. 显示窗口

```cpp
#include <glad/glad.h>
#include <GLFW/glfw3.h>

GLFWwindow* window = nullptr;

int main() {
    // 初始化及创建窗口
    glfwInit();
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    window = glfwCreateWindow(窗口宽度, 窗口高度, "窗口标题", nullptr, nullptr);
    glfwMakeContextCurrent(window);

    gladLoadGLLoader(reinterpret_cast<GLADloadproc>(glfwGetProcAddress));

    glfwSwapInterval(1);
    glfwShowWindow(window);
    // 窗口初始化完成
}
```

整个初始化过程我们需要注意几个先后关系：

1. 在调用任何 OpenGL 相关函数之前，必须先导入 `glad.h` 头文件，即 `#include <glad/glad.h>`
2. Java 下，调用任何 OpenGL 函数前，包括 `glViewport` 等，必须先使用 `GL.createCapabilities()` 初始化 GL 上下文

至此，窗口即可创建成功。

> 以上代码没有做异常处理。
>
> * `glfwInit` 返回 `GLFW_FALSE` 即 0 时表示 GLFW 初始化异常
> * `glfwCreateWindow` 返回空指针时表示窗口创建失败

# 窗口事件

创建窗口后，还需要绑定必要的 GLFW 窗口事件回调。

```cpp
// 窗口初始化完成
// 绑定窗口事件
glfwSetFramebufferSizeCallback(window_, [](GLFWwindow*, const int width, const int height)
{
	glViewport(0, 0, width, height);
});
// 窗口事件绑定完成
```

* `glfwSetFramebufferSizeCallback`：窗口绘制区域（帧缓冲区）发生变化时调用，通常代表调整的窗口大小。该回调中通常调用 `glViewport` 重置 OpenGL 视口
* `glfwSetMouseButtonCallback`：鼠标按键状态变更时触发
    * `button`：触发的按钮，可选 `GLFW_MOUSE_BUTTON_LEFT`、`GLFW_MOUSE_BUTTON_RIGHT`、`GLFW_MOUSE_BUTTON_MIDDLE`
    * `action`：按键变动情况
        * `GLFW_RELEASE`：按键弹起
        * `GLFW_PRESS`：按键按下
    * `mod`：组合键
* `glfwSetCursorPosCallback`：鼠标移动时触发
* `glfwSetScrollCallback`：鼠标滚轮滚动时触发
* `glfwSetKeyCallback`：键盘按钮状态变更时触发
    * `key`：按钮，值为 `GLFW_KEY_对应按键`
    * `scancode`：某些按键可能没有对应的按钮，该值为每个按键的唯一扫描值
    * `action`：按键变动情况
        * `GLFW_RELEASE`：按键弹起
        * `GLFW_PRESS`：按键按下
        * `GLFW_REPEAT`：按键长按
    * `mod`：组合键
# 窗口渲染

窗口成功创建后，便可以进行绘制了。通常我们会使用一个循环绘制，`glfwWindowShouldClose` 可用于判断窗口是否应当关闭。

我们通过将颜色缓冲区的颜色清空成一个纯色，使窗口显示一个纯色的背景。

```cpp
// 窗口事件绑定完成
// 窗口绘制循环
while (!glfwWindowShouldClose(window))
{
    // 清空缓冲区
    glClearColor(1, 1, 1, 1);
    glClear(GL_COLOR_BUFFER_BIT);
    // 绘制开始
    // 绘制结束，交换后台缓冲区
	glfwSwapBuffers(window);
	// 处理输入事件（键盘、鼠标等）
	glfwPollEvents();
}
// 清理资源并结束
glfwDestroyWindow(window);
glfwTerminate();
```
# 窗口销毁

```cpp
// 销毁 GLFW 窗口
glfwDestroyWindow(window);
// 结束 GLFW
glfwTerminate();
```
