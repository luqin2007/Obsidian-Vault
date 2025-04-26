# 上古神兵利器 sed

sed 会逐行扫描输入数据存入模式空间（一个缓冲区），将其与给定模式匹配，匹配成功则执行对应 sed 指令，否则跳过，最终输出结果

```shell
命令 | sed <选项> '匹配条件和操作指令'
sed <选项> '匹配条件和操作指令' 文件...
```
# 基本指令

## 命令选项

| 命令选项        | 功能描述                             |
| ----------- | -------------------------------- |
| -n，--silent | 屏蔽默认输出，不会把数据显示在屏幕上               |
| -r          | 支持扩展正则                           |
| -i[SUFFIX]  | 直接修改源文件，如果设置了SUFFIX后缀名，sed会将数据备份 |
| -e          | 指定需要执行的sed指令，支持使用多个-e参数          |
| -f          | 指定需要执行的脚本文件，需要提前将sed指令写入文件中      |

## 操作指令

| 操作指令              | 功能描述                    |
| ----------------- | ----------------------- |
| p                 | 打印当前匹配的数据行              |
| l                 | 打印当前匹配的数据行，显示控制字符，如回车符等 |
| =                 | 打印当前读取的数据行数             |
| a text            | 在匹配的数据行后面追加文本内容         |
| i text            | 在匹配的数据行前面插入文本内容         |
| d                 | 删除匹配的数据行整行内容（行删除）       |
| c text            | 将匹配的数据行整行内容替换为特定的文本内容   |
| r filename        | 从文件中读取数据并追加到匹配的数据行后面    |
| w filename        | 将当前匹配到的数据写入特定的文件中       |
| q [exit code]     | 立刻退出sed脚本               |
| s/regexp/replace/ | 使用正则匹配，将匹配到的数据替换为特定的内容  |

## 数据定位

| 格式                 | 功能描述                          |
| ------------------ | ----------------------------- |
| number             | 直接根据行号匹配数据                    |
| first~step         | 从frst行开始，步长为step，匹配所有满足条件的数据行 |
| $                  | 匹配最后一行                        |
| /regexp/，\cregexpc | 使用正则表达式匹配数据行，c可以是任意字符         |
| addr1,addr2        | 直接使用行号定位，匹配从addr1到addr2的所有行   |
| addr1,+N           | 直接使用行号定位，匹配从addr1开始及后面的N行     |
| !                  | 条件取反                          |
## 实例

### 根据行号选择行

> [!example] 输出 `/etc/hosts` 的文件内容
> ```shell
> sed 'p' /etc/hosts
> ```

> [!bug] 上面的代码每行会输出两次，因为 `sed` 本身会打印一次
> ```shell
> sed -n 'p' /etc/hosts
> ```

> [!example] 输出 `/etc/hosts` 文件第二行
> ```shell
> sed -n '2p' /etc/hosts
> ```

> [!example] 输出 `/etc/hosts` 文件第 1-3 行
> ```shell
> sed -n '1,3p' /etc/hosts
> ```

> [!example] 输出 `/etc/hosts` 文件第二行到文件末尾
> ```shell
> sed -n '2,$p' /etc/hosts
> ```

> [!example] 输出 `/etc/hosts` 文件第 2,5 行
> ```shell
> sed -n '2p;5p' /etc/hosts
> ```

> [!example] 输出 `/etc/hosts` 文件第 2 行及后 3 行（第 2-5 行）
> ```shell
> sed -n '2,+3p' /etc/hosts
> ```

> [!example] 输出 `/etc/hosts` 文件偶数行（从第 2 行开始，步长为 2）
> ```shell
> sed -n '2~2p' /etc/hosts
> ```

> [!example] 输出 `/etc/hosts` 文件第 1,2,4,5,7,8,... 行（跳过 3,6,9... 行），带行号
> 使用 `cat -n` 为文件添加行号，通过管道传递给 `sed`
> ```shell
> cat -n /etc/hosts | sed -n '3~3!p'
> ```
### 根据内容选择行

