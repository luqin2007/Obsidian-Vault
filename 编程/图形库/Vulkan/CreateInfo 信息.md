不同 `Info` 类有一些公共的字段

| 成员      | 类型                | 说明                                  |
| ------- | ----------------- | ----------------------------------- |
| `sType` | `VkStructureType` | 结构体类型，不同结构体值不同，**仅 C API，类中不需要**    |
| `pNext` | `void*`           | 如有必要，指向一个用于扩展该结构体的结构体，通常为 `nullptr` |
| `flags` | 各类 `Flags`        | 默认为 0                               |

当信息类中包含 `count` 和对应数组时，类还包含一个可以直接传入底层以数组存储的容器的方法，如 `std::vector`，`std::array` 等
# InstanceCreateInfo

| 成员                        | 类型                   | 说明                                       |
| ------------------------- | -------------------- | ---------------------------------------- |
| `sType`                   |                      | `VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO` |
| `pApplicationInfo`        | `ApplicationInfo*`   | 指向描述本程序相关信息的结构体                          |
| `enabledLayerCount`       | `uint32_t`           | 所需额外开启的实例级别层数                            |
| `pEnabledLayerNames`      | `const char* const*` | 指向由所需开启的层的名称构成的数组（同一名称可以重复出现）            |
| `enabledExtensionCount`   | `uint32_t`           | 所需额外开启的实例级别扩展数                           |
| `ppEnabledExtensionNames` | `const char* const*` | 指向由所需开启的扩展的名称构成的数组（同一名称可以重复出现）           |
## ApplicationInfo

| 成员                   | 类型            | 说明                                   |
| -------------------- | ------------- | ------------------------------------ |
| `sType`              |               | `VK_STRUCTURE_TYPE_APPLICATION_INFO` |
| `pApplicationName`   | `const void*` | 应用程序的名称                              |
| `applicationVersion` | `uint32_t`    | 应用程序的版本号                             |
| `pEngineName`        | `const void*` | 引擎的名称                                |
| `engineVersion`      | `uint32_t`    | 引擎的版本号                               |
| `apiVersion`         | `uint32_t`    | VulkanAPI 的版本号，必填                    |
# DebugUtilsMessengerCreateInfoEXT

| 成员                | 类型                                     | 说明                                                              |
| ----------------- | -------------------------------------- | --------------------------------------------------------------- |
| `messageSeverity` | `DebugUtilsMessageSeverityFlagsEXT`    | 获取 debug 信息的严重性，由 `VkDebugUtilsMessageSeverityFlagBitsEXT` 组合而成 |
| `messageType`     | `DebugUtilsMessageTypeFlagsEXT`        | 获取 debug 信息类型，由 `VkDebugUtilsMessageTypeFlagBitsEXT` 组合而成       |
| `pfnUserCallback` | `PFN_vkDebugUtilsMessengerCallbackEXT` | debug 信息的自定义回调函数，返回值必须是 VK_FALSE                                |
| `pUserData`       | `void*`                                | 提供一个地址，之后如有需要，可以让自定义回调函数将数据存入该地址                                |
# DeviceCreateInfo

| 成员                        | 类型                               | 说明                                           |
| ------------------------- | -------------------------------- | -------------------------------------------- |
| `queueCreateInfoCount`    | `uint32_t`                       | 队列的创建信息的个数                                   |
| `pQueueCreateInfos`       | `const VkDeviceQueueCreateInfo*` | 指向由队列的创建信息构成的数组                              |
| `enabledExtensionCount`   | `uint32_t`                       | 所需额外开启的设备级别扩展数                               |
| `ppEnabledExtensionNames` | `const char* const*`             | 指向由所需开启的扩展的名称构成的数组（同一名称可以重复出现）               |
| `pEnabledFeatures`        | `VkPhysicalDeviceFeatures*`      | 指向一个 VkPhysicalDeviceFeatures 结构体，指明需要开启哪些特性 |
# DeviceQueueCreateInfo

| 成员                 | 类型             | 说明                                         |
| ------------------ | -------------- | ------------------------------------------ |
| `queueFamilyIndex` | `uint32_t`     | 队列族索引                                      |
| `queueCount`       | `uint32_t`     | 在该队列族索引下，所须创建的队列个数                         |
| `pQueuePriorities` | `const float*` | 指向浮点数的数组，用于指定各个队列的优先级，浮点数范围在 0 到 1 之间，1 最大 |
# SwapchainCreateInfoKHR

