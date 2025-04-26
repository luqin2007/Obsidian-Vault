`v-bind` 属性用于动态更新 HTML 元素属性，格式为 `v-bind:属性名`，简写为 `:`，如 `v-bind:href`、 `:href`

```embed-html
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/v-bind.html"
LINES: "9-13,15-26"
```

`v-bind` 属性与正常属性是可以共存的。如果属性只能有一个值，绑定的属性将替代原本属性，否则将添加到原属性列表中。

# 动态绑定 class

像 `class` 属性值为一个数组，`v-bind` 提供对象语法和数组语法用于简化使用

* 允许同时存在静态属性 `class` 和绑定属性 `:class`，最终属性将是二者的和
* 直接指向一个字符串、对象或数组
* 对象语法：插值表达式结果可以是一个对象。当对象中属性值为 `true` 时，对应属性名的 `class` 存在

```html title:对象语法
<!-- isActive 和 hasError 为 app 中的属性值 -->
<!-- 若对应值为 true 则存在对应 active、text-danger 属性 -->
<div :class="{ 'active': isActive, 'text-danger': hasError }">对象语法</div>
```

* 数组语法：插值表达式结果可以是一个数组。数组元素的值为字符串或对象，支持三元表达式

```html title:数组语法
<div :class="[ activeClass, errorClass ]">数组语法</div>
<div :class="[ isActive ? activeClass : '', errorClass ]">三元表达式</div>
<div :class="[ { 'active': isActive }, errorClass ]">数组嵌套对象</div>
```

---

`````col
````col-md
flexGrow=1
===
```embed-js
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/v-bind-class.html"
LINES: "18-28"
TITLE: v-bind-class.html
```
````
````col-md
flexGrow=3
===
```embed-html
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/v-bind-class.html"
LINES: "9-15"
```
````
`````

# 动态绑定 style

像 `style` 值为一个对象，可以直接绑定到一个对象，对象名使用短横线或驼峰均可正确识别。

```html
<div :style="{ 'color': activeColor, 'fontSize': fontSize + 'px' }">绑定内联样式</div>
```

```reference fold
file: "@/_resources/codes/Vue/Vue3/hellovue/v-bind-style.html"
```

绑定 `style` 也支持数组语法，数组每个值都是一个对象，结果是所有对象的集合

> [!note] 直接使用对象比较长，难以阅读，可以使用计算属性

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/v-bind-style-completed.html"
start: 9
end: 11
```

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/v-bind-style-completed.html"
lang: "js"
start: 14
end: 26
```
