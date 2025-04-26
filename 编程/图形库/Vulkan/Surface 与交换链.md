
> [!tip] 如果不需要直接渲染，比如只用于计算或离屏渲染，可以跳过该节 

> [!tip] 交换链需要 `VK_KHR_SWAPCHAIN_EXTENSION_NAME` 扩展
> Vulkan 符合单一职责原则，只负责从 CPU 向 GPU 的通信。而交换链则是对 GPU 端如何渲染的管理，故属于扩展
> 同样的还有 SurfaceKHR 扩展，，不同平台有不同扩展

> [!success] 不用担心可显示设备没有 SwapchainKHR 和 SurfaceKHR 扩展
# 创建 Surface

Surface 用于输出图片到窗口，与平台强相关，可以使用窗口 API 获取具体所需的扩展或查询相关手册
- GLFW：`glfwGetRequiredInstanceExtensions`
- SDL2：`SDL_Vulkan_GetInstanceExtensions`

同样，创建 Surface 也需要平台或窗口库提供的函数
- GLFW：`glfwCreateWindowSurface`
- SDL2：`SDL_Vulkan_CreateSurface`

```cpp
void Context::createSurface(GLFWwindow *window) {
    VkSurfaceKHR vkSurface;
    auto result = glfwCreateWindowSurface(instance, window, nullptr, &vkSurface);
    throwVkError("Failed to create surface!");
    surface = vkSurface;
}
```
## 创建显示队列

判断是否支持显示的队列使用 `physicalDevice.getSurfaceSupportKHR`

在前面 Device 选择时，判断并记录了图像队列，这里类似，将记录显示队列的逻辑插进去就行了

```cpp hl:9,19,20,26-31,44-50,56
void Context::pickupPhysicalDevice() {
    auto devices = instance.enumeratePhysicalDevices();
    for (auto &physicalDevice: devices) {
        // 可在此处遍历物理设备特性
        const auto& features = physicalDevice.getFeatures();
        if (!features.geometryShader) continue;
        // 检查队列
        queryQueueFamilyIndices(physicalDevice);
        if (queueFamilyIndices.graphicsQueue && queueFamilyIndices.presentQueue) {
            phyDevice = physicalDevice;
            break;
        }
    }
    cout << "Picked physical device: " << phyDevice.getProperties().deviceName << endl;
}

void Context::queryQueueFamilyIndices(const PhysicalDevice &physicalDevice) {
    const auto& properties = physicalDevice.getQueueFamilyProperties();
    queueFamilyIndices.presentQueue.reset();
    queueFamilyIndices.graphicsQueue.reset();
    for (int i = 0; i < properties.size(); ++i) {
        auto flags = properties[i].queueFlags;
        if (flags & QueueFlagBits::eGraphics) {
            queueFamilyIndices.graphicsQueue = i;
        }
        if (physicalDevice.getSurfaceSupportKHR(i, surface)) {
            queueFamilyIndices.presentQueue = i;
        }
        if (queueFamilyIndices.graphicsQueue && queueFamilyIndices.presentQueue) {
            break;
        }
    }
}

void Context::createDevice() {
    DeviceCreateInfo createInfo;
    DeviceQueueCreateInfo graphicsQueueCreateInfo, presentQueueCreateInfo;
    float queuePriority = 1.0f;
    std::vector<DeviceQueueCreateInfo> queueCreateInfos;
    graphicsQueueCreateInfo.setQueuePriorities(queuePriority)
            .setQueueCount(1)
            .setQueueFamilyIndex(*queueFamilyIndices.graphicsQueue);
    queueCreateInfos.push_back(graphicsQueueCreateInfo);
    // 如果两个队列不是同一个队列，则创建两个队列
    if (queueFamilyIndices.graphicsQueue != queueFamilyIndices.presentQueue) {
        presentQueueCreateInfo.setQueuePriorities(queuePriority)
                .setQueueCount(1)
                .setQueueFamilyIndex(*queueFamilyIndices.presentQueue);
        queueCreateInfos.push_back(presentQueueCreateInfo);
    }
    std::vector<const char*> extensions = { VK_KHR_SWAPCHAIN_EXTENSION_NAME };
    createInfo.setQueueCreateInfos(queueCreateInfos)
              .setPEnabledExtensionNames(extensions);
    device = phyDevice.createDevice(createInfo);
    graphicsQueue = device.getQueue(*queueFamilyIndices.graphicsQueue, 0);
    presentQueue = device.getQueue(*queueFamilyIndices.presentQueue, 0);
}
```

