将现存样式表应用于当前样式表，在选择器后加一个括号，像函数调用一样即可。

`````col
````col-md
```less
.mixin_a, #mixin_b {  
  color: red;  
}  
  
.mixin_c {  
  font-size: small;  
}  
  
.mixin_all {  
  #mixin_b();  
  .mixin_c();  
}
```
````
````col-md
```css
.mixin_a,  
#mixin_b {  
  color: red;  
}  
.mixin_c {  
  font-size: small;  
}  
.mixin_all {  
  color: red;  
  font-size: small;  
}
```
````
`````

## 括号样式

样式只用于 Mixin，不作为独立的 CSS 样式，可在其后面增加一个 `()`：

`````col
````col-md
```less
.mixin_a {  
  color: red;  
}  
  
.mixin_b() {  
  font-size: small;  
}  
  
.mixin_all {  
  .mixin_a();  
  .mixin_b();  
}
```
````
````col-md
```css
.mixin_a {  
  color: red;  
}  
/* 不会出现 .mixin_b */
.mixin_all {  
  color: red;  
  font-size: small;  
}
```
````
`````
## Mixin 选择器

Mixin 也可以包含选择器等其他 Less 内容

`````col
````col-md
```less
.my_button_mixin() {  
  border: 1px dot-dash black;  
  &:hover {  
    border: 1px solid red;  
  }  
}  
  
button {  
  color: blue;  
  .my_button_mixin();  
}
```
````
````col-md
```css
button {  
  color: blue;  
  border: 1px dot-dash black;  
}  
button:hover {  
  border: 1px solid red;  
}
```
````
`````
# 命名空间

Less 支持嵌套，可以像命名空间那样组织和管理 Mixin

`````col
````col-md
```less
#outer() {  
  .inner_red() {  
    color: red;  
  }  
  
  .inner_blue() {  
    color: blue;  
  }  
}  
  
div {  
  #outer.inner_red();  
}
```
````
````col-md
```css
div {  
  color: red;  
}
```
````
`````
# 条件

可实现仅当结果为 true 时，Mixin 生效

```Less
#namespace when (@mode = huge /* 某些条件 */) {  
  color: red;  
}  
  
#namespace {  
  .mixin() when (@mode = huge /* 某些条件 */) {  
    color: blue;  
  }  
}
```

## 类型判断

- 类型：`isnumber`，`iscolor`，`isstring`，`iskeyword`，`isurl`
- 单位：`ispixel`，`ispercentage`，`isem`，`isunit`
## 逻辑运算

- `and`：与关系
- `,`：或关系
- `not`：非关系

```Less
// @a 是在 () 中定义的一个变量
.mixin(@a) when (isnumber(@a)) and (@a > 0) {
  // ...
}
```

## 默认条件

`default()` 条件始终为 `true`，用于创建一个互斥的条件

```Less
#sp_1 when (default()) {
  #sp_2 when (default()) {
    .mixin() when not(default()) { /* */ }
  }
}
```
## if

使用 `if` 代替 `when`

```Less
@dr: if(@my-option = true, {  
  button {  
    color: white;  
  }  
  a {  
    color: blue;  
  }  
});  
  
@my-option: true;  
  
@dr();
```

相当于：

```Less
button when(@my-option = true) {  
  color: white;  
}  
  
a when(@my-option = true) {  
  color: blue;  
}
```

编译成 CSS 后：

```CSS
button {  
  color: white;  
}  
a {  
  color: blue;  
}
```

# 参数化

可以向 `()` 中添加其他的属性。相当于写在作用域中

`````col
````col-md
```less
.border_radius(@radius) {  
  -webkit-border-radius: @radius;  
  -moz-border-radius: @radius;  
  border-radius: @radius;  
}  
  
#header {  
  .border_radius(3px);  
}
```
````
````col-md
```css
#header {  
  -webkit-border-radius: 3px;  
  -moz-border-radius: 3px;  
  border-radius: 3px;  
}
```
````
`````

参数化的属性可以带有默认值：

```Less
.foo(@bg: #f5f5f5; @color: #900) {
  background: @bg;
  color: @color;
}
```

相当于：

```Less
.foo() {
  @bg: #f5f5f5;
  @color: #900;
  background: @bg;
  color: @color;
}
```

- 参数之间使用逗号 `,` 或分号 `;` 分隔。
	- 一般来说，使用 `,` 即可，除非某些属性本身包含 `,`
	- 使用命名参数时，使用 `;` 分隔：`.mixin(@color, @margin);`，调用时 `.mixin(@color: red; @margin: 5px);`