> [!example] 输出 `/etc/passwd` 中以 `/bash` 结尾的行
> ```shell
> sed -n '/bash$/p' /etc/passwd
> ```

> [!example] 输出 `/etc/passwd` 中包含以 `s` 开头，以 `:x` 结尾，中间包含三个字符的内容的行
> ```shell
> sed -n '/s...:x/p' /etc/passwd
> ```

> [!example] 输出 `/etc/passwd` 中包含数字的行
> ```shell
> sed -n '/[0-9]/p' /etc/passwd
> ```

> [!example] 输出 `/etc/services` 中以 `http` 开头的行
> ```shell
> sed -n '/^http/p' /etc/services
> ```

> [!example] 输出 `/etc/protocols` 中包含 `icmp` 或 `igmp` 的行
> `|` 条件需要 `-r` 开启扩展正则
> ```shell
> sed -rn '/^(icmp|igmp)/p' /etc/protocols
> ```

> [!example] 输出 `/etc/shells` 中包含 `bash` 的行
> `\xbashx` 中开头结尾的 `x` 可以是任意字符
> ```shell
> sed -n '\xbashxp' /etc/shells
> ```

> [!example] 输出 `/etc/shells` 中不包含 `bash` 的行
> ```shell
> sed -n '/bash/!p' /etc/shells
> ```

> [!example] 输出 `/etc/shells`，同时输出控制字符
> ```shell
> sed -n 'l' /etc/shells
> ```

### 修改整行内容