| 成员                      | 类型                            | 说明                                                                             |
| ----------------------- | ----------------------------- | ------------------------------------------------------------------------------ |
| `surface`               | `SurfaceKHR`                  | Window surface                                                                 |
| `minImageCount`         | `uint32_t`                    | 交换链中图像的最少数量                                                                    |
| `imageFormat`           | `Format`                      | 交换链中图像的格式<br>- Rn、Gn、Bn、An：各通道位数<br>- U：底层是无符号整型<br>- NORM：自动标准化（转换为 `[0, 1]`） |
| `imageColorSpace`       | `ColorSpaceKHR`               | 交换链中图像的色彩空间                                                                    |
| `imageExtent`           | `Extent2D`                    | 交换链图像的尺寸                                                                       |
| `imageArrayLayers`      | `uint32_t`                    | 视点数，用于多视点（multiview）或立体显示设备，普通显示器为 1                                           |
| `imageUsage`            | `ImageUsageFlags`             | 交换链中图像的用途                                                                      |
| `imageSharingMode`      | `SharingMode`                 | 交换链中图像的分享模式                                                                    |
| `queueFamilyIndexCount` | `uint32_t`                    | 将会访问交换链图像的队列族总数                                                                |
| `pQueueFamilyIndices`   | `const uint32_t*`             | 将会访问交换链图像的队列族索引                                                                |
| `preTransform`          | `SurfaceTransformFlagBitsKHR` | 对交换链图像的变换，比如旋转 90°、镜像等                                                          |
| `compositeAlpha`        | `CompositeAlphaFlagBitsKHR`   | 指定如何处理交换链图像的透明度                                                                |
| `presentMode`           | `PresentModeKHR`              | 呈现方式                                                                           |
| `clipped`               | `Bool32`                      | 是否允许舍弃掉交换链图像应有但窗口中不会显示的像素                                                      |
| `oldSwapchain`          | `SwapchainKHR`                | 旧的交换链，在重建交换链时填入                                                                |
# ImageViewCreateInfo

| 成员                 | 类型                      | 说明                |
| ------------------ | ----------------------- | ----------------- |
| `image`            | `Image`                 | 图像的 handle        |
| `viewType`         | `ImageViewType`         | 图像视图的类型           |
| `format`           | `Format`                | 图像视图的格式，可以与图像格式不同 |
| `components`       | `ComponentMapping`      | 指定各通道的映射关系        |
| `subresourceRange` | `ImageSubresourceRange` | 子资源范围             |
## ImageViewType

| 值            | `VkImageViewType` 值             | 说明      |
| ------------ | ------------------------------- | ------- |
| `e1D`        | `VK_IMAGE_VIEW_TYPE_1D`         | 1D 图像   |
| `e2D`        | `VK_IMAGE_VIEW_TYPE_2D`         | 2D 图像   |
| `e3D`        | `VK_IMAGE_VIEW_TYPE_3D`         | 3D 图像   |
| `eCube`      | `VK_IMAGE_VIEW_TYPE_CUBE`       | 立方体图像   |
| `e1DArray`   | `VK_IMAGE_VIEW_TYPE_1D_ARRAY`   | 1D 图像数组 |
| `e2DArray`   | `VK_IMAGE_VIEW_TYPE_2D_ARRAY`   | 2D 图像数组 |
| `eCubeArray` | `VK_IMAGE_VIEW_TYPE_CUBE_ARRAY` | 立方体图像数组 |
## ComponentMapping

| 值           | `VkComponentSwizzle` 值          | 说明            |
| ----------- | ------------------------------- | ------------- |
| `eIdentity` | `VK_COMPONENT_SWIZZLE_IDENTITY` | 不改变映射关系       |
| `eZero`     | `VK_COMPONENT_SWIZZLE_ZERO`     | 使用该通道的数值一概归 0 |
| `eOne`      | `VK_COMPONENT_SWIZZLE_ONE`      | 使用该通道的数值一概归 1 |
| `eR`        | `VK_COMPONENT_SWIZZLE_R`        | 将 R 通道映射到该通道  |
| `eG`        | `VK_COMPONENT_SWIZZLE_G`        | 将 G 通道映射到该通道  |
| `eB`        | `VK_COMPONENT_SWIZZLE_B`        | 将 B 通道映射到该通道  |
| `eA`        | `VK_COMPONENT_SWIZZLE_A`        | 将 A 通道映射到该通道  |
## ImageSubresourceRange

