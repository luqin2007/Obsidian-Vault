`v-on` 可以绑定 HTML 事件监听器，表示为 `v-<event>="<handler>"`，简写为 `@<event>="<handler>"`

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/v-on.html"
start: 9
end: 28
```

# 原生 DOM 对象

使用 `$event` 可以访问原生 DOM 事件对象

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/v-on-$event.html"
start: 9
end: 27
```

# 事件修饰符

针对 `event.preventDefault()`，`event.stopPropagation()` 等常用方法，Vue 为其设计了一系列事件修饰符

`````col
````col-md
flexGrow=1
===
```embed-html
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/v-on-eventmodifiers.html"
LINES: "9-26"
TITLE: "v-on-eventmodifiers.html"
```
````
````col-md
flexGrow=1
===

|事件修饰符|说明|
| ------------| --------------------------|
|`stop`|阻止点击事件|
|`prevent`|阻止默认事件行为|
|`capture`|使用事件捕获模式<sup>（对应事件先由该元素处理，再由内部元素处理）</sup>|
|`self`|事件在该元素<sup>（而不是内部元素触发）</sup>触发时才触发|
|`once`|对应的事件回调只触发一次|
|`passive`|事件的默认行为立即触发|

````
`````

多个事件修饰符之间可以串联。

```html
<a @click.stop.prevent="doThat"></a>
```

带有事件修饰符的属性也可以没有值

```html
<form @submit.prevent></form>
```
