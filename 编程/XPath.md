XPath 是一种用于在 XML 文档中导航和索引数据的语言

```XML title:测试数据
<bookstore>
    <book>
        <title lang="eng">Harry Potter</title>
        <price>29.99</price>
    </book>
    
    <book>
        <title lang="eng">Learning XML</title>
        <price>39.99</price>
    </book>
</bookstore>
```

> [!note] 节点/项目：组成 XML 文档的组分，包括元素，属性，文本，命名空间，处理指令，注释，文档
> XML 文档被称为节点树的根节点或文档节点

> [!note] 基本值/原子值：根节点或叶节点
# 语法
## 节点

| 表达式        | 描述                            |
| ---------- | ----------------------------- |
| `nodename` | 单独出现时选取 `<nodename>` 节点的所有子节点 |
| `/`        | 根节点                           |
| `//`       | 无视位置选择所有节点                    |
| `.`        | 选择当前节点                        |
| `..`       | 选择父节点                         |
| `@`        | 按属性选择节点                       |
| `text()`   | 节点内容文本                        |
> [!example]
> - `bookstore`：选择 `bookstore` 元素的所有子节点，即两个 `book` 节点
> - `/bookstore`：选择根节点的 `bookstore` 子节点的所有子节点
> - `bookstore/book`：选取 `bookstore` 节点的所有 `book` 子节点
> - `//book`：选取所有 `book` 节点，无视位置
> - `bookstore//book`：选取 `bookstore` 节点下的所有 `book` 节点，不一定是直接子节点
> - `@lang`：选择所有 `lang` 属性节点
## 谓词

使用 `[]` 添加谓词判断
- `[n]`：选择第 n 个元素；支持简单运算 `+`，`-` 等
	- `last()`：最后一个
	- 索引从 1 开始
- 数值比较：`>`，`<`，`=` 等
	- `position()`：元素所在位置
	- `@属性名`：存在属性且引用属性值
	- `节点名`：选取节点值

> [!example] 
> - `/bookstore/book[1]`：选取 `/bookstore/book` 选择的元素中的第一个
> - `/bookstore/book[last()]`：选取 `/bookstore/book` 选择的元素中的最后一个
> - `/bookstore/book[last()-1]`：选取 `/bookstore/book` 选择的元素中的倒数第二个
> - `/bookstore/book[position()<3]`：选取 `/bookstore/book` 选择的元素中的前两个
> - `/bookstore/book[price>35]`：选取 `/bookstore/book` 选择的元素中 `price` 子元素值大于 35 的
> - `//title[@lang]`：选取 `//title` 中带有 `lang` 属性的
> - `//title[@lang='eng']`：选取 `//title` 中带有 `lang` 属性，且属性为 `eng` 的
## 通配符

- `*`：任意结点
- `@*`：任意带有属性的节点
- `node()`：任意类型节点
## 轴

根据当前节点选择其他节点

![[Pasted image 20230129225526.png]]
# 运算符

- 节点集：`|` 用于合并多个节点集
- 数值运算：`+`，`-`，`*`，`div`，`mod`
- 比较运算：`=`，`!=`，`<`，`<=`，`>`，`>=`
- 逻辑运算：`and`，`or`，函数 `not(bool)`
# 函数

[XPath、XQuery 以及 XSLT 函数 | 菜鸟教程 (runoob.com)](https://www.runoob.com/xpath/xpath-functions.html)

