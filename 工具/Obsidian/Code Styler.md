一个为代码块添加额外样式的插件
# 设置

比较重要的设置有：
- 排除某种语言的代码块： `Codeblock Styling` - `Codeblock Languages` - `Excluded Languages`
- 包含代码块：默认不处理其他插件的代码块，在 `Codeblock Styling` - `Codeblock Languages` - `Whitelisted Proceesed Codeblocks` 设置
- 主题及各种样式
# 代码块

代码块参数紧跟代码类型后，若有配置参数使用 `:` 或 `=` 设置：

````
```cpp fold title:CppFile ln=true
```
````

若代码块没有设置代码类型，则参数与 \` 之间存在一个空格

````
``` fold
```
````

插件还支持处理 Markdown 风格代码块参数

````
```{r title, hl=5}
```
````
## 标题

标题使用 `title` 设置

```cpp title:test.cpp
```

若标题包含空格，使用 `""` 或 `''` 包围
## 行号

- `ln:true`: 总是显示行号
- `ln:false`: 总是隐藏行号
- `ln:N`: 行号从 N 开始显示，且总是显示行号

```cpp ln:2
int main() {
    cout << "Hello world";
    return 0;
}
```
## 折叠

使用 `fold` 表示代码块默认折叠
- 若存在 `title` 参数，将使用标题为默认占位符
- 可以使用 `fold:占位符` 手动设置占位符
- 若不存在 `title`，也没有手动设置，使用默认占位符（在插件设置里可以修改）

```cpp fold
int main() {}
```
## 引用

使用 `ref:<link>` 或 `reference:<link>` 设置引用链接。设置后标题可点击，点击后跳转到对应链接。
- `<link>` 链接支持 wikilink 或 Markdown 链接
- 若不存在标题，则使用链接的标题

```cpp ref:[Baidu](https://www.baidu.com)
```
## 高亮

若要高亮某一行，使用 `hl` 参数，后接高亮行，使用 `,` 分隔，支持写法：
- `N`：高亮第 `N` 行
- `N-M`：高亮第 `N` 到 `M` 行
- `<str>`，`'str'`，`"str"`：包含指定字符串的行
- `/<reg>`：高亮给定正则匹配通过的行

```cpp hl:1,3-4,foo,'bar baz',"bar and baz",/#\w{6}/
int main() 
{
    printf("Hello World\n");
    printf("Hello World\n");
    printf("Hello World\n");
    printf("foo\n");
    printf("a:bar baz\n");
    printf("a:bar and baz\n");
    printf("Hello World\n");
    printf("#123456 is my favorite color\n");
}
```

- 插件设置中可以设置默认高亮的行
- 插件设置中可以设置渐变高亮
- 还支持 `{n,a-b,...}` 的形式设置高亮，但这种格式仅支持按行号高亮，不支持字符串和正则

```cpp {1,3-4}
int main() 
{
    printf("Hello World\n");
    printf("Hello World\n");
}
```

在插件设置中添加不同高亮样式，可以手动添加诸如 `info`、`warn`、`error` 等标记设置高亮不同颜色

![[Pasted image 20240717232032.png]]

```cpp info:1 warn:2 error:3
int main() 
{
    printf("Hello World\n");
    printf("Hello World\n");
}
```
## 折叠

> [!note]
> 折叠效果仅在阅读模式中生效

当一行代码过长时，可以使用一些折叠参数设置点击时的行为

![[Pasted image 20240718082234.png]]
- `Wrap Lines on Click`：当一行代码过长时，点击代码是否展开（自动换行）

```python
print("This line is very long and should be used as an example for how the plugin deals with wrapping and unwrapping very long lines given the choice of codeblock parameters and settings.")
```

- `unwrap`，`unwrap:true`：总是单行显示

```python unwrap
print("This line is very long and should be used as an example for how the plugin deals with wrapping and unwrapping very long lines given the choice of codeblock parameters and settings.")
```

- `unwrap:false`，`wrap`：总是多行显示，自动换行

```python unwrap:false
print("This line is very long and should be used as an example for how the plugin deals with wrapping and unwrapping very long lines given the choice of codeblock parameters and settings.")
```

- `unwrap:inactive`：点击时总是自动展开

```python unwrap:inactive
print("This line is very long and should be used as an example for how the plugin deals with wrapping and unwrapping very long lines given the choice of codeblock parameters and settings.")
```
# 内联代码

在内联代码前添加 `{}{代码类型} 代码` 可以为内联代码添加高亮：`{c++} cout << "Hello world";`
- `{代码 icon}` 可以添加图标：`{c++ icon} cout << "Hello world"` 
- `{代码 title:标题}` 可以添加标题：`{c++ title:hello} cout << "Hello world"`
- 二者可共存：`{c++ icon title:hello} cout << "Hello world"`
# 引用代码

该插件可以引用其他代码文件，使用 `reference` 类型代码块

````
```reference
file: "@/_resources/codes..."
lang: "python"
```
````

块内使用 YAML 语法，包含以下参数：
- `file`、`link` 或 `fileLink`：指定代码文件或链接
	- 使用 ` @/ ` 开头表示笔记目录
	- URL 地址，代码将缓存至 `.obsidian/plugins/code-styler/reference-files`
	- wikilink 引用：`[[ ... ]]`
- `language` 或 `lang`：代码语言
- `start` 和 `end`：起止行

# 参考

```cardlink
url: https://github.com/mayurankv/Obsidian-Code-Styler
title: "GitHub - mayurankv/Obsidian-Code-Styler: A plugin for Obsidian.md for styling codeblocks and inline code"
description: "A plugin for Obsidian.md for styling codeblocks and inline code - mayurankv/Obsidian-Code-Styler"
host: github.com
favicon: https://github.githubassets.com/favicons/favicon.svg
image: https://opengraph.githubassets.com/29d56bd880ff6a3597d8e4ee75a11cb02147121732734dc01807cfed3194c25a/mayurankv/Obsidian-Code-Styler
```
