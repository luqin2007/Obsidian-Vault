# 请开始你的表演，数组、Subshell 与函数
# 数组

Bash 支持一维数组和关联数组，暂不支持多维数组。
## 一维数组

直接使用下标访问即可，索引可以是正负整数及其表达式、`*` 和 `@`

| 索引                  | 功能             | 备注                                                       |
| ------------------- | -------------- | -------------------------------------------------------- |
| `n`                 | 获取或修改第 n 个元素   |                                                          |
| `-n`                | 获取或修改倒数第 n 个元素 |                                                          |
| `*`，`@`             | 输出所有变量         | 书上说 `*` 是作为一个整体输出，在循环中视为一个元素，但实测 `*` 和 `@` 都可以用于遍历所有数组元素 |
| `!arr[*]`，`!arr[@]` | 输出所有下标         |                                                          |
> [!note] 索引不连续时，`*` 不会输出不存在的索引

```shell
name[0]="Jacob"
name[1]="Rose"
name[2]="Vicky"
name[3]="Rick"
name[4+4]="TinTin"

echo $name
echo ${name[0]}
echo ${name[1]}
echo ${name[4]}
echo ${name[8]}
echo ${name[-1]}
echo ${name[-2]}
echo ${name[-9]}
echo ${!name[@]}
echo ${!name[*]}

for i in ${name[*]}
do
    echo "*-$i"
done
for j in ${name[@]}
do
    echo "@-$j"
done
```

也可以使用 `()` 创建数组，下标从 0 开始连续创建，以 ` ` 分隔

```shell
name=(Jacob Rose Vicky Rick  TinTin)
echo ${name[*]}
echo ${!name[*]}
```
## 关联数组

使用 `declare -A <数组名>` 创建关联数组，关联数组下标可以是任意字符串。

> [!note] 普通数组与关联数组之间不可以互相转化

```shell
declare -A man
man[name]=TOM
man[age]=26
man[addr]=Beijing
man[phone]=13666666666

echo ${man[name]}
echo ${man[*]}
echo ${!man[*]}
```

关联数组也支持 `()` 初始化

```shell
declare -A woman
woman=([name]=lucy [phone]=13999999999 [age]=27 [addr]=Xian)

echo ${woman[name]}
echo ${woman[*]}
echo ${!woman[*]}
```

---

> [!example] 统计 Nginx 日志
```reference fold
file: "@/_resources/codes/linuxshell/nginx_log.sh"
lang: "shell"
```
# Subshell
## fork

使用 `()` 可以创建一个子进程。子进程继承父进程的上下文。
- 每进入一层子进程，`BASH_SUBSHELL+1`

```reference fold
file: "@/_resources/codes/linuxshell/subshell.sh"
lang: "shell"
```

除此之外，以下符号也会开启子进程：
- 管道运算 `|`
- 分组符号 `()`、分组替换 `$()`
- 后台程序 `&`
- 执行其他程序或脚本

使用 `>` 导出文件不会开启新进程
使用 `source` 载入其他脚本不会开启新进程
## exec

使用 `exec` 执行 Shell 指令将开启一个子进程，并将子进程替代父进程，因此执行结束后会退出。

通常使用 `fork` 的形式执行 `exec` 的代码用于防止覆盖当前脚本

> [!warning] 当 `exec` 后接 `>` 时不会替换当前进程
## source

执行一个脚本，不打开子进程
## wait

为防止同时输出内容过多，导致脚本退出过快，而屏幕没有反应过来，可加一个 `wait` 等待所有子进程结束后再退出。

```reference hl:19
file: "@/_resources/codes/linuxshell/ping_mutil_proc.sh"
lang: "shell"
```
# 函数

函数声明有多种

```shell
函数名() {
    # do something
}

function 函数名() {
    # do something
}

function 函数名 {
    # do something
}
```

- 直接使用函数名调用函数，不需要 `()`
- 使用 `unset 函数名` 可以取消函数

```reference
file: "@/_resources/codes/linuxshell/usage.sh"
lang: "shell"
```

- 使用 `$n` 访问参数，参数使用空格分隔

```reference
file: "@/_resources/codes/linuxshell/check_service.sh"
lang: "shell"
```
## 变量作用域

Shell 调用函数时不会创建子进程，且不像一般程序语言没有函数栈的隔离，变量相当于直接在全局声明，在函数内外都可访问

```shell
#!/bin/bash

global_var1="hello"
global_var2="world"

function demo() {
    echo -e "\e[46mfunction [demo] started...\e[0m"
    func_var="Topic:"
    global_var2="Broke Girls"
    echo "$func_var $global_var2"
    echo -e "\e[46mfunction [demo] end\e[0m"
}

demo

echo
echo "$func_var $global_var1 $global_var2"
```

例外的是使用 `declare` 创建的关联数组和使用 `local <var-name>` 创建的变量，作为函数内部变量使用，不能在外部访问

```shell
#!/bin/bash

a=(aa bb cc)
declare -A b
b[a]=11
b[b]=22

echo ${a[@]}
echo ${b[@]}

function demo() {
    a=(xx yy zz)
    declare -A b
    b[a]=88
    b[b]=99
    echo
    echo ${a[@]}
    echo ${b[@]}
}
demo

echo
echo ${a[@]}
echo ${b[@]}
```
## return

函数默认以最后一条语句的状态码为返回值，也可以通过 `return` 返回一个返回值作为状态码，通过 `$?` 访问

```shell
#!/bin/bash

# 最后一条语句的状态码
demo1() {
    uname -r
}

# 主动使用 return 返回状态码
demo2() {
    echo "start demo2"
    return 100
    echo "end demo2"
}

# exit 则会导致脚本退出
demo3() {
    echo "hello"
    exit
}

demo1
echo "demo1 status is $?"
demo2
echo "demo2 status is $?"
demo3
echo "demo3 status is $?"
```
# 进程数控制

通常使用文件描述符控+命名管道控制进程数量，实现每次启动有限个进程，多批次执行的效果
## 文件描述符

文件描述符是一个非负整数，是内核访问文件的一个 id，用于文件读写
- 当每次打开已有文件或创建文件时，由内核返回一个描述符
- 系统默认有 3 个描述符
	- 0：stdin
	- 1：stdout
	- 2：stderr

通过 `/proc/<PID 号>/fd` 可以查看每个进程拥有（使用）的文件描述符

![[../../../_resources/images/Pasted image 20241202110057.png]]

文件描述符可以手动创建和关闭
- 创建：`exec 文件描述符 <> 文件名`，`>` 表示可写，`<` 表示可读，`<>` 表示读写，`>>` 表示追加
- 使用：`&文件描述符`
- 关闭：`exec 文件描述符<&-` 或 `exec 文件描述符>&-`

```reference
file: "@/_resources/codes/linuxshell/fd_demo.sh"
lang: "shell"
```

`read` 可以通过 `read -u<fd>` 指定读的文件描述符
## 命名管道

管道是 Linux 进程间通信的一种方式，前面 `|` 创建的管道是一种匿名管道，仅用于父进程向子进程传递信息。

使用命名管道可用于在两个进程之间互相传递信息，命名管道又称 FIFO 文件
- 通常使用 `mknod` 或 `mkfifo` 创建，可在文件系统中看到，标记为 `p`
- 命名管道内的数据常驻内存，不实际写入硬盘
- 读写阻塞：管道内有数据时，写入阻塞；管道内无数据时，读取阻塞

通过阻塞机制，可以用于控制进程数量

```reference hl:11,22
file: "@/_resources/codes/linuxshell/multi_proc.sh"
lang: "shell"
```
