---
title: "【Vulkan 学习笔记】4.1.3 Validation layers - 哔哩哔哩"
source: "https://www.bilibili.com/opus/965058466867576854"
author:
  - "[[哔哩哔哩]]"
published:
created: 2025-03-14
description:
tags:
  - "clippings"
---
**验证层是什么**

Vulkan 的设计围绕着最小驱动开销的理念，表现之一就是默认情况下 API 中错误检查很少。即使是简单到像错误的枚举值或给必要的参数传了空指针也不会明确处理，而是直接崩溃或者未定义行为。因为 Vulkan 需要你对你的所作所为非常明确，所以很容易发生各种小问题，像是使用一个新的 GPU 特性却忘了在创建逻辑设备时请求它之类的。

不过这并不意味着这些错误检查不能加到 API 里。Vulkan 为此引入了名为验证层的系统。验证层是一个可选的组件，它能插入 Vulkan 的函数调用来进行额外操作。验证曾的常用操作有：

- 检查参数是否符合规范，检测误用
- 跟踪对象创建和销毁，检测资源泄漏
- 跟踪调用来源线程，检测线程安全
- 将每次调用及其参数记录输出到标准输出
- 追踪 Vulkan 调用，用于分析和回放

下面这个例子展示了一个诊断验证层中的函数实现可能长什么样子：

```cpp
VkResult vkCreateInstance(
    const VkInstanceCreateInfo* pCreateInfo,
    const VkAllocationCallbacks* pAllocator,
    VkInstance* instance) {

    if (pCreateInfo == nullptr || instance == nullptr) {
        log("Null pointer passed to required parameter!");
        return VK_ERROR_INITIALIZATION_FAILED;
    }

    return real_vkCreateInstance(pCreateInfo, pAllocator, instance);
}
```

这些验证层可以任意堆叠来引入你想要的所有调试功能。你可以简单地在调试时启用它们，然后在发布时完全禁用，豪用！

Vulkan 没有内置验证层，不过 LunarG Vulkan SDK 提供了一系列检查常见错误的验证层。它们也是完全开源的，你可以产看它们检测和贡献哪些错误。使用验证层是避免你的应用意外使用了未定义行为而在不同驱动上出错的最好方式。

验证层只有在系统中安装后才能使用。举个例子，只有安装了了 Vulkan SDK 的电脑才能使用 LunarG 验证层。

以前 Vulkan 里有两种验证层：实例验证层和特定设备验证层。实例层只会检查与全局 Vulkan 对象（如实例）相关的调用，而特定设备层只会检查与特定 GPU 相关的调用。设备特定层现在已被弃用，这意味着实例验证层适用于所有 Vulkan 调用。为某些实现所需的兼容性，规范文档仍然建议你在设备级别启用验证层。我们只需在逻辑设备级别指定与实例相同的层，后面章节会有提及。

**使用验证层**

这部分我们来看看如何启用 Vulkan SDK 提供的标准分析层。和扩展一样，验证曾需要通过指定名称启用。所有有用的标准验证都被打包在 SDK 里一个叫 VK\_LAYER\_KHRONOS\_validation 的层里。

首先我们来添加两个配置变量，一个用来指定要启用的层，一个用来指定是否启用它们。后面这个值基于程序是否在调试模式编译。这里用 C++ 标准里的 NDEBUG 宏，它的意思是“非调试模式”。

```cpp
const uint32_t WIDTH = 800;
const uint32_t HEIGHT = 600;

const std::vector<const char*> validationLayers = {
    "VK_LAYER_KHRONOS_validation"
};

#ifdef NDEBUG
    const bool enableValidationLayers = false;
#else
    const bool enableValidationLayers = true;
#endif
```

我们添加一个新的方法 checkValidationLayerSupport 来检测是否所有需要的层都是可用的。首先用 vkEnumerateInstanceLayerProperties 列出所有可用的层。它的用法和 vkEnumerateInstanceExtensionProperties 差不多，上一章刚刚说过。

