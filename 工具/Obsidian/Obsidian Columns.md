Obsidian 多列显示插件，有三种设置语法
# 标注

使用标注语法 Callout（`>`）创建列，支持实时预览和少量设置
- 使用 `[!col]` 创建一组列
- 使用 `[!col-md]` 创建新列，两个 `[!col-md]` 之间需要空一行
- 使用 `[!col-md-n]` 设置列宽，列宽最大为 10，且需要是 0.5 的倍数

```
> [!col]
> 使用标注语法
>> [!col-md]
>> 第二列
>
>> [!col-md-2]
>> 第三列，列宽为 2
>
>> [!col-md]
>> 第四列
```

> [!col]
> 使用标注语法
>> [!col-md]
>> 第二列
>
>> [!col-md-2]
>> 第三列，列宽为 2
>
>> [!col-md]
>> 第四列
# 代码块

使用代码块语法设置列，外层代码块应该比内层至少多一组 \`
- 使用 `col` 代码块创建一组列
- 使用 `col-md` 代码块创建新列
- 代码块头部可以包含一些列设置，之后使用一行 `===` 分隔正文
	- `flexGrow=N`：设置列宽为 N
	- `height=N`：列高，可以是任何 CSS 的长度写法或  `shortest` 表示所有列中的最短高度
	- `textAlign=start|left|end|right|center|justify`，`justify` 调整列内容
- 使用  `===` 表示换行

`````
````col
使用代码块语法
```col-md
第二列
```
```col-md
flexGrow=2
===
第三列，列宽为 2
```
```col-md
第四列
```
````
`````

````col
使用代码块语法
```col-md
第二列
```
```col-md
flexGrow=2
===
第三列，列宽为 2
```
```col-md
第四列
```
````
# 列表

> [!error]
> 列表模式只能在阅读模式下生效，否则只是个列表

也可以使用多级列表语法 `-` 创建多列视图
- 列开头为 `!!!col`
- 次一级使用一个数字表示列宽

```
- !!!col
    - 1
        第一列
    - 2
        第二列，列宽为 2
    - 1
        第三列
```

- !!!col
    - 1
        第一列
    - 2
        第二列，列宽为 2
    - 1
        第三列
# 参考

```cardlink
url: https://github.com/tnichols217/obsidian-columns
title: "GitHub - tnichols217/obsidian-columns"
description: "Contribute to tnichols217/obsidian-columns development by creating an account on GitHub."
host: github.com
favicon: https://github.githubassets.com/favicons/favicon.svg
image: https://opengraph.githubassets.com/7911f960ab95e1988d4e767170781f19788a4536687df53e0ab8a567ca208d0a/tnichols217/obsidian-columns
```
