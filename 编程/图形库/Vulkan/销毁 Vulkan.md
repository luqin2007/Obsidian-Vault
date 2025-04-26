
1. 等待逻辑设备空闲

```cpp title:Device::~Device
// 等待逻辑设备空闲
vkDeviceWaitIdle(handle);
```

2. 销毁交换链、逻辑设备内等

```cpp title:Swapchain::~Swapchain
// 销毁交换链
if (handle) {
    invokeDestroyCallbacks;
    destroyDeviceHandles(vkDestroyImageView, swapchainImageViews);
    destroyDeviceHandle(vkDestroySwapchainKHR);
    swapchainImages.resize(0);
    swapchainCreateInfo = {};
}
```

```cpp title:Device::~Device
// 销毁逻辑设备
invokeDestroyCallbacks;  
destroyHandle(vkDestroyDevice);
```

3. 销毁 WindowsSurface

```cpp title:VulkanInstance::~VulkanInstance
// 删除 Surface
if (surface) {
    vkDestroySurfaceKHR(handle, surface, nullptr);
}
```

4. 销毁 DebugMessenger（仅调试）

```cpp title:DebugMessenger::~DebugMessenger
if constexpr (ENABLE_DEBUG) {
    if (!handle) return;
    if (auto destroyFunc = getProcAddr(vkDestroyDebugUtilsMessengerEXT)) {
        destroyInstanceHandle(destroyFunc);
    }
}
```

5. 销毁 Vulkan Instance

```cpp title:VulkanInstance::~VulkanInstance
// 销毁 Vulkan Instance
destroyHandle(vkDestroyInstance);
```

6. 释放其他资源

```cpp title:Device::~Device
physicalDevice = VK_NULL_HANDLE;
```

7. 销毁 GLFW 等其他上下文对象

```cpp title:VkWindow::~VkWindow
if (pWindow) {
    glfwDestroyWindow(pWindow);
    glfwTerminate();
    pWindow = nullptr;
    pMonitor = nullptr;
}
```