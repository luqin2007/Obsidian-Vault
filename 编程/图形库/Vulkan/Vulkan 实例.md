# 版本选择

使用 `vkEnumerateInstanceVersion` 可用于查询 Vulkan SDK 支持的最新 Vulkan 版本，但该方法仅在 1.0 以上的版本存在

```cpp
// 不存在 vkEnumerateInstanceVersion 函数，说明为 1.0
uint32_t apiVersion = VK_VERSION_1_0;

if (vkGetInstanceProcAddr(VK_NULL_HANDLE, "vkEnumerateInstanceVersion")) {
    // 返回值表示是否成功获取
    return vkEnumerateInstanceVersion(&apiVersion);
}

// 输出 Vulkan 版本
std::cout << std::format("Vulkan API Version: {}.{}.{}\n",
                         VK_VERSION_MAJOR(apiVersion),
                         VK_VERSION_MINOR(apiVersion),
                         VK_VERSION_PATCH(apiVersion));

return VK_SUCCESS;
```
# 层与扩展

声明 Vulkan 实例需要确认层与扩展，提供对特定需求、特定平台的支持，还包括特定厂商提供的功能。
- 层：有显著的作用范围，如验证层等。只有实例级层，没有设备（独占）级层
	- 使用 `enumerateInstanceLayerProperties` 可以遍历所有可用层
- 扩展：分为实例级和设备级扩展。某些扩展也需要特定层
	- 使用 `enumerateInstanceExtensionProperties` 可以遍历所有可用扩展，额外提供一个 `layerName` 参数表示扩展用于哪个层

当创建 Vulkan 实例时，若存在无法满足的层和扩展，会产生失败结果
- 不满足层：`VK_ERROR_LAYER_NOT_PRESENT`
- 不满足扩展：`VK_ERROR_EXTENSION_NOT_PRESENT`

> [!note] 设备层：有些层只能用于设备，所以需要在创建逻辑设备时添加
> - 使用物理设备对象的 `enumerateDeviceExtensionProperties` 方法可查看所有支持的层
## 验证层

> [!attention] 调试时最好开启验证层 `VK_LAYER_KHRONOS_validation`
> 只要添加了该层就有效果，会提示错误的 Vulkan API 调用

> [!warning] Valkan 没有内置验证层，只有安装了 Vulkan SDK 的设备才可用验证层
> `#NDEBUG` 宏可用于检查非调试环境

> [!tip] 验证层
> Vulkan SDK 引入验证层系统用于检验 Vulkan 参数、对象、线程、调用等信息，并将参数记录输出。参考 [[../../../_resources/documents/【Vulkan 学习笔记】4.1.3 Validation layers - 哔哩哔哩]]

获取验证层捕捉的 debug 信息，用于调试，使用 `Vk` 创建
# 创建实例

创建实例所需的信息对象为 [[CreateInfo 信息#InstanceCreateInfo|InstanceCreateInfo]]，其中 [[CreateInfo 信息#ApplicationInfo|ApplicationInfo]] 的 `apiVersion` 字段填写使用的 VulkanAPI 版本

```cpp
    void Context::createInstance() {
        ApplicationInfo applicationInfo;
        applicationInfo.setPApplicationName("Vulkan Demo")
                .setApiVersion(getLatestVersion());
        std::vector<const char*> layers = {
#ifndef NDEBUG
                "VK_LAYER_KHRONOS_validation",
#endif
        };
        // SurfaceKHR, 不渲染不需要
        uint32_t extensionCount;
        auto extensions = glfwGetRequiredInstanceExtensions(&extensionCount);
        InstanceCreateInfo instanceCreateInfo;
        instanceCreateInfo.setPApplicationInfo(&applicationInfo)
                .setPEnabledLayerNames(layers)
                .setEnabledExtensionCount(extensionCount)
                .setPpEnabledExtensionNames(extensions);
        instance = vk::createInstance(instanceCreateInfo);
    }
```
# Window Surface

VkSurfaceKHR，用于 VulkanAPI 与平台功能对接
- GLFW：使用 `glfwCreateWindowSurface` 创建
- 纯 VulkanSDK：使用 `vkCreateWin32SurfaceKHR` 创建

> [!note] 若应用有多个窗口，则对应有多个 Window Surface 和多个交换链

```cpp title:VulkanInstance::setSurfaceFromGLFW
auto result = glfwCreateWindowSurface(handle, window, nullptr, &surface);
// if (result) { cout; return result; }
returnVkError("Failed to allocate a vulkan surface!");
return VK_SUCCESS;
```

> [!failure] `VK_RESULT_MAX_ENUM` 错误代码
> `VK_RESULT_MAX_ENUM` 本身不是错误代码。但 VkResult 中没有找不到函数时的枚举值，且 `VK_RESULT_MAX_ENUM` 不是任何已用的错误代码，因此使用该值。