| 成员               | 类型                 | 说明        |
| ---------------- | ------------------ | --------- |
| `aspectMask`     | `ImageAspectFlags` | 所使用图像的类型  |
| `baseMipLevel`   | `uint32_t`         | 初始 mip 等级 |
| `levelCount`     | `uint32_t`         | mip 等级总数  |
| `baseArrayLayer` | `uint32_t`         | 初始图层      |
| `layerCount`     | `uint32_t`         | 图层总数      |
### ImageAspectFlags

| 版本  | 值                              | 说明                           |
| --- | ------------------------------ | ---------------------------- |
| 1.3 | `VK_IMAGE_ASPECT_NONE`         | 不使用任何层面                      |
| 1.0 | `VK_IMAGE_ASPECT_COLOR_BIT`    | 颜色层面                         |
| 1.0 | `VK_IMAGE_ASPECT_DEPTH_BIT`    | 深度层面                         |
| 1.0 | `VK_IMAGE_ASPECT_STENCIL_BIT`  | 模板层面                         |
| 1.0 | `VK_IMAGE_ASPECT_METADATA_BIT` | 元数据层面（稀疏绑定）                  |
| 1.1 | `VK_IMAGE_ASPECT_PLANE_0_BIT`  | 多层面（multi-planar）图像格式的 0 号平面 |
| 1.1 | `VK_IMAGE_ASPECT_PLANE_1_BIT`  | 多层面（multi-planar）图像格式的 1 号平面 |
| 1.1 | `VK_IMAGE_ASPECT_PLANE_2_BIT`  | 多层面（multi-planar）图像格式的 2 号平面 |
# GraphicsPipelineCreateInfo

| 参数                    | 类型                                       | 说明                                                |
| --------------------- | ---------------------------------------- | ------------------------------------------------- |
| `sType`               |                                          | `VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO` |
| `stageCount`          | `uint32_t`                               | 可编程管线阶段的数量                                        |
| `pStages`             | `PipelineShaderStageCreateInfo* `        | 提供可编程管线阶段的创建信息（着色器）                               |
| `pVertexInputState`   | `PipelineVertexInputStateCreateInfo* `   | 顶点输入状态的创建信息                                       |
| `pInputAssemblyState` | `PipelineInputAssemblyStateCreateInfo* ` | 输入装配状态的创建信息                                       |
| `pTessellationState`  | `PipelineTessellationStateCreateInfo*`   | 细分状态的创建信息                                         |
| `pViewportState`      | `PipelineViewportStateCreateInfo*`       | 视口状态的创建信息                                         |
| `pRasterizationState` | `PipelineRasterizationStateCreateInfo*`  | 栅格化状态的创建信息                                        |
| `pMultisampleState`   | `PipelineMultisampleStateCreateInfo*`    | 多重采样状态的创建信息                                       |
| `pDepthStencilState`  | `PipelineDepthStencilStateCreateInfo*`   | 深度模板状态的创建信息                                       |
| `pColorBlendState`    | `PipelineColorBlendStateCreateInfo*`     | 混色状态的创建信息                                         |
| `pDynamicState`       | `PipelineDynamicStateCreateInfo*`        | 动态状态的创建信息                                         |
| `pipelineLayout`      | `PipelineLayout`                         | 管线布局                                              |
| `renderPass`          | `RenderPass`                             | 渲染通道                                              |
| `subpass`             | `uint32_t`                               | 指明使用了该管线的子通道在renderPass所示渲染通道中的索引                 |
| `basePipelineHandle`  | `Pipeline`                               | 如果该管线从一个已经创建了的管线衍生而来，提供已创建管线的handle               |
| `basePipelineIndex`   | `int32_t`                                | 如果由另一个管线衍生而来，提供另一管线的索引，否则为 -1                     |
## PipelineCreateFlagBits

