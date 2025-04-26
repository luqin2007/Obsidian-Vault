Twitter 开发维护的响应式 UI 框架，大部分依赖于 CSS 的 class 属性，某些交互依赖于 js
- 排版：主要是栅格系统
- 组件：轮播、下拉按钮、模态框等新组建
- 样式：对 `<code>`，`<kbd>`，`<table>` 等组件提供新样式

# 引入

````tabs
tab: v3

```cardlink
url: https://v3.bootcss.com/getting-started/#download
title: "起步 · Bootstrap v3 中文文档 | Bootstrap 中文网"
description: "简要介绍 Bootstrap，以及如何下载、使用，还有基本模版和案例等。"
host: v3.bootcss.com
favicon: https://v3.bootcss.com/favicon.ico
```

tab: v4

```cardlink
url: https://v4.bootcss.com/docs/getting-started/download/
title: "下载 Bootstrap"
description: "下载 Bootstrap 以获得编译后的 CSS 和 JavaScript 文件、源码，或者通过你所喜欢的软件包管理器，例如 npm、RubyGems 等，将 Bootstrap 添加到你的项目中。"
host: v4.bootcss.com
favicon: https://v4.bootcss.com/docs/4.6/assets/img/favicons/favicon-32x32.png
image: https://v4.bootcss.com/docs/4.6/assets/brand/bootstrap-social.png
```

tab: v5

```cardlink
url: https://v5.bootcss.com/docs/getting-started/introduction/
title: "Bootstrap 入门"
description: "Bootstrap is a powerful, feature-packed frontend toolkit. Build anything—from prototype to production—in minutes."
host: v5.bootcss.com
favicon: https://v5.bootcss.com/docs/5.3/assets/img/favicons/favicon-32x32.png
image: https://v5.bootcss.com/docs/5.3/assets/brand/bootstrap-social.png
```

````
可以从上面网站下载 Bootstrap 包，包括用于生产环境的包（dist 后缀）及源码，或通过其他方式引用，包括但不限于 CDN，npm 等

Bootstrap 某些交互效果需要相关 JavaScript 库支持：

````tabs
tab: v3/v4 - jQuery

```cardlink
url: https://jquery.com/
title: "jQuery"
description: "jQuery: The Write Less, Do More, JavaScript Library"
host: jquery.com
```

tab: v5 - Popper

```cardlink
url: https://popper.js.org/
title: "Home"
description: "Positioning tooltips and popovers is difficult. Popper is here to help! Popper is the de facto standard to position tooltips and popovers in modern web applications."
host: popper.js.org
favicon: https://popper.js.org/favicon-32x32.png?v=e8966dab7a9bda34fd13edd5fb997ff2
image: https://popper.js.org/images/popper-og-image.jpg
```

````
可以定制需要包含的组件和样式，如颜色，默认内外边距等

```cardlink
url: https://v3.bootcss.com/customize/
title: "定制并下载 Bootstrap · Bootstrap v3 中文文档 | Bootstrap 中文网"
description: "通过自定义 Bootstrap 组件、Less 变量和 jQuery 插件，定制一份属于你自己的 Bootstrap 版本吧。此功能支持 IE9+ 或最新版本的 Safari、Chrome 或 Firefox 浏览器。"
host: v3.bootcss.com
favicon: https://v3.bootcss.com/favicon.ico
```
# 栅格

![[Pasted image 20230123161835.png]]

根据屏幕宽度不同，将父元素平均分成 12 列，为版心内每一行元素元素设置不同的块数，列间隙默认都是 `30px`

````col
```col-md
flexGrow=3
===
为不同的显示宽度选择不同的列数：`col-宽度别名-列数`
- 宽度别名：相应屏幕宽度对应的别名
- 列数：整数，范围为 $1\leq n\leq12$
```
```col-md
flexGrow=1
===

|屏幕宽度|别名|
|---|---|
|<768px|xs|
|>=768px|sm|
|>=992px|md|
|>=1200px|lg|

```
````
一行的列数可以超过 12，超过的元素将另起一行排列

```html
<div class="container" style="color: white">
  <div class="col-lg-5" style="background-color: red">01</div>
  <div class="col-lg-5" style="background-color: green">02</div>
  <!-- 从第 3 个 div 开始这一行已经超过 12 个格了 -->
  <div class="col-lg-5" style="background-color: blue">03</div>
  <div class="col-lg-5" style="background-color: yellow">04</div>
</div>
```

![[Pasted image 20230123211610.png]]
## 列偏移

使用 `col-offset` 可以将列向右移 n 个格，附加在包含 `col` 的类上：`col-宽度别名-offset-n`，`n` 为向右偏移格数

*使用 `margin-left` 实现元素向右偏移*

## 列移动

以下标签需要添加在带有 `row` 类的元素上：
- 使元素向右移动 n 格：`col-宽度别名-push-n`，通过 `right` 实现
- 使元素向左移动 n 格：`col-宽度别名-pull-n`，通过 `left` 实现

## 取消偏移

在附加有 `row` 类的元素上附加 `row-no-gutters` 类用以取消行内列之间的间隔

>[!note] 实质：设定元素 `margin-left` 和 `margin-right` 为 0

# 版心

````col
```col-md
flexGrow=3
===
`container` 为版心，居中、左右内边距 `15px`，在不同宽度的浏览器上有不同的宽度

`container-fluid` 为宽度固定为 `100%` 的版本，不常用
```
```col-md
flexGrow=1
===

| 屏幕宽度 | 容器宽度 |
| -------- | -------- |
| <768px   | 100%     |
| >=768px  | 750px    |
| >=992px  | 970px    |
| >=1200px | 1170px   |

```
````

`row` 类表示版心中没有左右缩进的一行，带有该类的元素将自带左右 -15px 的 `margin`，用于抵消 `container` 类左右 `15px` 的内边距

```html
<body>  
<div class="container" style="background-color: gray">  
  <div class="col-lg-3" style="background-color: red">01</div>  
  <div class="col-lg-3" style="background-color: green">02</div>  
  <div class="col-lg-3" style="background-color: blue">03</div>  
  <div class="col-lg-3" style="background-color: yellow">04</div>  
</div>  
<div class="container" style="background-color: gray">  
  <div class="row">  
    <div class="col-lg-3" style="background-color: red">01</div>  
    <div class="col-lg-3" style="background-color: green">02</div>  
    <div class="col-lg-3" style="background-color: blue">03</div>  
    <div class="col-lg-3" style="background-color: yellow">04</div>  
  </div>  
</div>  
</body>
```

![[Pasted image 20230123170329.png]]

**一般来说，`container` 的直接子元素都应是 `row` 类，`row` 类的所有直接子元素都应是 `col` 类**

# 情景模式

- `primary`：深蓝色
- `success`：绿色
- `info`：蓝色
- `warning`：黄色
- `danger`：红色