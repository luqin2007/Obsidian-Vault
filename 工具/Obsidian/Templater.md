`templater` 插件可以在模板中使用更多内置函数甚至自定义 JavaScript 函数

该插件可以将已经预定义好的名为模板的特殊文档，通过补充某些细节快速生成笔记。
- 模板：包含一系列命令的文件
	- 命令以 `<%` 开头，以 `%> 结束。
	- 命令中可以调用函数，包括内置函数和用户函数。
	- 命令最终会生成一个字符串，取代 `<%%>` 区域。

安装 `templater` 后，我们可以在插件设置中看到他丰富的设置选项。从这里我们可以简单的看出，这款插件至少支持自定义模板内部变量，默认模板，快捷键，自定义函数等多重功能。

```tabs
tab: Settings1
![[Pasted image 20221206160005.png]]
tab: Settings2
![[Pasted image 20221206160038.png]]
```
# 基础模板

在使用之前，我们最好在设置里为插件指定一个专门的目录，在插件设置中的 `template folder location`  中填好我们的目录即可。
![[Pasted image 20221206163156.png]]

之后在模板目录里创建一个笔记，使用 `<% %>` 包围你要的自定义内容。他提供的内置自定义函数比 Obsidian 自带的模板要丰富很多，以 `tp` 开头，包括：

- tp.config：包含 `templater` 插件的配置信息
	- tp.config.template_file：使用的模板文件，返回一个 [[TFile]]
	- tp.config.target_file：模板插入的目标文件，返回一个 [[TFile]]
	- tp.config.run_mode：调用 `templater` 的方式，返回的是一个 `number`
	- tp.config.active_file：调用模板时的活动文件，返回一个[[TFile]]或者 `null`
- tp.date：日期相关
	- tp.date.now：今天
	- tp.date.tomorrow：明天
	- tp.date.yesterday：昨天
	- tp.date.weekday：工作日
- tp.file：文件相关
	- 类型相关
		- tp.file.find_tfile：将字符串转换为 `TFile` 类型
	- 文件信息
		- tp.file.content：返回当前文件内容
		- tp.file.title：返回文件标题
		- tp.file.tags：返回文件的 `tag`，返回值为 `string[]`
		- tp.file.creation_date：返回文件的创建日期
		- tp.file.last_modified_date：返回文件最后一次修改时间
		- tp.file.exists：查询文件是否存在
		- tp.file.path：返回文件路径
		- tp.file.folder：返回文件所在目录
		- tp.file.selection：返回活动文件中被选中的内容
		- tp.file.include：返回指定文件中的链接
	- `cursor` 
		- tp.file.cursor：在当前位置之后插入一个 `cursor` ，可通过快捷键快速导航
		- tp.file.cursor_append：在当前活动 `cursor` 之后插入内容
	- 文件操作
		- tp.file.create_new：使用特定内容或模板创建新文件
		- tp.file.functions.move：移动文件
		- tp.file.rename：重命名文件
- tp.frontmatter
- tp.obsidian：Obsidian 内置的方法和类
	- 文档：[obsidian-api/obsidian.d.ts at master · obsidianmd/obsidian-api (github.com)](https://github.com/obsidianmd/obsidian-api/blob/master/obsidian.d.ts)
- tp.system：系统输入输出
	- tp.system.clipboard：返回剪贴板内容
	- tp.system.prompt：弹出一个输入框，返回用户输入内容
	- tp.system.suggester：弹出一个输入框，该输入框包含一系列可选内容，返回用户输入/选择内容
- tp.web：提供了两个依赖于网络的功能
	- tp.web.daily_quote：向 `api.quotable.io` 请求内容
	- tp.web.random_picture：向 `unsplash.com` 请求内容

若模板发生错误，可通过 `ctrl+shift+I` 打开控制台查看问题出在哪。

`<% %>` 中实际上是一条 JavaScript 语句，因此里面也能用其他任何 JavaScript 语句支持的写法（仅一条），包括 `await`，但不支持 `if` （支持 ?: 运算）

一个实例可以看 [[template_sample]]，由该模板生成的笔记位是 [[Untitled]]

# 嵌入 JavaScript

## 多行语句

有些时候，一行语句可能无法实现要求。我们可以在模板中使用 JavaScript 语句

嵌入多行 JavaScript 语句通过 `<%* %>` 创建

`<%*`
var hello = "hello";
var mode = tp.config.run_mode;
var title = tp.file.title;

function log(msg) {
    console.log(msg);
}
`%>`

当语句与文档内容发生联系时也可以使用

`<%* if (tp.file.tags.contains("#todo")) { %>`
This is a todo file!
`<%* else { %>`
This is a finished file!
`<%* } %>`

## 动态语句

若文档中的内容需要动态更新，可使用 `<%+ %>` 包围，其值将在进入阅读模式时更新。

Last modified date: <%+ tp.file.last_modified_date() %>

## 空行处理

若一行没有任何输出，可在尾部使用 `-%` 可以去除之后的空行，在 `%-` 去除之前的空行

`<%* /* something */ -%>`
`<%- /* something */ -%>`

# 用户函数

## 用户脚本

在插件设置中，可以设置外部脚本的位置。

![[Pasted image 20221206174642.png]]

在脚本目录中创建的 js 文件都将被加载，并将其导出函数和变量挂载在 `tp.user` 下。自定义脚本中，我们可以访问到全局命名空间中的变量 `app`，`moment`，但不能访问模板文件中的变量`tp`和`tR`。

```javascript
function my_function (msg) {
    return `Message from my script: ${msg}`;
}
module.exports = my_function;
```

之后便可以在插件设置中找到我们的自定义脚本内容，并在文档中使用

![[Pasted image 20221206175054.png]]

`<%+ tp.user.my_script("Hello World!") %>`

## 系统命令

系统命令是指，允许某些用户自定义函数调用 `cmd` 或 `bash` 脚本等。

首先，在插件设置中开启系统命令，创建对应函数名并关联到命令行脚本。这些函数在 `tp.user` 中可访问：

![[Pasted image 20221206175848.png]]

向系统函数传参，可以传入一个对象如 `tp.user.echo({a:'value1', b: 'value2'})`，这样在对应语句中可以通过形如 `$a`，`$b` 等形式访问。

系统命令中也可以使用类似 `<% %>` 的形式调用内部函数，在执行系统命令之前，将先调用内部函数并替换其结果。
# 参考

```cardlink
url: https://github.com/SilentVoid13/Templater
title: "GitHub - SilentVoid13/Templater: A template plugin for obsidian"
description: "A template plugin for obsidian. Contribute to SilentVoid13/Templater development by creating an account on GitHub."
host: github.com
favicon: https://github.githubassets.com/favicons/favicon.svg
image: https://opengraph.githubassets.com/b781d300dbb426b3895d5fd09499bd3ab8c5bd2d07e1d57783aa2e8ee6d0dbb1/SilentVoid13/Templater
```