```cpp
bool checkValidationLayerSupport() {
    uint32_t layerCount;
    vkEnumerateInstanceLayerProperties(&layerCount, nullptr);

    std::vector<VkLayerProperties> availableLayers(layerCount);
    vkEnumerateInstanceLayerProperties(&layerCount, availableLayers.data());

    return false;
}
```

接下来，检查是否 validationLayers 中的层在 availableLayers 里都有。你可能需要包含

```cpp
for (const char* layerName : validationLayers) {
    bool layerFound = false;

    for (const auto& layerProperties : availableLayers) {
        if (strcmp(layerName, layerProperties.layerName) == 0) {
            layerFound = true;
            break;
        }
    }

    if (!layerFound) {
        return false;
    }
}

return true;
```

然后在 createInstance 里使用这个方法：

```cpp
void createInstance() {
    if (enableValidationLayers && !checkValidationLayerSupport()) {
        throw std::runtime_error("validation layers requested, but not available!");
    }

    ...
}
```

现在在调试模式下运行程序，确保不会发生这个错误。如果发生了，右转 FAQ。

最后，修改 VkInstanceCreateInfo 的初始化代码，在启用验证层时添加它们：

```cpp
if (enableValidationLayers) {
    createInfo.enabledLayerCount = static_cast<uint32_t>(validationLayers.size());
    createInfo.ppEnabledLayerNames = validationLayers.data();
} else {
    createInfo.enabledLayerCount = 0;
}
```

如果检测成功，vkCreateInstance 就不会返回 VK\_ERROR\_LAYER\_NOT\_PRESENT 错误，再运行一次确认一下。

**消息回调**

验证层默认会把调试信息打印到标准输出，但我们也可以用一个回调来自己处理。这样你可以自己决定哪些消息要显示，因为不是所有消息都很重要（致命）。如果你不想这么干，你也可以跳到这章的最后一部分。

要在程序里设置一个用来处理消息和相关细节的回调，我们需要用 VK\_EXT\_debug\_utils 设置一个带有回调的调试发信器。

我们首先创建一个 getRequiredExtensions 方法，它根据验证曾是否启用返回需要的扩展列表：

```cpp
std::vector<const char*> getRequiredExtensions() {
    uint32_t glfwExtensionCount = 0;
    const char** glfwExtensions;
    glfwExtensions = glfwGetRequiredInstanceExtensions(&glfwExtensionCount);

    std::vector<const char*> extensions(glfwExtensions, glfwExtensions + glfwExtensionCount);

    if (enableValidationLayers) {
        extensions.push_back(VK_EXT_DEBUG_UTILS_EXTENSION_NAME);
    }

    return extensions;
}
```

GLFW 指明的扩展总是需要的，但调试发信器扩展会视情况添加。注意这里我用了 VK\_EXT\_DEBUG\_UTILS\_EXTENSION\_NAME 宏，它相当于字面量 “VK\_EXT\_debug\_utils”。用宏可以避免打错字。

接下来在 createInstance 里调用方法：

```cpp
auto extensions = getRequiredExtensions();
createInfo.enabledExtensionCount = static_cast<uint32_t>(extensions.size());
createInfo.ppEnabledExtensionNames = extensions.data();
```

运行程序，确保你不会收到 VK\_ERROR\_EXTENSION\_NOT\_PRESENT 错误。我们不用检查这个扩展是否真的存在，因为验证层可用就意味着它是可用的（是这个意思不）。

现在我们来看一下调试回调函数长什么样。根据 PFN\_vkDebugUtilsMessengerCallbackEXT 函数原型添加一个叫 debugCallback 的静态成员方法。VKAPI\_ATTR 和 VKAPI\_CALL 可以确保它有正确的签名好让 Vulkan 调用。

