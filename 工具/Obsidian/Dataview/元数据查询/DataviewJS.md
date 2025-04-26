Dataview JavaScript API 提供一个包含 Dataview 变量 `dv` 的完整 JavaScript 执行环境，分为内联和插件两部分。
# DataArray

`DataArray` 是 Dataview 查询的结果，类似于扩展的数组，通过 `dv.pages()` 创建。
- 支持索引和 `for-of` 迭代、`length` 属性
- 支持 `sort`、`groupBy`、`distinct` 、`where` 等方法
- 当直接访问某个字段名时，返回每个值的字段名对应的值组成的新 `DataArray`
	- 通过 `file` 返回所有文件组成的 `DataArray`
	- 若字段对应属性是一个数组，将扁平化

> [!note]
> 通过 `dv.array(arr)` 可以将一个 JS 数组转换成 `DataArray`，`DataArray#array()` 可以转换回 JS 数组
# 来源

相当于 DQL 的 `from` 部分，选择需要查询的文件，返回一个 `DataArray`

- `dv.current()`：从当前页面开始查询
- `dv.pages(source)`：从一组页面开始查询，详见 [[DQL#from]]
	- `dv.pages()`：从所有页面查询
- `dv.pageSource(source)`：从符合查询条件的第一个页面查询

> [!warning]
> 查询目录必须包含双引号，即 `dv.pages("books/a.md")` 不起效，应使用 `dv.pages('"books/a.md"')`

- `dv.page(path)`：从某个指定路径文档查询
# 渲染
## HTML 标签

- `dv.el(element, text, [attr])`：输出一个 HTML 元素
	- `element`：元素类型，如 `<p>` 应为 `'p'`
	- `attr`：类及属性对象，如：

```js
{
  // class 属性
  cls: "dataview dataview-class",
  // 其他属性
  attr: {
      alt: "Nice!"
  }
}
```

``````col
`````col-md
flexGrow=2
===
````
```dataviewjs
dv.el("b", "This is some text", {
  cls: "dataview dataview-class",
  attr: { alt: "Nice!" }
})
```
````
`````
`````col-md
flexGrow=1
===
```dataviewjs
dv.el("b", "This is some text", {
  cls: "dataview dataview-class",
  attr: { alt: "Nice!" }
})
```
`````
``````

- `dv.header(level, text)`：输出 1-6 级标题

``````col
`````col-md
flexGrow=2
===
````
```dataviewjs
dv.header(1, "Big!")
dv.header(6, "Small!")
```
````
`````
`````col-md
flexGrow=1
===
```dataviewjs
dv.header(1, "Big!")
dv.header(6, "Small!")
```
`````
``````

- `dv.paragraph(text)`：输出 `<p>text</p>`
- `dv.span(text)`：输出 `<span>text</span>`
## 动态查询

- `dv.execute(source)`：输出指定 DQL 查询
	- `dv.executeJs(source)`：输出指定 DataviewJS 查询
- `await dv.view(path, input)`：异步加载指定 JS 文件，并传入 `dv` 和 `input`
	- 若要导入 CSS 文件，应传递一个路径，路径中包含 `view.js` 和 `view.css`
	- 目录不应以 `.` 开头

``````col
`````col-md
flexGrow=1.5
===
````
```dataviewjs
await dv.view("views/custom", {})
```
````
`````
`````col-md
flexGrow=1
===
```dirtree
- /views
  - custom
    - view.js
    - view.css
```
`````
`````col-md
flexGrow=1
===
```dirtree
- /views
  - custom.js
```
`````
``````

``````col
`````col-md
flexGrow=2
===
````
```dataviewjs
let path = "_resources/codes/Dataview/test"
await dv.view(path, {
  arg1: 'a',
  arg2: 'b'
})
```
````
`````
`````col-md
flexGrow=1
===
```dataviewjs
let path = "_resources/codes/Dataview/test"
await dv.view(path, {
  arg1: 'a',
  arg2: 'b'
})
```
`````
``````
## Dataview 视图

- `dv.list(elements)`：将一个数组或 DataArray 作为 [[DQL#list|LIST]] 导入
	- `dv.markdownList`：等效 `dv.list`，并返回 Markdown 文本
- `dv.taskList(tasks)`：将一个数组或 DataArray 作为 [[DQL#task|TASK]] 导入
	- `dv.markdownTaskList`：等效 `dv.taskList`，并返回 Markdown 文本
- `dv.table(headers, elements)`：将一个数组或 DataArray 作为 [[DQL#table|TABLE]] 导入
	- `dv.markdownTable`：等效 `dv.table`，并返回 Markdown 文本

``````col
`````col-md
flexGrow=1
===
````
```dataviewjs
dv.table(
    ["Col1", "Col2", "Col3"],
    [
        ["Row1", "Dummy", "Dummy"],
        ["Row2", 
            ["Bullet1",
             "Bullet2",
             "Bullet3"],
         "Dummy"],
        ["Row3", "Dummy", "Dummy"]
    ]);
```
````
`````
`````col-md
flexGrow=1
===
```dataviewjs
dv.table(
    ["Col1", "Col2", "Col3"],
        [
            ["Row1", "Dummy", "Dummy"],
            ["Row2", 
                ["Bullet1",
                 "Bullet2",
                 "Bullet3"],
             "Dummy"],
            ["Row3", "Dummy", "Dummy"]
        ]
    );
```
`````
``````
# 文件 IO

- `await dv.io.csv(path, [origin-file])`：读取 CSV，否则返回 `undefined`
	- `path` 支持文件、链接或字符串
	- 使用相对路径时，相对于 `origin-file` 或当前文件
- `await dv.io.load(path, [origin-file])`：读取纯文本文件内容
	- `path` 支持文件、链接或字符串
	- 使用相对路径时，相对于 `origin-file` 或当前文件
- `dv.io.normalize(path, [origin-file])`：将相对路径转换为绝对路径
	- `path` 支持文件、链接或字符串
	- 使用相对路径时，相对于 `origin-file` 或当前文件
# 评估

- `await dv.query(source, [file, settings])`：执行查询并返回查询结果对象
	- `await dv.tryQuery(source, [file, settings])`：执行失败会产生 JS 异常而不是结果对象
	- `await dv.queryMarkdown(source, [file, settings])`：查询结果返回 Markdown

```
// { successful: true, value: { type: "list", values: [value1, ...] } }
await dv.query("LIST FROM #tag")
```

- `dv.evaluate(exp, [context])`：计算表达式，`this` 指向当前文件
	- `dv.tryEvaluate(exp, [context])`：计算失败引发 JS 异常
# 工具

- `dv.array(value)`：将数组转换为 `DataArray`，当值仍为 `DataArray`，返回原始值
	- `dv.isArray(value)`：判断某个值是否为数组或 `DataArray`
- `dv.fileLink(path, [embed?], [display?])`：将文本转换为文件路径
- `dv.sectionLink(path, section, [embed?], [display?])`：创建 `[[path#section]]` 链接
- `dv.blockLink(path, blockId, [embed?], [display?])`：创建链接到块的链接 `[[path#^blockId]]`
- `dv.date(text)`：将文本转换为日期
- `dv.duration(text)`：将文本转换为时间段
- `dv.compare(a, b)`：比较 `a` 与 `b` 的大小，返回 `a > b ? 1 : a < b ? -1 : 0`
- `dv.equal(a, b)` ：比较 `a` 与 `b` 是否相同
- `dv.clone(value)`：深度克隆 Dataview 对象
- `dv.parse(value)`：将字符串解析为 Dataview 对象