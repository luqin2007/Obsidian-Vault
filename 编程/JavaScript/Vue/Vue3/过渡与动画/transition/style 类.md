CSS 过渡动画的 `style` 类默认命名规则为 `<name>-<type>-<during>`
* `<name>`：动画名，默认为 `v`，由 `<transition>` 的 `name` 属性设置
* `<type>`：包括 `enter` 和 `leave`，分别对应元素的进入和离开
* `<during>`：包括 `from`，`to` 和 `active`。
    * `from`：在元素插入前生效，在元素被插入的下一帧移除
    * `to`：在元素插入后生效（同时 `from` 被删除），动画播放完成后移除
    * `active`：在整个过渡期间生效，覆盖整个 `from` 和 `to` 的时间

也可以在 HTML 元素上添加 `[enter/leave]-[from/to/active]-class` 属性和 `appear-active-class` 属性手动指定 `style` 类名，常用于使用 [`animate.css`](https://animate.style/) 等三方动画库

`````col
````col-md
flexGrow=1
===
```html
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.7.2/animate.min.css"/>
<transition name="fade" appear
            :duration="{enter: 5000, leave: 5000}"
            enter-active-class="animated swing fade-enter-active"
            leave-active-class="animated shake fade-leave-active"
            appear-active-class="animated swing">
    <div v-if="show">Hello world!</div>
</transition>
```
````
````col-md
flexGrow=1
===
```css
.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
.fade-enter-active,
.fade-leave-active {
    transition: opacity 5s;
}
```
````
`````