| 支持版本 | 值                                | 说明                                                                               |
| ---- | -------------------------------- | -------------------------------------------------------------------------------- |
| 1.0  | `eDisableOptimization`           | 禁止优化                                                                             |
| 1.0  | `eAllowDerivatives`              | 允许从该管线衍生出其他管线                                                                    |
| 1.0  | `eDerivative`                    | 该管线是从其他管线衍生而来                                                                    |
| 1.1  | `eViewIndexFromDeviceIndex`      | 着色器中所有的 `gl_ViewIndex` 输入都采取与 `gl_DeviceIndex` 输入一致的数值                           |
| 1.1  | `eDispatchBase`                  | 绑定计算管线后可以使用 `vkCmdDispatchBase` 指令，仅用于计算管线                                       |
| 1.3  | `eFailOnPipelineCompileRequired` | 使用了着色器模组标识符，但无法根据标识符找到管线缓存，管线创建将会失败，并返回 `VK_PIPELINE_COMPILE_REQUIRED`           |
| 1.3  | `eEarlyReturnOnFailure`          | 使用 `createGraphicsPipelines` 或 `createComputePipelines` 时，当前管线创建失败，则后续其他管线亦不会被创建 |
## PipelineVertexInputStateCreateInfo

| 成员                                | 类型                                   | 说明                                                          |
| --------------------------------- | ------------------------------------ | ----------------------------------------------------------- |
| `sType`                           |                                      | `VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO` |
| `vertexBindingDescriptionCount`   | `uint32_t`                           | 顶点输入绑定描述的数量                                                 |
| `pVertexBindingDescriptions`      | `VertexInputBindingDescription*`     | 指定顶点输入绑定                                                    |
| `vertexAttributeDescriptionCount` | `uint32_t`                           | 顶点属性描述的数量                                                   |
| `pVertexAttributeDescriptions`    | `VkVertexInputAttributeDescription*` | 指定顶点属性                                                      |
## PipelineInputAssemblyStateCreateInfo

| 成员                       | 类型                  | 说明                                                            |
| ------------------------ | ------------------- | ------------------------------------------------------------- |
| `sType`                  |                     | `VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO` |
| `topology`               | `PrimitiveTopology` | 输入的图元拓扑类型                                                     |
| `primitiveRestartEnable` | `VkBool32`          | 是否允许重启图元                                                      |
### PrimitiveTopology

| 值                | 图元类型 | 说明                                             |
| ---------------- | ---- | ---------------------------------------------- |
| `ePointList`     | 点    | 每个顶点表示一个点                                      |
| `eLineList`      | 线    | 每两个点组成一个线段                                     |
| `eLineStrip`     | 线    | 每个顶点都与上一个点组成线段                                 |
| `eTriangleList`  | 三角形  | 每三个点组成一个三角形，如 `1-2-3`，`4-5-6`                  |
| `eTriangleStrip` | 三角形  | 每个点与前两个点组成一个三角形，如 `1-2-3`，`2-3-4`，`3-4-5`      |
| `eTriangleFan`   | 三角形  | 每个点与前一个点、第一个点组成一个三角形，如 `1-2-3`，`1-3-4`，`1-4-5` |
## PipelineShaderStageCreateInfo

| 值                     | 类型                    | 说明                                                    |
| --------------------- | --------------------- | ----------------------------------------------------- |
| `sType`               |                       | `VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO` |
| `stage`               | `ShaderStageFlagBits` | 着色器对应的可编程管线阶段                                         |
| `module`              | `ShaderModule`        | 着色器模组                                                 |
| `pName`               | `const char*`         | 入口函数名，通常为 `main`                                      |
| `pSpecializationInfo` | `SpecializationInfo*` | 常量的特化信息，不需要则为 `nullptr`                               |
## PipelineViewportStateCreateInfo

| 成员              | 类型            | 说明                                                      |
| --------------- | ------------- | ------------------------------------------------------- |
| `sType`         |               | `VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO` |
| `viewportCount` | `uint32_t`    | 指定视口的个数，通常为 1                                           |
| `pViewports`    | `VkViewport*` | 指定视口                                                    |
| `scissorCount`  | `uint32_t`    | 指定剪裁范围的个数                                               |
| `pScissors`     | `VkRect2D*`   | 指定剪裁范围                                                  |
## PipelineRasterizationStateCreateInfo
| 成员                        | 类型              | 说明                                                           |
| ------------------------- | --------------- | ------------------------------------------------------------ |
| `sType`                   |                 | `VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO` |
| `depthClampEnable`        | `Bool32`        | 是否钳制深度                                                       |
| `rasterizerDiscardEnable` | `Bool32`        | 是否在栅格化阶段前丢弃图元                                                |
| `polygonMode`             | `PolygonMode`   | 几何图形绘制模式                                                     |
| `cullMode`                | `CullModeFlags` | 面剔除模式                                                        |
| `frontFace`               | `FrontFace`     | 正面顶点顺序是顺时针还是逆时针                                              |
| `depthBiasEnable`         | `Bool32`        | 是否开启深度偏移，主要是为了解决实现阴影时的Shadow Acne问题                          |
| `depthBiasConstantFactor` | `float`         | 深度偏移常量系数                                                     |
| `depthBiasClamp`          | `float`         | 深度偏移钳制                                                       |
| `depthBiasSlopeFactor`    | `float`         | 深度偏移坡度系数                                                     |
| `lineWidth`               | `float`         | 指定绘制线段时的线宽，没有开启 `wideLines` 设备特性的话必须为 1                      |
## PipelineColorBlendStateCreateInfo

