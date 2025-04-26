局域网内共享键盘鼠标、剪贴板等资源的工具，开源但收费，免费使用可以自己编译。

[symless/synergy-core: Open source core of Synergy, the cross-platform keyboard and mouse sharing tool (Windows, macOS, Linux) (github.com)](https://github.com/symless/synergy-core)

（1.14.6有已经编译好的：[Synergy编译、去激活、汉化：macOS+Windows保姆级教程 - 哔哩哔哩 (bilibili.com)](https://www.bilibili.com/read/cv19053031/)）
# 准备

* git，用于下载源码
* Visual Studio 2019+
* Windows 10 SDK，可以通过 Visual Studio Installer 安装
    * SDK 下载地址：[Windows SDK - Windows 应用开发 | Microsoft Developer](https://developer.microsoft.com/zh-cn/windows/downloads/windows-sdk/)
* Bonjour SDK for Windows
    * 下载地址：[Bonjour SDK for Windows](https://binaries.symless.com/bonjour/bonjoursdksetup.exe)
* Qt
    * 在线安装包下载地址：[Download Qt OSS: Get Qt Online Installer](https://www.qt.io/download-qt-installer-oss)
    * 离线安装包下载地址：[Download Source Package Offline Installers | Qt](https://www.qt.io/offline-installers)
    * Synergy 使用 QT5 编写，最新版本 5.12.12，可以使用离线安装包加快下载速度
    * 安装完成后，将 `\msvc2019_64\bin`  添加到 path 环境变量
* OpenSSL，下载 3.1.x 非 Lite 版本
    * 下载地址：[适用于 Windows 的 Win32/Win64 OpenSSL 安装程序 - Shining Light Productions (slproweb.com)](https://slproweb.com/products/Win32OpenSSL.html)
* CMake，CLion 自带有，不过本身也不大下载一个最新的，记得加入 path 环境变量
    * 下载地址：[Download CMake](https://cmake.org/download/)
# 编译

* 下载源代码

```shell
git clone git@github.com:symless/synergy-core.git
```

* 使用 QT Creator 打开项目，取消所有 Desktop Qt 开头的调试选项
  ![[Pasted image 20240806172538.png]]

* 右键项目，选择执行 CMake
  ![[Pasted image 20240806172556.png]]
* 切换到 Release 版本，构建；若不存在 Release，在项目 - 构建设置那里创建一个
  ![[Pasted image 20240806172611.png]]

  ![[Pasted image 20240806172617.png]]

  ![[Pasted image 20240806172624.png]]

# 问题

> [!error] 找不到 cl

![[Pasted image 20240806172637.png]]

* 检查 VS 编译器是否正确识别：在工具 - 选项 - Kits - 编译器检查是否有 Microsoft Visual C++ Compiler

  ![[Pasted image 20240806172644.png]]
* 在 Kits 标签页下，找到默认 Kits 配置，两个都选择 Visual C++ x86_amd64 那个

  ![[Pasted image 20240806172650.png]]

  ![[Pasted image 20240806172656.png]]
* 在 Visual Studio Installer 中安装 VS 2019 的 C++ 编译器

  ![[Pasted image 20240806172702.png]]

> [!error] is not able to compile a simple test program

交叉编译相关，双击进入错误位置，把这部分 if 和 endif 注掉即可，不需要交叉编译

![[Pasted image 20240806172708.png]]

> [!error] Could NOT find OpenSSL

![[Pasted image 20240806172714.png]]

* 确认安装的 OpenSSL 不是 Lite 版本
* 确认安装的 OpenSSL 版本为 3.1.x，**3.2 不可用**
* 在项目 - Build Environment 添加环境变量 `OPENSSL_ROOT_DIR`，值为 OpenSSL 安装目录

  ![[Pasted image 20240806172739.png]]

> [!error] `SerialKeyEdition.cpp` ，`SerialKeyEditionTests.cpp` 

![[Pasted image 20240806172748.png]]

编码问题，VS 读中文有问题，将源码中中文改成英文即可

# 结果

构建结果在 `build-synergy-core-Qt_5_12_12_MSVC2017_64bit-Release` 目录中，复制 `bin` 目录和 `synergy-core/ext/openssl` 目录，运行 `synergy.exe`

![[Pasted image 20240806172829.png]]


# 运行时问题

> [!error] 去激活

版本在 `lib/shared/EditionType.h` 中的 `Edition` 枚举中，将 `kUnregistered` 相关的地方全部改成想要的版本后重新编译即可

> [!error] 找不到 OpenSSL

synergy-core 使用 OpenSSL 1.1，安装的 OpenSSL 版本为 3.1。

* 在 `CMake Modules\dist\wix\Product.wxs` 中将所有 `libcrypto-1_1-x64.dll` 替换成 `libcrypto-3-x64.dll`
* 在 `CMake Modules\dist\wix\Product.wxs` 中将所有 `libssl-1_1-x64.dll` 替换成 `libssl-3-x64.dll`
* 在 `CMake Modules\dist\wix\Product.wxs` 中将所有 `libcrypto-1_1.dll` 替换成 `libcrypto-3.dll`
* 在 `CMake Modules\dist\wix\Product.wxs` 中将所有 `libssl-1_1.dll` 替换成 `libssl-3.dll`
* 重新编译，在 OpenSSL 安装目录 `bin` 子目录下复制 `libcrypto-3-x64.dll`，`libssl-3-x64.dll`，`openssl.exe` 到 synergy 安装目录中

> [!error] ipc connection error

Synergy 需要服务运行

```shell
sc create synergy binPath= "synergy安装目录\synergyd"
```

之后在任务管理器 - 服务运行服务即可

> [!error] tls certificate doesn't exist

找不到 TLS 认证，编辑 - Preference 关闭 TLS 即可

![[Pasted image 20240806172846.png]]
