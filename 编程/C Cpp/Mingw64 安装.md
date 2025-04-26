Mingw64 有多个安装器，详见 [Downloads - MinGW-w64](https://www.mingw-w64.org/downloads/)

这里使用 MingW-W64-builds 安装，下载地址：[Releases · niXman/mingw-builds-binaries (github.com)](https://github.com/niXman/mingw-builds-binaries/releases)

根据需求选择分支版本，这里选择 `x68_64-posix-seh-ucrt` 版本

| 版本   | 选择分类     | 说明                                                                                 |
| -------- | -------------- | -------------------------------------------------------------------------------------- |
| i686   | CPU指令集    | 32 位                                                                                |
| x86_64 | CPU指令集    | 64 位                                                                                |
| posix  | 多线程标准   | 使用 posix 线程，启用C++11/C11多线程功能，并使 libgcc 依赖于 libwinpthreads          |
| win32  | 多线程标准   | 使用 Windows 的 API 标准，不会启用C++11多线程功能                                    |
| mscvrt | C 标准库     | Microsoft Visual C++ Runtime，在所有 Windows 版本都可用，但缺少一些功能              |
| ucrt   | C 标准库     | 较新，Microsoft Visual Studio 默认使用的版本，有像使用 MSVC 编译的代码一样工作和表现 |
| dwarf  | 异常处理模型 | dwarf-2，仅适用于 32 位系统，没有永久的运行时开销，但需要整个调用堆栈被启用          |
| seh    | 异常处理模型 | 零开销 exception，可用于 64 位系统                                                   |

下载后解压 mingw64 目录到想安装的位置，配置 path 环境变量到 mingw64/bin 目录。

运行 gcc --version 正常输出即可

![[Pasted image 20240806163318.png]]
