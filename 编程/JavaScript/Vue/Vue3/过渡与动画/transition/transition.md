使用 `<transition>` 过渡封装组件包裹要实现过渡效果的组件，包括 Vue 组件和原生 HTML 元素，可以方便设置插入、更新或移除 DOM 时的过渡效果。

```html
<transition [name="<过渡名称>"]>
    ...
</transition>
```

> [!note] `<transition>` 支持 `v-if`、`<component>` 切换元素

CSS 过渡动画通过添加和删除 [[style 类]]控制动画的播放与停止。

`````col
````col-md
flexGrow=1
===
```embed-html
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/transition.html"
LINES: "20-29"
```
````
````col-md
flexGrow=1
===
```embed-html
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/transition.html"
LINES: "7-17"
```
````
`````

> [!note] `<transition>` 可以添加 `appear` 属性，在 DOM 加载完成后立即播放动画

```html
<transition appear> ... </transition>
```

# 过渡动画

`<transition>` 允许同时使用 CSS 过渡动画（包括 [[CSS 过渡]]、[[CSS 动画]]）和 [[JS 动画]]。

> [!important] `<transition>` 同时使用过渡和动画时，需要设置播放动画的时间：

* 使用 `type` 指定使用 `animation` 还是 `transition` 时间作为动画时间

```html
<transition type="transition"> ... </transition>
```

* 使用 `duration` 属性设置动画时间，单位 `ms`

```html
<transition :duration="{enter: 5000, leave: 5000}"> ... </transition>
```

# 多元素动画

当有多个元素同时播放动画时，`<transition>` 默认 `enter` 和 `leave` 动画同时播放。可以通过 `mode` 属性修改：

* `in-out`：先进入后退出
* `out-in`：先退出后进入

```html
<transition mode="in-out"> ... </transition>
```

多个元素切换时，如果没有涉及到 DOM 变化的话需要加 `:key` 属性绑定到一个属性，使 Vue 为相同元素生成多个实例，否则不会触发切换动画

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/transition-multi-el.html"
start: 24
end: 26
```

`<component>` 变更 `is` 属性不需要加 `:key`

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/transition-multi-component.html"
start: 21
end: 26
```
