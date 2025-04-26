执行 `make` 指令后，`make` 会在当前目录下搜索 `makefile` 或 `Makefile` 文件，如果找不到则执行失败。

> [!note] `make` 执行后，返回 0 表示成功，1 表示出现错误，2 表示使用了 `-q` 参数且一些目标不需要更新

例：应用程序 `edit` 项目包含 3 个头文件和 8 个源码文件 

```reference
file: "@/_resources/codes/make/example/makefile"
lang: "makefile"
```

# 目标

文件中第一个任务是默认任务，`make` 不指定任何任务时使用该任务。

以举例的 `makefile` 文件为例，在没有指定具体任务的情况下，`make` 自动执行第一个任务 `edit`。`make` 会递归的检查每个依赖文件，直到源文件，只要有一个更新，则该链上的任务都会执行。

> 如修改了 `buffer.h`，所有依赖于该文件的任务 `display.o`、`insert.o`、`search.o`、`files.o` 都会重新执行

其他目标使用 `make 目标名` 执行。如 ` clean ` 通过 ` make clean ` 执行

> [!attention] 以 `-` 开头或包含 `=` 的目标不能手动指定执行，因为 `make` 会将这类任务名解析为参数

`MAKECMDGOALS` 变量记录了手动指定的目标列表

> [!note] 一个 `makefile` 中的常见伪目标
> - `all`：所有目标任务，用于编译所有目标
> - `clean`：删除 `make` 创建的文件
> - `install`：安装，编译并将目标文件复制到指定目录
> - `print`：列出所有变更的源文件
> - `tar`：打包源码为一个 `tar` 文件
> - `dist`：打包源码为一个 `gz` 压缩包
> - `TAGS`：更新所有目标，用于重编译
> - `check`、`test`：测试

# `makefile` 文件命名

默认 `make` 命令会在目录中查找 `GNUmakefile`、`makefile` 和 `Makefile` 文件，其中 `GNUmakefile` 仅 `GNUMake` 使用。

>[!note] 推荐使用 `Makefile` 作为文件名，因为其排序上更接近于 `README` 等重要文件

也可以使用其他文件名，需要 `-f` 或 `--file` 参数指定，可以指定多个文件。

# `makefile` 文件读取过程

1. 读入 `Makefile` 、`makefile` 或 `-f/--file` 指定的所有文件
2. 读入 `include` 或 `MAKEFILES` 引入的所有文件
3. 初始化文件中的变量
4. 引入隐式推导分析所有规则
5. 生成依赖关系链
6. 根据依赖关系和执行的命令，决定哪些目标需要重新生成
7. 执行生成命令

# `make` 参数

详见 [make 的运行 — 跟我一起写Makefile 1.0 文档 (seisman.github.io)](https://seisman.github.io/how-to-write-makefile/invoke.html#id3)

- `-n`、`--just-print`：仅打印命令，不执行，用于调试
- `-W file`，`--what-if=file`：执行依赖于某个文件的命令，常配合 `-n`
- `-q`，`--question`：查找一个目标，若存在则什么也不做，不能存在则输出错误
- `-debug=a/b/v/i/j/m`：调试信息，`a` 为 `all`，`i` 包含隐含规则，`j` 包含命令详细信息，`m` 输出 `makefile` 信息
- `-s`，`--slient`：不输出命令输出

- `-c dir`，`--directory=dir`：指定 `makefile` 目录
- `-f file`，`--file=file`：指定 `makefile` 文件路径
- `-I dir`，`--include-dir=dir`：指定源码包含目录
- `-i`，`--ignore-errors`：忽略所有错误



```cardlink
url: https://seisman.github.io/how-to-write-makefile/invoke.html#id3
title: "make 的运行 — 跟我一起写Makefile 1.0 文档"
host: seisman.github.io
```
