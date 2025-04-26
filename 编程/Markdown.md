# 区块
## 标题

类 `Setext`：标题下加 `=` 或 `-`

```markdown
类 Setext 标题一
=

类 Setext 标题二
-
```

类 `Atx`：使用 `#` `##` 一直到 `######`

```markdown
# 类 Atx 标题
```
## 引用

使用 `>` 表示引用，引用可嵌套，一段话可只加一个 `>` 符号

引用内可嵌套其他 Markdown 语法

```markdown

> 引用
>
> > 嵌套引用

> 段落引用，
段落结束
```
## 列表

无序列表使用 `*`，`+` 或 `-` 做标记

```markdown
* 无序1
+ 无序2
- 无序3
```

有序列表使用 `n.` 做标志，`n` 为任意数字

```markdown
1. 有序1
1. 有序2
8. 有序3
```

列表标记最多距离最左边 3 个空格，标记后至少跟一个空格或制表符

列表项目可包含多个段落，但列表下面每个段落必须缩进 4 空格或 1 制表符

可用 `\.` 形式防止列表转义
## 代码块

使用 \`\`\` 表示代码区块

```markdown
这是个代码区块
‍‍```

代码区块中，markdown 语法不会被转换
‍‍```
```
## 分割线

使用三个 `*`，`-`，`_` 创建分割线，三个字符之间可以加入空格。该行不允许有其他字符

```markdown
---
***
___
```
# 区段
## 链接

链接文字使用 `[]` 表示，地址用 `()` 表示

```markdown
[百度](www.baidu.com)
```

地址支持相对地址，使用主机资源

```markdown
[about](/about/)
```

参考链接，两个中括号连用，id可在文档任何位置。id 不区分大小写

参考链接 id 可省略（中括号内不输入任何字符），省略时 id 等同于链接文字

```markdown
[参考][baidu]
```

参考链接格式：`[]` 后加 `:` 加至少一个空格，加地址，选择性接着 `title` 内容，使用单引号/双引号/括号包裹

```markdown
[ baidu]: www.baidu.com	"百度"
```
## 强调

`*` 或 `_`：em 标签

*em*

`**` 或 `__`：strong 标签

**strong**
## 代码

使用 \`\`\` 表示代码

使用多个 \`\`\` 开启代码，则可以在代码区域输入 \`\`\`

## 图片

`![Alt text](url "title")`
# 其他

- 键盘：使用 `<kbd>` 标签，如 <kbd>Shift</kbd>
# Mermaid

```cardlink
url: https://mermaid.js.org/
title: "Mermaid | Diagramming and charting tool"
description: "Create diagrams and visualizations using text and code."
host: mermaid.js.org
favicon: https://mermaid.js.org/favicon.ico
```

```cardlink
url: https://mermaid.nodejs.cn/intro/
title: "关于 Mermaid | Mermaid 中文网"
description: "使用文本和代码创建图表和可视化。"
host: mermaid.nodejs.cn
favicon: https://mermaid.nodejs.cn/favicon.ico
```
# PlantUML

![[../../_resources/documents/PlantUML_Language_Reference_Guide_zh.pdf|PlantUML_Language_Reference_Guide_zh]]

```cardlink
url: https://plantuml.com/zh/guide
title: "PlantUML Language Reference Guide"
description: "You can download freely the PlantUML Language Reference Guide in PDF format."
host: plantuml.com
image: https://plantuml.com/og-guide
```