```cpp
static VKAPI_ATTR VkBool32 VKAPI_CALL debugCallback(
    VkDebugUtilsMessageSeverityFlagBitsEXT messageSeverity,
    VkDebugUtilsMessageTypeFlagsEXT messageType,
    const VkDebugUtilsMessengerCallbackDataEXT* pCallbackData,
    void* pUserData) {

    std::cerr << "validation layer: " << pCallbackData->pMessage << std::endl;

    return VK_FALSE;
}
```

第一个参数指明了消息的等级，它的值有以下可能：

- VK\_DEBUG\_UTILS\_MESSAGE\_SEVERITY\_VERBOSE\_BIT\_EXT：诊断消息
- VK\_DEBUG\_UTILS\_MESSAGE\_SEVERITY\_INFO\_BIT\_EXT：通知消息，比如资源创建
- VK\_DEBUG\_UTILS\_MESSAGE\_SEVERITY\_WARNING\_BIT\_EXT：不一定是错误，但可能是程序 bug 的行为的消息
- VK\_DEBUG\_UTILS\_MESSAGE\_SEVERITY\_ERROR\_BIT\_EXT：无效并可能导致崩溃的行为的消息

这几个枚举的值递增，所以你可以用比较运算符来检测消息的等级，比如这样：

```cpp
if (messageSeverity >= VK_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT) {
    // Message is important enough to show
}
```

messageType 参数可能有以下值：

- VK\_DEBUG\_UTILS\_MESSAGE\_TYPE\_GENERAL\_BIT\_EXT：与规范和性能无关的事件
- VK\_DEBUG\_UTILS\_MESSAGE\_TYPE\_VALIDATION\_BIT\_EXT：与规范不符或可能出错的事件
- VK\_DEBUG\_UTILS\_MESSAGE\_TYPE\_PERFORMANCE\_BIT\_EXT：可能不是 Vulkan 的最佳使用

pCallbackData 参数引用一个 VkDebugUtilsMessengerCallbackDataEXT 结构体，它包含这个消息本身的细节，其中最重要的字段有：

- pMessage：以 null 结尾的字符串形式的消息
- pObjects：与消息有关的 Vulkan 对象句柄数组
- objectCount：上面数组的长度

最后，pUserData 参数包含一个在回调设置期间指定的指针，用来让你把自己的数据传递给它。

回调返回一个布尔值，可以指示是否要终止出发这条验证层消息的 Vulkan 调用。如果返回是，调用就会以 VK\_ERROR\_VALIDATION\_FAILED\_EXT 终止。这一般用来测试验证层自身，所以你应该总是返回 VK\_FALSE。

接下来要做的就是把回调函数传给 Vulkan 了。可能有点惊人的是，Vulkan 里即使是调试发信器也要用句柄管理并显示创建和销毁。这样的回调函数也是调试发信器的一部分，你想要多少就有多少。在 instance 下面为这个句柄添加一个成员字段：

```cpp
VkDebugUtilsMessengerEXT debugMessenger;
```

然后添加一个 setupDebugMessenger 方法并在 initVulkan 里 createInstance 之后调用。

```cpp
void initVulkan() {
    createInstance();
    setupDebugMessenger();
}

void setupDebugMessenger() {
    if (!enableValidationLayers) return;

}
```

把发信器和回调的相关细节填充到对应的 XXXCreateInfo 里：

```cpp
VkDebugUtilsMessengerCreateInfoEXT createInfo{};
createInfo.sType = VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT;
createInfo.messageSeverity = VK_DEBUG_UTILS_MESSAGE_SEVERITY_VERBOSE_BIT_EXT | VK_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT | VK_DEBUG_UTILS_MESSAGE_SEVERITY_ERROR_BIT_EXT;
createInfo.messageType = VK_DEBUG_UTILS_MESSAGE_TYPE_GENERAL_BIT_EXT | VK_DEBUG_UTILS_MESSAGE_TYPE_VALIDATION_BIT_EXT | VK_DEBUG_UTILS_MESSAGE_TYPE_PERFORMANCE_BIT_EXT;
createInfo.pfnUserCallback = debugCallback;
createInfo.pUserData = nullptr; // Optional
```

