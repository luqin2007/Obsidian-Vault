`makefile` 文件告诉 `make` 如何编译和链接程序

```makefile
target ... : prerequisites ...
	recipe
```

- `target`：目标，可以是一个目标文件名，也可以是一个动作名
- `prerequisites`：该目标依赖的其他文件和 `target`
- `recipe`：该 `target` 执行的指令

> [!attention] 缩进必须是 Tab，不能用空格

> [!note] 一行写不开的时候，行尾使用 `\` 换行

> [!info] 注释：使用 `#` 开头，与 `bash` 一样

例：应用程序 `edit` 项目包含 3 个头文件和 8 个源码文件 

```reference
file: "@/_resources/codes/make/example/makefile"
lang: "makefile"
```

# 变量

```
变量名 = 变量值
```

使用时，使用 `$(变量名)` 引用

```embed-makefile
PATH: "vault://_resources/codes/make/var/makefile"
LINES: "1-2,4-5,23-24"
```

# 隐式推导

当需要生成一个 `<file>.o` 文件时，`make` 自动推导 C 语言编译过程：
-  `<file.c>` 会自动加入对应依赖文件中
- 指令推导为 `cc -c <file>.o`

因此前面的文件可以进一步简化为

```reference
file: "@/_resources/codes/make/implicit/makefile"
lang: "makefile"
start: 7
end: 14
```

# 合并任务

前面的文档中，还有很多重复部分：
- 所有 `.o` 的编译任务都依赖于 `defs.h`
- 部分 `.o` 编译任务依赖于 `command.h` 或 `buffer.h`

根据此分组，还可以对任务进一步简化，`make` 可以自动合并依赖并完成隐式推导：

```reference
file: "@/_resources/codes/make/simple/makefile"
lang: "makefile"
start: 7
end: 9
```

# 伪目标

`clean` 任务不生成目标文件或可执行文件，称为伪目标。伪目标可以直接写，更稳健的做法是将其作为 `.PHONY` 的一个依赖

```reference
file: "@/_resources/codes/make/phony/makefile"
lang: "makefile"
start: 11
end: 13
```

> [!note] 指令前的 `-` 表示当命令出现错误时，忽略错误并继续进行

# 包含文件

`{makefile} include 文件名...` 表示引入其他 `makefile` 文件内容，前面可以有空格但不能有 Tab

`````col
````col-md
flexGrow=1
===
```dirtree
- Makefile
- foo.make
- a.mk
- b.mk
- c.mk
- bish
- bash
```
````
````col-md
flexGrow=3
===
```makefile title:Makefile
bar = bish bash
include foo.make *.mk $(bar)
```
等效于
```makefile
include foo.make a.mk b.mk c.mk bish bash
```
````
`````

找不到文件时 `make` 会产生一条警告。 `include` 搜索文件的路径包括：
- 当前目录
- `make` 执行时 `-I` 或 `--include-dir` 指定的目录
- `$.INCLUDE_DIRS$` 环境变量

`sinclude` 与 `-include` 效果相同，表示当找不到文件时跳过

## 环境变量

`MAKEFILES` 环境变量与 `-include` 作用类似，也是引入文件，但默认目标不起作用