- 参数化后函数，允许重载（同名不同参数）
## @arguments

使用 `@arguments` 变量可直接引用所有参数，参数之间以空格分隔

```Less
.box-shadow(@x: 0, @y: 0, @blur: 1px, @color: #000) {  
  -webkit-box-shadow: @arguments;  
  -moz-box-shadow: @arguments;  
  box-shadow: @arguments;  
}  
  
.big-block {  
  .box-shadow(2px, 5px);  
}
```

编译成 CSS 后：

```CSS
.big-block {  
  -webkit-box-shadow: 2px 5px 1px #000;  
  -moz-box-shadow: 2px 5px 1px #000;  
  box-shadow: 2px 5px 1px #000;  
}
```

## @rest

使用 `...` 可以为 Mixin 增加不定长变量，使用 `@rest` 可以引用所有 `...` 属性

```Less
.mixin(...) {}
.mixin(@a: 1, ...) {}
.mixin(@a: 1, @rest...) {}
```

# !important

在 Mixin 后添加 `!important`，可以使混合的所有属性都增添 `!important` 标记

`````col
````col-md
```less
.foo(@bg: #f5f5f5; @color: #900) {  
  background: @bg;  
  color: @color;  
}  
  
.unimportant {  
  .foo();  
}  
  
.important {  
  .foo() !important;  
}
```
````
````col-md
```css
.unimportant {  
  background: #f5f5f5;  
  color: #900;  
}  
.important {  
  background: #f5f5f5 !important;  
  color: #900 !important;  
}
```
````
`````

# 模式匹配

模式匹配允许根据传入的变量选择 Mixin，所有可以匹配的项都可以被混入

```Less
// #1
.mixin_select(dark, @color) {  
  color: darken(@color, 10%);  
}  
// #2
.mixin_select(light, @color) {  
  color: lighten(@color, 10%);  
}  
// #3
.mixin_select(@_, @color) {  
  background-color: @color;  
}  
// #4
.mixin_select(@color) {  
  display: block;  
}  

@switch: light;  
.class {  
  .mixin_select(@switch, #888);  
}
```

编译成 CSS 后：

```CSS
/* 四个 .mixin_select 中 #2, #3 被混入 */
.class {  
  color: #a2a2a2;  
  background-color: #888;  
}
```

# 属性引用

通过属性引用，我们可以像函数一样使用 Mixin：

```Less
.average(@x, @y) {  
  @result: ((@x + @y) / 2);  
}  
  
div {  
  padding: .average(10px, 8px)[@result];  
}
```

编译成 CSS 后：

```CSS
div {  
  /* .average 调用后引用 @result 属性 */
  padding: 9px;  
}
```

若无参数化，可省略 `()`；若只有一个变量，则可省略变量名

```Less
.average(@x, @y) {  
  @result: ((@x + @y) / 2);  
}  
  
.values() {  
  @v: 12px;  
}  
  
div {  
  width: .values[];  
  // webstorm 显示错误，但 lessc 可编译
  padding: .average(10px, 8px)[];  
}
```

编译成 CSS 后：

```CSS
div {  
  width: 12px;  
  padding: 9px;  
}
```

# 递归

Less 支持 Mixin 的递归调用，配合 `when`，可以实现很多类似于循环的效果：

```Less
.generate-columns(@n, @i: 1) when (@i <= @n) {  
  .column-@{i} {  
    width: (@i * 100% / @n);  
  }  
  // 递归调用
  .generate-columns(@n, @i + 1);  
}  

// 4, 1
// 4, 2
// 4, 3
// 4, 4
.generate-columns(4);
```

编译成 CSS 后：

```CSS
.column-1 {  
  width: 25%;  
}  
.column-2 {  
  width: 50%;  
}  
.column-3 {  
  width: 75%;  
}  
.column-4 {  
  width: 100%;  
}
```

# 别名

可以将一个 mixin 赋值给一个变量作为其别名

```Less
#theme.navbar {  
  .color(light) {  
    primary: purple;  
    secondary: lightgray;  
  }  
  
  .color(dark) {  
    primary: black;  
    secondary: gray;  
  }  
  
  .set_color() {  
    color: red;  
  }  
}  
  
.navbar {  
  @colors: #theme.navbar.color(dark);  
  @set-colors: #theme.navbar.set_color();  
  
  background: @colors[primary];  
  border: 1px solid @colors[secondary];  
  @set-colors();  
}
```

编译成 CSS 后：

```CSS
.navbar {  
  background: black;  
  border: 1px solid gray;  
  color: red;  
}
```