| 成员                | 类型                                   | 值                                                          |
| ----------------- | ------------------------------------ | ---------------------------------------------------------- |
| `sType`           |                                      | `VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO` |
| `logicOpEnable`   | `Bool32`                             | 是否开启逻辑运算                                                   |
| `logicOp`         | `LogicOp`                            | 指定进行何种逻辑运算                                                 |
| `attachmentCount` | `uint32_t`                           | 混色方式的数量                                                    |
| `pAttachments`    | `PipelineColorBlendAttachmentState*` | 为每个颜色附件指定混色方式                                              |
| `blendConstants`  | `float[4]`                           | 常量混色因子，按RGBA顺序指定                                           |
### PipelineColorBlendAttachmentState

| 参数                    | 类型                    | 说明                                                     |
| --------------------- | --------------------- | ------------------------------------------------------ |
| `blendEnable`         | `Bool32`              | 是否开启混色                                                 |
| `srcColorBlendFactor` | `BlendFactor`         | 对新生成片元的RGB通道采用的混色因子                                    |
| `dstColorBlendFactor` | `BlendFactor`         | 对颜色附件中已有的RGB通道采用的混色因子                                  |
| `colorBlendOp`        | `BlendOp`             | 对RGB通道的混色选项（运算方式）                                      |
| `srcAlphaBlendFactor` | `BlendFactor`         | 对新生成片元的Alpha通道采用的混色因子                                  |
| `dstAlphaBlendFactor` | `BlendFactor`         | 对颜色附件中已有的Alpha通道采用的混色因子                                |
| `alphaBlendOp`        | `BlendOp`             | 对A通道的混色选项（运算方式）                                        |
| `colorWriteMask`      | `ColorComponentFlags` | 是否向RGBA各通道写入数值的位遮罩，RGBA分别对应0b0001、0b0010、0b0100、0b1000 |
## RenderPassCreateInfo

| 成员                  | 类型                       | 说明                                          |
| ------------------- | ------------------------ | ------------------------------------------- |
| `sType`             |                          | `VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO` |
| `attachmentCount`   | `uint32_t`               | 图像附件的数量                                     |
| `pAttachments`      | `AttachmentDescription*` | 图像附件                                        |
| `subpassCount`      | `uint32_t`               | 子通道的数量                                      |
| `pSubpasses`        | `SubpassDescription*`    | 子通道                                         |
| `dependenciesCount` | `uint32_t`               | 子通道依赖的数量                                    |
| `pDependencies`     | `SubpassDependency*`     | 子通道依赖                                       |
### AttachmentDescription

| 成员               | 类型                    | 说明                                  |
| ---------------- | --------------------- | ----------------------------------- |
| `format`         | `Format`              | 图像附件的格式，与要被用作图像附件的 image view 的格式一致 |
| `samples`        | `SampleCountFlagBits` | 每个像素的采样点数量                          |
| `loadOp`         | `AttachmentLoadOp`    | 读取图像附件时，对颜色和深度值进行的操作                |
| `storeOp`        | `AttachmentStoreOp`   | 存储颜色和深度值到图像附件时的操作，或指定不在乎存储          |
| `stencilLoadOp`  | `AttachmentLoadOp`    | 读取图像附件时，对模板值进行的操作                   |
| `stencilStoreOp` | `AttachmentStoreOp`   | 存储模板值到图像附件时的操作，或指定不在乎存储             |
| `initialLayout`  | `ImageLayout`         | 读取图像附件时的内存布局                        |
| `finalLayout`    | `ImageLayout`         | 存储渲染结果到图像附件时，需转换至的内存布局              |
### SubpassDescription

