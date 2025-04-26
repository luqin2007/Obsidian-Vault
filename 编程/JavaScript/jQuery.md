封装了用于快速获取与设置样式，调用常用 DOM 方法的代码的轻量级 JS 库
- 官网：[jQuery](https://jquery.com/)
- 版本：3.6.4
- CDN：https://code.jquery.com/jquery-3.6.4.min.js

# 入口

- 可在任意位置使用 `$` 调用 jQuery 函数
- 当需要在浏览器加载完成时调用，可以使用以下两种方式，实质是向 `document` 注册 `DOMContentLoaded` 事件：
```javscript
$(document).ready(() => { /* dosomething */ })
// 等效于
$(() => { /* dosomething */ })
```

# 查询

## jQuery 对象

使用 jQuery 查询后获取的对象。区别于直接获取的 DOM 元素，jQuery 获取的对象是一个包含 DOM 元素的伪数组对象，同时包含一系列 jQuery 方法

转化：
- DOM->jQuery：`$(dom对象)`
- jQuery->DOM：`jq对象[0]` 或 `jq对象.get(0)`

## 选择器

jQuery 使用 `$('选择器')` 通过 CSS 的选择器选择元素，返回一个 jQuery 对象

可以对 jQuery 对象使用 `parent`, `children`, `find` 等进行进一步选择

![[Pasted image 20230414110015.png]]

- `parents(selector)`：返回所有祖先元素

隐式迭代：对 jQuery对象的所有操作都会应用于所有选择器选出的元素中，相当于对 querySelectorAll 的结果进行遍历应用。

- 通过 siblings 方法，配合隐式迭代，可以快速进行排他性操作
- `index()` 方法可以获取当前元素是选择器中的第几个元素

之后的大部分操作都是针对 jQuery 对象进行操作，且支持链式调用

# 样式

## CSS 属性

- 通过 `$("...").css(name, value)` 修改元素的 CSS 样式，相当于直接修改每个元素的 `style` 对象中的对应属性
- 通过 `$("...").css({name, value, ...})` 同时修改多个样式，属性名为首字母小写的驼峰，此时属性值为数字时可以不用字符串
- 通过 `$("...").css(name)` 获取元素的 CSS 样式值

## 类

- `addClass`，`removeClass` 方法直接添加/删除类
- `toggleClass` 切换类状态（添加/删除）

## 动画

- 动画类型：
	- 收放：`show/hide/toggle([speed, easing, fn])`
		- `speed`：`'slow'`，`'normal'`，`'fast'`，`数字(ms)`
		- `easing`：效果，默认 `swing`，可选 `linear`
	- 滑动：`slideUp/slideDown/slideToggle([speed, easing, fn])`
	- 淡入淡出：`fadeIn/fadeOut/fadeTo/fadeToggle`
		- `fadeTo(speed, opacity, easing, fn)`
	- 自定义动画：`animate(params[, speed, easing, fn])`
		- `params`：属性对象，包含动画结束状态 CSS 属性
- 动画队列
	- 每次触发动画，若上次动画还未完成，新动画加入动画队列
	- 使用 `stop()` 方法可清空动画队列并进行下一次动画

# DOM

## 元素属性

- 通过 `prop(name[, value])` 获取和设置元素的固有属性，如 a 的 href，input 的 value，checked 等
- 通过 `attr(name[, value])` 获取和设置元素的自定义属性，包括任意自定义属性和 `data-` 开头的属性
	```javascript
	// <a index="3", data-index="2"></a>
	$('a').attr('index') // "3"
	$('a').attr('data-index') // "2"
	```

- 通过 `data(name[, value])` 可以向元素绑定属性（存于缓存），该属性不体现在对应 DOM 元素上
	- 也可用于获取 H5 `data-` 属性，此时可省略 `data` 前缀，且值会自动转换成数字
- `val(...)` 相当于 `value` 属性，多用于表单元素
- `html(...)` 相当于 `innerHTML`，`text(...)` 相当于 `innerText`

## 尺寸

![[Pasted image 20230415023405.png]]

括号内提供数字则为修改，否则为查询，单位 dp

## 位置

- `offset()`：元素相对于文档的位置，对象包含 `left`，`top` 属性
- `position()`：元素相对于带定位的父级元素的位置，只能获取不能设置
- `scrollTop(), scrollLeft()`：元素被卷去的高度/长度

## 遍历

- `each(function(index, dom))` 方法可用于遍历所有子元素，回调中第二个元素是 DOM 对象而非 jQuery 对象
- `$.each(object, function(index, dom))` 可用于遍历任意对象，包括元素，对象，数组等，类似于 `for-in`

## 节点

- 直接使用 `$('元素内容')` 创建元素，如 `$('<li>something</li>')`
- 通过 `append(elem)` 与 `prepend(elem)` 可将元素 `elem` 作为子元素添加到标签的尾部或头部
- 通过 `after(elem)` 与 `before(elem)` 可将元素 `elem` 作为兄弟元素插入到标签的前面或后面
- `remove()` 用于从 DOM 树中移除该元素
- `empty()` 相当于 `html('')`，可将该元素的内容清空

## 元素方法

- `<form>` 标签
	- `serialize()`：快速获取表单所有数据，需要对应输入控件包含 name 属性

# 事件

## 注册

- 通过直接调用事件同名函数，传入回调函数的形式注册事件，注意事件的 ev 对象也是 JQuery 封装的事件对象
	- `hover` 事件同时注册鼠标进入、鼠标退出事件
- 通过 `on` 方法同时绑定多个事件或委托事件
	- `on(object)`，其中 `object` 为一个对象，以事件名为 key，以事件处理函数为 value
	- `on(events, function)`，其中 `events` 为空格分隔的多个事件名，`function` 为其共用的事件处理函数
	- `on(events, selector, function)`，其中 `selector` 为子元素的选择器，实现基于冒泡的事件委托
- 通过 `one` 方法可绑定一次性事件
- 通过 `off` 可用于解绑 `on` 方法绑定的事件
	- `off()` 用于解除所有事件
	- `off(event)` 用于解除指定事件
	- `off(event, selector)` 用于解除委托事件

## 触发

- 可以直接调用事件同名函数，不传入任何参数，用于触发一次对应事件
- `trigger(event)` 可用于触发对应事件
- `triggerHandler(event)` 可用于触发对应事件，但不会触发元素的默认行为

## 事件对象

在 jQuery 中注册事件时使用的事件对象也是对原本 DOM 事件对象的一个封装：
- 具有 `preventDefault()` 和 `stopPropagation()` 方法
	- 事件处理器返回 `false` 时相当于调用了 `preventDefault()` 方法
- 通过 `target` 属性可以返回触发的第一个对象，通过 `target(n)` 返回第 n 个对象，返回的都是 DOM 对象

# Ajax

jQuery 封装的实现[[../../编程/HTML/HTML API/HTML API#Ajax|Ajax]]的简单方法，最常用的包括：
- `$.get(url, [data], [callback: function(res)])`
- `$.post(url, [data], [callback: function(res)])`
- `$.ajax({type: 'GET'|'POST', url: string, data: object, success: function()})
	- 传输带文件的表单数据，需要一些额外设置
		- `data` 属性为 `FormData` 对象
		- `contentType: false`：不修改 `Content-Type` 属性，使用 `FormData` 默认值
		- `processData: false`：不对 `FormData` 的值进行 URL 编码操作
	- `$(document).ajaxStart(function)`：当 Ajax 触发时调用，只能添加到 `document` 上
	- 可通过该方法发起 jsonp 请求：`{ dataType: jsonp }`，请求结果将被反馈在 `success` 中
		- 默认向 url 附加一个 `callback=随机回调函数名` 的查询参数，函数名以 jQuery 开头
		- 可自定义回调函数名：`{ jsonp: "回调函数参数名", jsonpCallback: "回调函数名" }`

# 其他工具

- 对象合并：`$.extend([deep=false], target, obj1, obj2...)`：将 `objN` 等对象拷贝（合并）到 `target` 上
	- `deep`：该值为 `true` 时为深拷贝
	- `target`：目标对象
	- `objN`：源对象

# 冲突

当上下文环境中存在 $ 时，可使用以下方法解决冲突：
- 使用 `jQuery` 代替 `$`
- 为任意变量赋值 `$.noConflict()` 或 `jQuery.noConflict()` 后，可用该变量代替 `$`

# 插件

- [jQuery插件库-收集最全最新最好的jQuery插件 (jq22.com)](https://www.jq22.com/)
- [jQuery之家-自由分享jQuery、html5、css3的插件库 (htmleaf.com)](http://www.htmleaf.com/)