> [!note] 这里找的是同时满足图像和显示的设备。大部分情况都满足，但也可以使用不同设备分别用于图像和显示
# 创建交换链

[[CreateInfo 信息#SwapchainCreateInfoKHR|交换链创建]]时很多信息都需要从设备 Surface 信息 `VkSurfaceCapabilitiesKHR` 获取

```cpp title:Swapchain::initialize
void Swapchain::initialize(const VkExtent2D& size) {
        // 查询相关数据
        auto &context = Context::get();
        for (auto &fmt: context.phyDevice.getSurfaceFormatsKHR()) {
            if (fmt.format == Format::eR8G8B8A8Srgb && fmt.colorSpace == ColorSpaceKHR::eSrgbNonlinear) {
                format = fmt;
                break;
            }
        }
        auto capabilities = context.phyDevice.getSurfaceCapabilitiesKHR(context.surface);
        imageCount = std::clamp(2u, capabilities.minImageCount, capabilities.maxImageCount);
        imageExtent = {
                std::clamp(size.width, capabilities.minImageExtent.width, capabilities.maxImageExtent.width),
                std::clamp(size.height, capabilities.minImageExtent.height, capabilities.maxImageExtent.height),
        };
        transform = capabilities.currentTransform;
        presentMode = PresentModeKHR::eFifo;
        auto presents = context.phyDevice.getSurfacePresentModesKHR(context.surface);
        for (auto &present: presents) {
            if (present == PresentModeKHR::eMailbox) {
                presentMode = present;
                break;
            }
        }

        createInfo.setClipped(true)
                .setImageArrayLayers(1)
                .setImageUsage(ImageUsageFlagBits::eColorAttachment)
                .setCompositeAlpha(CompositeAlphaFlagBitsKHR::eOpaque)
                .setSurface(context.surface)
                // 查询出的信息
                .setImageColorSpace(format.colorSpace)
                .setImageFormat(format.format)
                .setMinImageCount(imageCount)
                .setImageExtent(imageExtent)
                .setPresentMode(presentMode);

        if (context.queueFamilyIndices.graphicsQueue == context.queueFamilyIndices.presentQueue) {
            createInfo.setQueueFamilyIndices(context.queueFamilyIndices.graphicsQueue.value())
                    .setImageSharingMode(SharingMode::eExclusive);
        } else {
            std::array indices = {
                    context.queueFamilyIndices.graphicsQueue.value(),
                    context.queueFamilyIndices.presentQueue.value()
            };
            createInfo.setQueueFamilyIndices(indices)
                    .setImageSharingMode(SharingMode::eConcurrent);
        }
        swapchain = context.device.createSwapchainKHR(createInfo);
    }
```
## 图像大小 imageExtent

该设备信息从 `surfaceCapabilities` 的 `currentExtent` 属性获取。`-1` 表示当前 Window Surface 大小未指定，此时可以指定一个默认值。

```cpp title:Swapchain::initialize
imageExtent = {
        std::clamp(size.width, capabilities.minImageExtent.width, capabilities.maxImageExtent.width),
        std::clamp(size.height, capabilities.minImageExtent.height, capabilities.maxImageExtent.height),
};
```
## 图像队列数量 minImageCount

通常，若容许的最小值与最大值数量不等，取最小值 + 1，这里直接取 2
- 图像队列不宜过少，否则会产生阻塞，即需要渲染一个新图像时，图像队列满（正在被呈现或被渲染）；
- 图像队列不宜过多，否则产生过多的显存开销

> [!note] 一般 `surfaceCapabilities.minImageCount` 为 2，多一张图可实现三重缓冲

```cpp title:Swapchain::initialize
imageCount = std::clamp(2u, capabilities.minImageCount, capabilities.maxImageCount);
```
## 透明通道模式 compositeAlpha

该参数指定处理交换链图像透明度通道的方式
- `eOpaque`：不透明，`alpha` 通道视为 1
- `ePreMultiplied`：预乘透明度模式
- `ePostMultiplied`：后乘透明度模式
- `eInherit`：由应用程序其他部分指定

> [!note] 透明度不一定由 Vulkan 决定，还有可能被窗口系统影响。此时应将 `compositeAlpha` 设置为 `VK_COMPOSITE_ALPHA_INHERIT_BIT_KHR`
## 图像用途 imageUsage

使用 `surfaceCapabilities.supportedUsageFlags` 获取所有可用的用途
- 至少有一个 `eColorAttachment` 表示颜色附件
- `eTransferDst`：数据传输目标，可用于 `vkCmdClearColorImage` 或 `vkCmdBlitImage`
- `eTransferSrc`：数据传输来源，可用于窗口截屏
## 图像格式与色彩空间

图像格式 `Format` ，色彩空间 `ColorSpaceKHR`
- 图像格式可以不指定（即 `VK_FORMAT_UNDEFINED`）
- 必须指定色彩空间，色彩空间对后续贴图格式、着色器色调映射影响很大
- 可能需要在已经开始的情况下修改色彩空间，需要重建交换链

图像格式具体可见[[图像#图像格式]]

```cpp title:Swapchain::initialize
for (auto &fmt: context.phyDevice.getSurfaceFormatsKHR()) {
    if (fmt.format == Format::eR8G8B8A8Srgb            /*RGBA各8位*/
    && fmt.colorSpace == ColorSpaceKHR::eSrgbNonlinear /*SRGB 非线性排列*/) {
        format = fmt;
        break;
    }
}
```
## 呈现模式 presentMode

```cpp title:Swapchain::initialize
presentMode = PresentModeKHR::eFifo;
auto presents = context.phyDevice.getSurfacePresentModesKHR(context.surface);
for (auto &present: presents) {
    if (present == PresentModeKHR::eMailbox) {
        presentMode = present;
        break;
    }
}
```

> [!note] Vulkan 一定支持 FIFO

优先选择 `eMailbox`，垂直同步选择 `eFifo`
- `eImmediate`：立即模式，不限制帧率，不等待垂直同步信号，渲染后立即呈现，可能画面撕裂
- `eFifo`：先进先出，与屏幕刷新率一致，必定支持。来不及呈现的图片存于队列
- `eFifoRelaxed`：先进先出，但来不及呈现的图片直接丢弃，可能造成画面撕裂
- `eMailbox`：类似于三重缓冲的模式，渲染缓冲只有一个元素，有新渲染的图片时立刻呈现缓存内的图片

![[../../../_resources/images/Pasted image 20250315003029.png|250]]  ![[../../../_resources/images/Pasted image 20250315002949.png|250]]  ![[../../../_resources/images/Pasted image 20250315003141.png|250]]
## 共享模式

一个交换链可能被多个队列访问，例如图形队列与呈现队列不同。此时应当设置 `queueFamilyIndices` 和 `imageSharingMode`

```cpp
if (context.queueFamilyIndices.graphicsQueue == context.queueFamilyIndices.presentQueue) {
    createInfo.setQueueFamilyIndices(context.queueFamilyIndices.graphicsQueue.value())
            .setImageSharingMode(SharingMode::eExclusive);
} else {
    std::array indices = {
            context.queueFamilyIndices.graphicsQueue.value(),
            context.queueFamilyIndices.presentQueue.value()
    };
    createInfo.setQueueFamilyIndices(indices)
            .setImageSharingMode(SharingMode::eConcurrent);
}
```
## 透明窗口背景

一种实现透明背景窗口的方法

- `glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GL_TRUE)`，实质是 `DwmEnableBlurBehindWindow`
- `compositeAlpha` 设置为 `eInherit`
- 使用 `VkClearColorValue({0f, 0f, 0f, 0f})` 清屏
- `presentMode` 设置为 `eImmediate` 或 `eMailbox`（但不限制帧数，浪费性能）
