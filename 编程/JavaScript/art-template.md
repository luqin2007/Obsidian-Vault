一个高性能模板引擎，支持 NodeJS 和浏览器，性能接近 JavaScript 极限性能
# 模板方法

## 实时编译

`let template = require('art-template')`
- 核心方法
	- `template(path)`：编译并返回用于生成模板的函数
	- `template(data, path)`：编译并返回根据模板生成的代码
	- `template.compile(source, options)`：编译并返回用于生成模板的函数
	- `template.render(source, data, options)`：编译并返回根据模板生成的代码
- 参数说明
	- `path`：模板文件路径
		- NodeJS：通过文件路径获取模板
		- 浏览器：通过 `document.getElementById(path).innerHTML` 获取模板
	- `data`：模板对象，用于替换对应占位符
	- `options`：选项

> [!attention] 为防止浏览器中模板被渲染，可将模板放入 `<script type="text/html">` 中

## 预编译与插件

- [[../../工具/Node.js/webpack|webpack]]：`art-template-loader`
- [[Express]]：`express-art-template`
- [[Koa]]：`koa-art-template`
 
# 语法

> [!note] 原文输出：不经 HTML 转义，直接输出内容，可用于输出 HTML 结构，但可能存在安全风险

> [!note] 模板继承：块级的模板内容替换，允许替换两端之间的所有内容

> [!note] 过滤器：一种特殊函数
> - 通过 `template.defaults.imports.过滤器名 = 过滤器函数` 定义
> - 过滤器函数定义为 `(value[, params]) => value`

## 标准语法

更易读写的语法，支持基本模板语法和基本 JavaScript 表达式

### 基本语法

- 使用 `{{ }}` 包围模板内容，变量和普通表达式可直接引用
- 使用 `{{ set 变量名=变量值 }}` 声明变量
- `if` 结构：`{{if condition}} ... {{else if condition2}} ... {{/if}}`
- 循环结构：`{{each target}} ... {{/each}}`，其中循环体中可使用 `{{$index}}`，`{{$value}}` 表示索引和值
	- `target` 支持数组和对象
	- 默认循环值，`{{each target val key}}` 可使 `$value` 表示键

### 模板扩展

- 使用 `{{extend 'path'}}`  和 `{{block 'name'}} {{/block}}` 表示模板继承
- 使用 `{{@ ... }}` 表示按原文输出
- 使用 `{{ include 'path' [data] }}` 表示引用子模板
- 使用 `{{ value | 过滤器1 | 过滤器2 | ... }}` 表示使用过滤器， `|` 类似管道运算符
	- 可以带参数，为 `{{ value | 过滤器 其他参数 }}` 形式使用

## 原始语法

更强大的逻辑表达能力，支持 EJS，Underscore，LoDash 模板，支持任意 JavaScript 表达式

### 基本语法

- 使用 `<%= .. %>` 可用于。直接输出变量或简单 JavaScript 表达式
- 使用 `<% .. %>` 表示块模板代码，其内容支持 JavaScript 块语法，即带有 `{}` 的结构，如 `if`，`for` 等
	- `<% if (condition) { %> ... <% } else if (condition2) { %> ... <% } %>`
	- `<% for (int i = 0; i < v.length; ++i) { %> ... <% } %>`
- 使用 `<%- .. %>` 表示原文输出

### 模板扩展

- 模板继承：`<% extend('path') %>` ， `<% block('head', function() { %> ... <% } %>`
- 子模板：`<% include('path'[, data]) %>`
- 过滤器：没有类似 `|` 的运算符，直接调用同名函数即可：
	- `<%= $imports.过滤器名(value, ...) %>`

## 内建变量与函数

- `$data`：传入的模板数据
- `$imports`：外部导入的变量和全局变量，在 `template.defaults.imports` 中设定
- `print`：字符串输出函数
- `include`：子模板导入函数
- `extend`：模板继承函数
- `block`：模板快声明函数

# 配置

[选项 - art-template (aui.github.io)](http://aui.github.io/art-template/zh-cn/docs/options.html)

默认配置位于 `template.defaults` 对象中

## 全局变量

在 `template.defaults.imports` 中设定

## 自定义解析

[解析规则 - art-template (aui.github.io)](http://aui.github.io/art-template/zh-cn/docs/rules.html)

## 调试

能捕获语法和运行错误，并支持自定义语法

`template.defaults.debug = true`

## 压缩

通过 `template.defaults.minimize = true` 可压缩 HTML，CSS，JS，具体设置包括：

```javascript
template.defaults.htmlMinifierOptions = {
	collapseWhitespace: true,
    minifyCSS: true,
    minifyJS: true,
    // 运行时自动合并：rules.map(rule => rule.test)
    ignoreCustomFragments: []
};
```
# 参考

```cardlink
url: http://aui.github.io/art-template/zh-cn/index.html
title: "art-template"
description: "高性能 JavaScript 模板引擎"
host: aui.github.io
```