所有样例均不会修改原文件，而是将修改后的内容输出。如需要保存详见[[#文件保存]]
#### 添加行

使用 `a` 在选定行前添加行，`i` 在选定行后添加行

> [!example] 在 `/etc/hosts` 第一行后追加一行 `add test line`
> ```shell
> sed '1a add test line' /etc/hosts
> ```

> [!example] 在 `/etc/hosts` 第一行前添加一行 `add test line`
> ```shell
> sed '1i add test line' /etc/hosts
> ```

> [!example] 在 `/etc/hosts` 所有包含 `new` 的行后添加一行 `add test line`
> ```shell
> sed '/new/a add test line' /etc/hosts
> ```
#### 删除行

使用 `d` 删除选定行

> [!example] 删除 `/etc/hosts` 所有偶数行，输出带有行号
> ```shell
> cat -n /etc/hosts | sed '2~2d'
> ```

> [!example] 删除空白行
> ```shell
> sed '/^$/d' /etc/hosts
> ```

> [!example] 删除注释行（`#` 开头）
> ```shell
> sed '/^#/d' /etc/hosts
> ```
#### 替换行

使用 `c` 替换行

> [!example] 将第二行替换为 `modity line`
> ```shell
> sed '2c modity line' /etc/hosts
> ```

> [!example] 将所有行替换为 `all modity`
> ```shell
> sed 'c all modity' /etc/hosts
> ```
#### 追加文件

> [!example] 在 `/etc/hosts` 每行后追加主机名（`/etc/hostname` 文件内容）
> ```shell
> sed 'r /etc/hostname' /etc/hosts
> ```

> [!example] 在 `/etc/hosts` 结尾追加主机名（`/etc/hostname` 文件内容）
> ```shell
> sed '$r /etc/hostname' /etc/hosts
> ```
### 修改局部内容
#### 以关键词为单位替换

使用 `s` 以关键词为单位进行正则替换

> [!example] 将 `~/test` 每行第一个 `o` 替换为 `O`
> ```shell
> sed 's/o/O/' ~/test
> ```

> [!example] 将 `~/test` 所有 `o` 替换为 `O`
> ```shell
> sed 's/o/O/g' ~/test
> ```

> [!example] 将 `~/test` 每行第二个 `o` 替换为 `O`
> ```shell
> sed 's/o/O/2' ~/test
> ```

> [!example] 将 `~/test` 所有 `o` 替换为 `O`，输出被替换行
> ```shell
> sed -n 's/o/O/gp' ~/test
> ```

> [!example] 将 `~/test` 所有 `jacob` 替换为 `Vicky`，忽略大小写
> ```shell
> sed 's/jacob/Vicky/gi' ~/test
> ```

> [!example] 将 `~/test` 所有 `o` 替换为 `O`，输出被替换行
> ```shell
> sed -n 's/o/O/gp' ~/test
> ```

> [!example] 在字符串 `/etc/hosts` 前添加 `ls -l `，并将替换后的文本作为命令执行
> ```shell
> echo /etc/hosts | sed 's/^/ls -l /e'
> ```

sed 开启扩展正则后支持 `()` 捕获，使用 `\1`，`\2` 等替换

> [!example] 将 `~/test` 中每行收尾字符对调
> ```shell
> sed -r 's/^(.)(.*)(.)$/\3\2\1/' ~/test
> ```

`s` 默认使用 `/` 作为替换标记，但也可以使用别的符号，如 `s#...#...#` 等效于 `s/.../.../`，在路径替换时可避免对 `/` 的转义

> [!example] 将 `/etc/passwd` 中所有的 `/usr/bin/nologin` 替换成 `/bin/sh`
> ```shell
> sed 's/\/usr\/bin\/nologin/\/bin\/sh/g' /etc/passwd
> ```
> 
> ```shell
> sed 's#/usr/bin/nologin#/bin/sh#g' /etc/passwd
> ```
#### 以字符为单位替换

使用 `c` 以关键词为单位进行正则替换

> [!example] 将 `~/test` 中的 h 替换为 1，g 替换成 2
> ```shell
> sed 'y/hg/12/' ~/test
> ```

> [!example] 将 `~/test` 中的小写字母替换成大写字母
> ```shell
> sed 'y/[abcdefghijklmnopqrstuvwxyz]/[ABCDEFGHIJKLMNOPQRSTUVWXYZ]/' ~/test
> ```
### 文件保存

> [!example] 在 `/etc/hosts` 第一行后追加一行 `add test line`，保存并将原文件备份为 `hosts.bak`
> 使用 `-i` 保存，后接一个后缀名作为备份文件名后缀。此时控制台不会有输出
> ```shell
> sed -i.bak '1a add test line' /etc/hosts
> ```

> [!example] 将 `/etc/hosts` 另存为 `/tmp/hosts.bak`
> 使用 `w` 将输出的内容存入指定文件。此时控制台仍有输出
> ```shell
> sed 'w /tmp/hosts.bak' /etc/hosts
> ```

> [!example] 将 `/etc/hosts` 第 1-3 行提取为 `/tmp/hosts.bak`
> ```shell
> sed '1,3w /tmp/hosts.bak' /etc/hosts
> ```
### 中断

`q` 可以退出 `sed` 对当前文件的处理

> [!example] 读到第三行时终止 `sed`
> ```shell
> sed '3q' /etc/hosts
> ```

`n` 可以将当前模式空间的数据输出并清空，然后调入下一行数据继续执行

> [!example] 删除 `~/test` 中的偶数行
> 读入一行 - 输出 - 读入一行 - 丢弃
> ```shell
> cat -n ~/test | sed 'n;d'
> ```

# 高级指令

除编辑数据的缓冲区（模式空间）外，`sed` 还有一块保留空间，保留空间内默认有字符串 `\n`

![[../../../_resources/images/sed 2024-12-07 08.20.16.excalidraw]]

| 操作指令    | 功能描述                               |
| ------- | ---------------------------------- |
| h       | 将模式空间中的数据复制到保留空间                   |
| H       | 将模式空间中的数据追加到保留空间                   |
| g       | 将保留空间中的数据复制到模式空间                   |
| G       | 将保留空间中的数据追加到模式空间                   |
| x       | 将模式空间和保留空间中的数据对调                   |
| n       | 读取下一行数据到模式空间                       |
| N       | 读取下一行数据追加到模式空间                     |
| y/源/目标/ | 以字符为单位将源字符转为为目标字符                  |
| :label  | 为 t 或 b 指令定义 label 标签              |
| t label | 有条件跳转到标签（label），如果没有label则跳转到指令的结尾 |
| b label | 跳转到标签（label），如果没有label则跳转到指令的结尾    |
## 保留空间

> [!example] 读取 `~/test` 文件，使用第二行数据覆盖第五行数据
> 使用 `cat -n` 为数据添加行号以查看结果
> 1. 将第二行数据复制到保留空间（`2h`）
> 2. 在第五行将模式空间覆盖到数据空间（`5g`）
> ```shell
> cat -n ~/test | sed '2h;5g'
> ```

> [!example] 读取 `~/test` 文件，将第二行数据插入到第五行后面
> ```shell
> cat -n ~/test | sed '2h;5G'
> ```

> [!example] 读取 `~/test` 文件，将第二行剪切到第五行
> ```shell
> cat -n ~/test | sed '2h;2d;5g'
> ```

> [!example] 将第三行追加到第二行后（使用 `\n` 分隔，构造多行模式空间）
> 使用 `l` 而不是 `p` 输出，可以看到一些控制字符（`\n`，`$` 等）
> ```shell
> cat -n ~/test | sed -n '2{N;l}'
> ```
## 标签跳转

`sed` 支持基于标签的跳转指令，类似 `goto`
- `:label`：创建名为 `label` 的标签
- `b label`：branch，无条件跳转到 `label` 标签
- `t label`：test，有条件跳转到 `label` 标签

```shell
:label
sed 指令序列

# ...
b label

# ...
s/regex/replace/
t label
```

> [!example] 读取 `~/test` 文件，从第一行开始输出到第四行，然后死循环不断输出第四行行号和内容
> 1. 创建标签 `top`
> 2. 使用 `=` 输出行号
> 3. 使用 `p` 输出内容
> 4. 使用 `4b top` 匹配当第四行时，跳转到 `top`
> ```shell
> sed -n ':top;=;p;4b top' ~/test
> ```

> [!example] 输出 `~/test` 文件行号和内容，若行中包含 `go` 则只输出内容
> 1. 匹配 `go` 时跳转到 `label`
> 2. 输出行号
> 3. 定义标签 `label`
> 4. 输出内容
> ```shell
> sed -n '/go/b label;=;:label;p' ~/test
> ```

> [!example] 将 `~/test` 文件中第一个 `hello` 替换成 `nihao`
> ```shell
> sed '/hello/{s/hello/nihao/;:next;n;b next}' ~/test
> ```
> ![[../../../_resources/images/sed 2024-12-07 23.19.08.excalidraw]]

> [!example] 为 MAC 地址添加 `:` 分隔符
> ```shell
> echo fe54008f2592 | sed -r ':start; s/([^:]+)([0-9a-f]{2})/\1:\2/; t start'
> ```
# 多指令

`sed` 支持多条匹配条件和操作指令，可使用 `;` 分隔或 `-e`

```shell
sed -n '1p;3p;5p' test
sed -n -e '1p' -e '3p' -e '5p' test
```

`sed` 还支持使用 `-f` 从文件中读取指令

```shell
sed -f ./script.sed ~/test
```

存放 `sed` 指令的文件通常扩展名为 `.sed`，每行一条指令

```reference
file: "@/_resources/codes/linuxshell/script.sed"
lang: "sed"
```

- 将第一行修改为 `hello world`
- 输出第二行数据，然后将第二行数据中的 `g` 替换成 `G`
- 将所有包含数字的行删除
- 匹配包含 `beijing` 的行，将 `h` 替换成 `H`，将 `beijing` 替换成 `china`