messageSeverity 字段允许你指定哪些等级的消息会触发你的回调。这里我指定了除了 INFO 的所有等级来接收可能的问题通知并忽略常规调试信息。

类似地，messageType 允许你指定触发回调的消息类型。这里我直接选了所有。你要是想的话也可以删掉你觉得没用的。

最后，pfnUserCallback 字段指定指向回调函数的指针。你可以选择把一个指针传给 pUserData 字段，它会再被传到回调函数的 pUserData 参数。比如你可以用这种方式传一个指向 HelloTriangleApplication 类的指针。

请注意，配置验证层消息和调试回调的方法有很多，本教程这种是入门版。其他方式请参阅扩展规范。

上面的结构体需要传给 vkCreateDebugUtilsMessengerEXT 来创建 VkDebugUtilsMessengerEXT 对象。坏消息是，这个函数是个扩展函数，它不会被自动加载。我们需要用 vkGetInstanceProcAddr 自己搜索这个函数的地址。下面我们创建一个代理函数来在后台处理这个逻辑。我把它加在了 HelloTriangleApplication 类定义的前面。

```cpp
VkResult CreateDebugUtilsMessengerEXT(VkInstance instance, const VkDebugUtilsMessengerCreateInfoEXT* pCreateInfo, const VkAllocationCallbacks* pAllocator, VkDebugUtilsMessengerEXT* pDebugMessenger) {
    auto func = (PFN_vkCreateDebugUtilsMessengerEXT) vkGetInstanceProcAddr(instance, "vkCreateDebugUtilsMessengerEXT");
    if (func != nullptr) {
        return func(instance, pCreateInfo, pAllocator, pDebugMessenger);
    } else {
        return VK_ERROR_EXTENSION_NOT_PRESENT;
    }
}
```

如果加载失败，vkGetInstanceProcAddr 会返回 nullptr。如果加载成功我们才创建扩展对象：

```cpp
if (CreateDebugUtilsMessengerEXT(instance, &createInfo, nullptr, &debugMessenger) != VK_SUCCESS) {
    throw std::runtime_error("failed to set up debug messenger!");
}
```

倒数第二个参数依然是可选的分配器回调，我们设成 nullptr，除此之外别的参数一看就懂。由于调试发信器特定于 Vulkan 实例和它的层，第一个参数这里我们需要指定实例。后面在子对象那里我们还会看到这种模式。

VkDebugUtilsMessengerEXT 对象也需要显式清理，清理函数是 vkDestroyDebugUtilsMessengerEXT。和创建函数（vkCreateDebugUtilsMessengerEXT）类似，它也需要手动加载。

在 CreateDebugUtilsMessengerEXT 下面再创建一个代理函数：

```cpp
void DestroyDebugUtilsMessengerEXT(VkInstance instance, VkDebugUtilsMessengerEXT debugMessenger, const VkAllocationCallbacks* pAllocator) {
    auto func = (PFN_vkDestroyDebugUtilsMessengerEXT) vkGetInstanceProcAddr(instance, "vkDestroyDebugUtilsMessengerEXT");
    if (func != nullptr) {
        func(instance, debugMessenger, pAllocator);
    }
}
```

确保这个函数是静态的或者是非成员，我们就可以在 cleanup 方法里调用它：

```cpp
void cleanup() {
    if (enableValidationLayers) {
        DestroyDebugUtilsMessengerEXT(instance, debugMessenger, nullptr);
    }

    vkDestroyInstance(instance, nullptr);

    glfwDestroyWindow(window);

    glfwTerminate();
}
```

**调试实例创建和销毁**

虽然我们已经用验证层添加了调试功能，我们还是没法调试所有部分。调用 vkCreateDebugUtilsMessengerEXT 需要已经创建好的实例，而 vkDestroyDebugUtilsMessengerEXT 需要在实例销毁前调用。这导致我们无法调试 vkCreateInstance 和 vkDestroyInstance 中出现的问题。

