# 构建及导入 VS2022

在 wxWidgets [下载源码](https://www.wxwidgets.org/downloads/)

![[image-20240621175253-3yu3zap.png]]

> [!note] 也可以下载 Binaries 下对应版本的二进制版本，但要放好位置

解压，设解压目录为 `{wxWidgets}` 使用 VS 2022 打开 `{wxWidgets}/build/msw/wx_vc17.sln`。如果是其他编译器则打开 `build` 目录下对应项目即可。

打开后，点击生成 - 批生成，全选后生成即可。大概十几分钟，生成完整后 15G 左右（15.1G）

![[image-20240621175600-h33xj7d.png]]

![[image-20240621175643-08oi65u.png]]

生成后约有 12G 的文件，在 `build/msw` 下几个文件夹中，可以删除，不影响后续使用；还有 2G+ 位于 `lib` 目录下，是主要生成的文件

在 VS2022 中，添加头文件和库文件
* 头文件：`C/C++` - `常规` - `附加包含目录`
    * `{wxWidgets}\include`
    * `{wxWidgets}\include\msvc`
* 库文件：`链接器` - `常规` - `附加库目录`
    * `{wxWidgets}\lib\vc_x64_lib`

测试程序可以在 [wxWidgets: Hello World Example](https://docs.wxwidgets.org/latest/overview_helloworld.html) 底部查看
# 问题

> [!error] 无法解析的外部符号 main，函数 "int __cdecl invoke_main(void)" 中引用了该符号

![[image-20240621174535-m7z4bdw.png]]

创建的是控制台应用，需要修改成 Windows 窗口应用

![[image-20240621174632-l29ro69.png]]

在项目属性 - 链接器 - 系统 - 子系统中选择 `窗口(/SUBSYSTEM:WINDOWS)` 即可
# 参考

```cardlink
url: https://www.wxwidgets.org/
title: "wxWidgets: Cross-Platform GUI Library"
host: www.wxwidgets.org
```

```cardlink
url: https://docs.wxwidgets.org/3.2/index.html
title: "wxWidgets: Documentation"
host: docs.wxwidgets.org
```
