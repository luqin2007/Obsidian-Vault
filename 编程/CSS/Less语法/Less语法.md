Leaner Style Sheets，是一种向后兼容的 CSS 扩展语言。
# 使用

Less 有一套配合的编译工具 `less.js`，通过 `npm` 安装，常安装于全局：

```shell
npm install less -g
```

`lessc` 工具用于编译 `less` 文件到 `CSS`

```shell
lessc [option option=parameter ...] <source> [description]
```
- option：参数
	- --silent：控制台中不显示任何警告信息
	- --version，-v：显示当前 `less.js` 版本
	- --help，-h：显示所有参数帮助信息
	- --depends，-M：显示依赖列表
- source：源文件或 `-` 表示从控制台读入文件
- description：输出文件，默认为当前目录同名 .css 文件

若不需要 less 导出成 css，在文件开头添加 `// out:false` 即可，常用于需要 `@import` 到其他文件中的公共样式：

```Less
// out:false
```

浏览器环境中可以直接使用 Less，包括 IE 在内的几乎所有浏览器和移动端浏览器都支持

```HTML
<html>
<head>
    <link rel="stylesheet/less" type="text/css" href="css/styles.less"/>  
    <script src="https://cdn.jsdelivr.net/npm/less@4"></script>
</head>
</html>
```

1. [[运算]]
2. [[变量]]
3. [[选择器]]
4. [[合并]]
5. [[Mixin]]
6. [[解绑规则]]
7. [[@import]]
8. [[@plugin]]
# 注释

除了 CSS 中的 `/* 注释内容 */` 样式的多行注释外，还可使用单行注释：`// 注释内容`
# 参考

```cardlink
url: https://less.bootcss.com/
title: "Less 快速入门 | Less.js 中文文档 - Less 中文网"
description: "Less 扩充了 CSS 语言，增加了诸如变量、混合（mixin）、运算、函数等功能。 Less 既可以运行在服务器端（Node.js 和 Rhino 平台）也可以运行在客户端（浏览器）。"
host: less.bootcss.com
```

```cardlink
url: https://less.bootcss.com/functions/
title: "Less 函数 | Less.js 中文文档 - Less 中文网"
description: "Less 扩充了 CSS 语言，增加了诸如变量、混合（mixin）、运算、函数等功能。 Less 既可以运行在服务器端（Node.js 和 Rhino 平台）也可以运行在客户端（浏览器）。"
host: less.bootcss.com
```

```cardlink
url: https://less.bootcss.com/tools/
title: "Less 相关工具 | Less.js 中文文档 - Less 中文网"
description: "Less 扩充了 CSS 语言，增加了诸如变量、混合（mixin）、运算、函数等功能。 Less 既可以运行在服务器端（Node.js 和 Rhino 平台）也可以运行在客户端（浏览器）。"
host: less.bootcss.com
```