如果你仔细阅读了扩展文档，你会看到有一种方法可以为这两个函数调用创建一个专门的调试工具发信器。它只需要你向 VkInstanceCreateInfo 的 pNext 字段传指向一个 VkDebugUtilsMessengerCreateInfoEXT 结构体的指针。首先把创建发信器 CreateInfo 的代码提取出来放到一个方法里：

```cpp
void populateDebugMessengerCreateInfo(VkDebugUtilsMessengerCreateInfoEXT& createInfo) {
    createInfo = {};
    createInfo.sType = VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT;
    createInfo.messageSeverity = VK_DEBUG_UTILS_MESSAGE_SEVERITY_VERBOSE_BIT_EXT | VK_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT | VK_DEBUG_UTILS_MESSAGE_SEVERITY_ERROR_BIT_EXT;
    createInfo.messageType = VK_DEBUG_UTILS_MESSAGE_TYPE_GENERAL_BIT_EXT | VK_DEBUG_UTILS_MESSAGE_TYPE_VALIDATION_BIT_EXT | VK_DEBUG_UTILS_MESSAGE_TYPE_PERFORMANCE_BIT_EXT;
    createInfo.pfnUserCallback = debugCallback;
}

...

void setupDebugMessenger() {
    if (!enableValidationLayers) return;

    VkDebugUtilsMessengerCreateInfoEXT createInfo;
    populateDebugMessengerCreateInfo(createInfo);

    if (CreateDebugUtilsMessengerEXT(instance, &createInfo, nullptr, &debugMessenger) != VK_SUCCESS) {
        throw std::runtime_error("failed to set up debug messenger!");
    }
}
```

现在在 createInstance 复用这个方法：

```cpp
void createInstance() {
    ...

    VkInstanceCreateInfo createInfo{};
    createInfo.sType = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
    createInfo.pApplicationInfo = &appInfo;

    ...

    VkDebugUtilsMessengerCreateInfoEXT debugCreateInfo{};
    if (enableValidationLayers) {
        createInfo.enabledLayerCount = static_cast<uint32_t>(validationLayers.size());
        createInfo.ppEnabledLayerNames = validationLayers.data();

        populateDebugMessengerCreateInfo(debugCreateInfo);
        createInfo.pNext = (VkDebugUtilsMessengerCreateInfoEXT*) &debugCreateInfo;
    } else {
        createInfo.enabledLayerCount = 0;

        createInfo.pNext = nullptr;
    }

    if (vkCreateInstance(&createInfo, nullptr, &instance) != VK_SUCCESS) {
        throw std::runtime_error("failed to create instance!");
    }
}
```

debugCreateInfo 变量在 if 语句外面创建，这样可以保证 vkCreateInstance 调用时它不会被销毁。这样创建一个额外的发信器后，vkCreateInstance 和 vkDestroyInstance 调用期间它会被自动调用并在结束后被清理。

**测试**

现在我们故意犯一个错误来看验证层会作何反应。暂时将 cleanup 中的 DestroyDebugUtilsMessengerEXT 调用删除并运行。退出时你会看到：

（图片在原文）

> 如果你没看到这些提示，检查下你安装的对不对

如果你想看下哪个调用出发了这条消息，你可以在回调函数里打个断点然后看栈踪。

**配置**

除了 VkDebugUtilsMessengerCreateInfoEXT 里的标志枚举，验证层还有很多其他可设置的行为。浏览 Vulkan SDK，打开 Config 文件夹。你可以找到 vk\_layer\_settings.txt 文件，它会告诉你怎么配置。

要为你自己的应用程序配置这些层设置，把文件复制到你工程里的 Debug 和 Release 文件夹，跟着文件里的指导来设置想要的行为。不过在本教程接下来的内容里我会假设你用的是默认配置。

本教程中，我会故意犯一些错误来向你展示验证层的在捕获错误上的帮助，并教你“明白自己在做什么”在 Vulkan 中有多重要。现在是时候看看 Vulkan 设备了。（下一章）

（完整代码看原文）