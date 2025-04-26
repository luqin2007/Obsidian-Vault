Cascading Style Sheets，层叠样式表，负责网页的样式，

# 引入

- 外联式：通过 `<link>` 标签，引入单独的 css 文件

```HTML
<link rel="stylesheet" href="*.css*">
```

- 内嵌式：在 `<style>` 标签内直接写 CSS，常位于 `<head>` 中。
	- type：默认 `text/css`，样式表 MIME 类型

```HTML
<style>
    /* 在这里写 CSS 代码 */
</style>
```

- 行内式：直接写在标签的 `style` 属性中

```HTML
<p style="color: red;"></p>
```

**优先级：行内式>内嵌式>外联式**

# 语法

CSS 样式中使用 `选择器 { 属性: 值; }` 的形式

`````col
````col-md
```CSS
选择器 {
    属性1: 值1;
    属性2: 值2;
    属性3: 值3;
}
```
````
````col-md
```CSS
p {
  color: red;
}
```
````
`````
- [[选择器/选择器|选择器]]：选择要修饰的内容
- 属性：用于调整样式
	- 通用：[[文本/文本|文本]]，[[背景]]，[[盒模型]]，[[布局/浮动|浮动]]，[[布局/定位|定位]]，[[剪裁]]，[[修饰]]，[[动画/动画|动画]]，[[变换]]，[[多媒体]]
	- 移动端，主要是各种布局：[[布局/弹性布局|弹性布局]]，[[移动适配]]，[[布局/响应式布局|响应式布局]]，[[布局/网格布局|网格布局]]

## 注释

CSS 中，使用 `/* ... */` 的形式作为注释

```CSS
/* 这是注释 */
```

## 复合属性

CSS 某些属性集成一类属性的功能，可以使用一个属性设置多个属性的参数。

> [!note] 复合属性中的每个子属性可以单独拿出来作为一个属性使用

通常复合属性各参数之间没有顺序关系，但有些属性值类型相同或有重合，会规定顺序以免引起歧义

大多数复合属性的子属性是可以省略的

## 特性


- 继承性：父元素的某些属性会自动在子元素上生效。
	- `color`，`font` 系列， `text-indent`，`text-align`， `line-height` 都可以继承
	- `<a>` 标签会覆盖 `color` 属性
	- `<h1>`，`<h2>`，`<h3>` 等会覆盖 `font-size` 属性
	- 可手动指定 `inherit` 属性应用继承

> [!note] 大部分控制文字渲染行为的可继承，而控制标签的属性不可继承

- 层叠性：对于不同位置为标签设置的样式，样式会层叠叠加到标签上
	- 当多个样式不冲突时，会自动整合到标签上
	- 当多个样式冲突时，后设置的属性会覆盖前面的属性
	- 层叠性优先级弱于选择器优先级 - 仅选择器优先级相同时，对于冲突样式才遵循层叠性顺序

## 导入

可以通过 `@import` 向当前 CSS 导入其他 CSS 文件，但该语句必须在其他语句之前
- `@import CSS地址;`
- `@import url(CSS地址);`

# 书写顺序

一个书写习惯，按一下顺序写会提高渲染速度（???）
- 浮动 / display
- 布局定位相关：position，clear，float，visibility，overflow 等
- 盒子模型，margin，border，padding，宽高，背景色
- 文字样式
- 其他 CSS 属性：content，cursor，border-radius，box-shader，text-shader 等

# 参考

```cardlink
url: https://developer.mozilla.org/zh-CN/
title: "MDN Web Docs"
description: "The MDN Web Docs site provides information about Open Web technologies including HTML, CSS, and APIs for both Web sites and progressive web apps."
host: developer.mozilla.org
favicon: https://developer.mozilla.org/favicon-48x48.bc390275e955dacb2e65.png
image: https://developer.mozilla.org/mdn-social-share.d893525a4fb5fb1f67a2.png
```