| 成员                        | 类型                     | 说明                                                |
| ------------------------- | ---------------------- | ------------------------------------------------- |
| `pipelineBindPoint`       | `PipelineBindPoint`    | 管线类型                                              |
| `inputAttachmentCount`    | `uint32_t`             | 输入附件的数量                                           |
| `pInputAttachments`       | `AttachmentReference*` | 输入附件数组                                            |
| `colorAttachmentCount`    | `uint32_t`             | 颜色附件的数量                                           |
| `pColorAttachments`       | `AttachmentReference*` | 颜色附件数组                                            |
| `pResolveAttachments`     | `AttachmentReference*` | 解析附件数组，与颜色附件一一对应                                  |
| `pDepthStencilAttachment` | `AttachmentReference*` | 深度模板附件（一个）                                        |
| `preserveAttachmentCount` | `uint32_t`             | 保留附件的数量                                           |
| `pPreserveAttachments`    | `uint32_t*`            | 保留附件所对应 `VkRenderPassCreateInfo::pAttachments` 索引 |

### SubpassDependency

| 成员                | 类型                   | 说明                            |
| ----------------- | -------------------- | ----------------------------- |
| `srcSubpass`      | `uint32_t`           | 源子通道                          |
| `dstSubpass`      | `uint32_t`           | 目标子通道                         |
| `srcStageMask`    | `PipelineStageFlags` | 源管线阶段                         |
| `dstStageMask`    | `PipelineStageFlags` | 目标管线阶段                        |
| `srcAccessMask`   | `AccessFlags`        | 源操作                           |
| `dstAccessMask`   | `AccessFlags`        | 目标操作                          |
| `dependencyFlags` | `DependencyFlags`    | `VK_DEPENDENCY_BY_REGION_BIT` |
#### DependencyFlags

| Vulkan 版本 | 值                                | 说明                      |
| --------- | -------------------------------- | ----------------------- |
| 1.0       | `VK_DEPENDENCY_BY_REGION_BIT`    | 依赖是 framebuffer-local 的 |
| 1.1       | `VK_DEPENDENCY_DEVICE_GROUP_BIT` | 依赖涉及到多个物理设备，通常指涉及多张显卡渲染 |
| 1.1       | `VK_DEPENDENCY_VIEW_LOCAL_BIT`   | 依赖是 view-local 的        |
# CommandBufferBeginInfo

| 成员                 | 类型                              | 说明                                            |
| ------------------ | ------------------------------- | --------------------------------------------- |
| `sType`            |                                 | `VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO` |
| `pInheritanceInfo` | `CommandBufferInheritanceInfo*` | 指向二级命令缓冲区的继承信息                                |
## CommandBufferUsageFlagBits

| 值                     | 说明                                                                    |
| --------------------- | --------------------------------------------------------------------- |
| `eOneTimeSubmit`      | 该命令缓冲区只会被提交一次，然后就会被被重置（重新录制当然也算）/释放                                   |
| `eRenderPassContinue` | 仅用于二级命令缓冲区，表示所录制命令被完全包含在某个渲染通道内<br>若使用该bit，继承信息中提供的 `renderPass` 必须有效 |
| `eSimultaneousUse`    | 该命令缓冲区可以在待决状态下（即还没执行完时）被重新提交                                          |

# 未归纳

| 成员                   | 类型              | 说明                                   |
| -------------------- | --------------- | ------------------------------------ |
| `sType`              |                 | `VK_STRUCTURE_TYPE_PRESENT_INFO_KHR` |
| `waitSemaphoreCount` | `uint32_t`      | 所需等待被置位的信号量的个数                       |
| `pWaitSemaphores`    | `Semaphore*`    | 指向所需等待被置位的信号量的数组                     |
| `swapchainCount`     | `uint32_t`      | 有图像需要被呈现的交换链的个数                      |
| `pSwapchains`        | `SwapchainKHR*` | 指向有图像需要被呈现的交换链的数组                    |
| `pImageIndices`      | `uint32_t*`     | 指向各个交换链中需要被呈现的图像的索引构成的数组             |
| `pResults`           | `VkResult*`     | 各个交换链中各图像的呈现结果，可以是 `nullptr`         |
