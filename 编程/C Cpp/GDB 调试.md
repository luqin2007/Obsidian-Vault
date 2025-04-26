# GDB 环境

- 编译工具：gcc，g++，make
- 调试工具：gdb

> [!note] g++ 使用 -g 编译，保留调试符号便于调试，但生成的文件更大

使用 `gdb 可执行文件` 开启调试。提示 `(No debugging symbols found in ...)` 表示没有生成调试符号（`-g`）。
# 基本功能
## 启动调试

若程序需要参数，使用以下几种方式设置参数：
- 命令行 `gdb --args <exe> <args>`
- gdb 环境中使用 `set args <args>`
- gdb 环境中使用 `r <args>` 设置参数并运行

使用 `r` / `run` 开始执行程序，程序运行时会在断点处停止
## 附加进程

gdb 也可以附加到正在运行的进程上（`ps -aux`）

- `gdb attach <pid>`
- `gdb --pid <pid>`

> [!attention] 附加到进程需要 root 权限
## 查看源码

使用 `l` / `list` 命令显示源代码，一次显示 10 行，回车或使用 ` l ` 继续查看，使用 ` l . ` 回到开头
- `l 文件名:行号`：从某个文件的某行开始查看
- `l 函数名`：查看某个函数

![[../../_resources/images/Pasted image 20250116160827.png]]

使用 `search` 系列命令可以搜索源代码
- `search 正则`：从当前代码行查找源代码，等效于 `forward-search 正则`
- `reverse-search 正则`：从当前代码行开始向前搜索

默认显示的行数可以通过 `set linesize n` 设置为一次显示 n 行

使用 `view directories` 可以查看源码搜索的目录
- `directory 目录`：添加源码目录
## 断点调试

使用 `b` / `break` 命令设置断点
- `b 函数名`：在函数第一行设置断点
- `b 文件名:行号`：在指定文件的某一行设置断点

在断点处停止后，
- 单步（跳过函数） step-over：`n` / `next`
- 单步（进入函数） step-into：`s` / `step`
- 退出（执行完）当前函数：`finish`
- 继续执行：`c` / `continue` ，直到下一个断点或程序结束
### 添加断点

- `b 文件名:行号`：在指定文件的指定行设置断点
![[../../_resources/images/Pasted image 20250117134308.png]]

- `b 函数名`：在指定函数名的第一行都加断点，包括不同位置的**同名函数**和**重载函数**
![[../../_resources/images/20250117_267_vmware.png]]
![[../../_resources/images/20250117_268_vmware.png]]

- `b 断点 条件`：当符合条件时触发断点，多用于循环中
![[../../_resources/images/20250117_272_vmware.png]]

- `rb 正则表达式`：对所有符合正则表达式的行设置断点
![[../../_resources/images/20250117_269_vmware.png]]

- `tb 断点`：临时断点，只触发一次，命中后取消该断点
![[../../_resources/images/20250117_275_vmware.png]]
### 断点管理

使用 `i b` / `info breakpoints` / `info break` 查看所有断点信息。
- `i b n` 可以只查看第 n 个断点

在断点停止时可以使用 `delete` 删除断点，也可以使用 `delete n` 删除第 n 个断点
- 在没有运行时使用 `delete` 可以删除所有断点

使用 `disable n` 禁用某个断点
### 变量管理

在端点暂停时可以查看运行时的变量名
- `p 变量名`：查看指定变量，支持查看数组
- `p locals`：查看所有局部变量
- `i args` / `info args`：查看所有传入的参数（main 函数的参数）

`p` 还可以用于手动执行语句，显示结果，如 ` p size(int) `，` p strlen(某字符串) ` 等

使用 `set print` 可以设置显示样式
- `set print null-stop`：char 数组形式存储的字符串，只显示到 `\0` 之前
- `set print pretty`：结构体中，一个字段显示一行
- `set print array on`：数组中，一个元素显示一行

修改变量值仍然使用 `p`：`p 变量=值`
### 内存管理

> [!note] 内存地址可以直接使用 `&` 取地址符从变量获取。`&` 也可以通过 ` p ` 查看

使用 `x` / `examine` 查看内存：`x /选项 地址`
- `x 地址`：直接查看某地址值
- `x /s 地址`：从指定地址读一个字符串
- `x /d 地址`：从指定地址读一个整型
- `x /4d 地址`：从指定地址开始读 4 个整型
- `x /4b 地址`：从指定地址开始读 4 个字节

使用 `set (类型)地址=值` 方式修改内存值，但用的不多，可以直接修改变量值实现。
### 寄存器管理

使用 `i r` / `i registers` 查看寄存器，多用于在没有调试符号时进行调试
- `i r`：查看所有寄存器
- `i r 寄存器名`：查看指定寄存器

> [!note] 当函数参数少于 6 个时通过寄存器传递，否则通过栈传递

| 寄存器 | 说明    |
| --- | ----- |
| rdi | 第一个参数 |
| rsi | 第二个参数 |
| rdx | 第三个参数 |
| rcx | 第四个参数 |
| r8  | 第五个参数 |
| r9  | 第六个参数 |
修改寄存器可以通过 `set` 或 `p` 设置
- `set var $寄存器名=值`
- `p $寄存器名=值`

> [!example] 设置 `pc`（又称 `rip`）寄存器
> 1. 使用 `info line 行` 查看汇编地址（`start at address ...`）
> 	- 也可以使用 `disassemble` 反汇编查看
> 2. 使用 `set var $rip=...` 设置跳转
### 栈帧管理

- `bt` / `backtrace`：查看栈回溯信息（调用层级）
![[../../_resources/images/Pasted image 20250118173000.png]]

- `frame n` / `f n`：切换到 n 号栈帧
- `info f n`：查看 n 号栈帧信息
![[../../_resources/images/Pasted image 20250118173057.png]]
## 退出调试

- 退出 gdb：`q` / `quit`，使用 `attach` 会杀死进程
- 分离进程：`detach`，退出 gdb 但不杀死进程
# 多线程死锁
# 动态库
# 内存检查
# 远程调试
# Core dump 分析
# 发行版调试
# 参考

```cardlink
url: https://www.gnu.org/software/gdb/
title: "GDB: The GNU Project Debugger"
host: www.gnu.org
```
