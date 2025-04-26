组合 API 可以将组件配置逻辑转移到 app 或组件的 `setup(props, context)` 函数中。在该函数中执行的操作可用的 API 称为组合式 API

> [!warning] `setup` 在组件创建之前调用，因此无法访问状态、计算属性、方法等，无法使用 `this`

组合式 API 中将大量使用<span data-type="text" parent-style="color: var(--b3-card-info-color);background-color: var(--b3-card-info-background);">响应式属性</span>，用于替代 `data`

```js
Vue.create({
    data()  { return { ... } },
    setup(props, context) { .... }
})
```

---

- [[生命周期函数]]
- [[组件链传递]]
- [[模板引用]]
- [[计算属性]]
- [[响应式侦听]]
- [[自定义事件]]

---

# 参数

* `props`：通过 HTML 标签传入的属性

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/setup-proxy.html"
lang: "js"
start: 15
end: 26
```

> [!attention] `props` 是响应式参数，故不能通过 ES6 解构（将丧失响应性）。如果需要解构则使用 `Vue.toRefs(props)`
  
* `context`：当前上下文对象，包括 `{ attrs, slots, emit, expose }` 四个属性
    * `attrs`：等同于 `$attrs`，所有属性
    * `slots`：等同于 `$slots`，所有内部 `<slot>` 标签
    * `emit`：等同于 `$emit`，发布自定义事件的方法
    * `expose`：用于暴露公共 `property` 的函数

> [!danger] `attrs`，`slots` 是带状态对象，随组件更新而更新，因此不能用 ES6 的解构

> [!check] `context` 是普通对象，可以正常解构

# 返回值

`setup()` 方法可以返回一个对象或函数

* 对象：若 `setup` 返回一个对象，该对象将暴露给模板渲染，模板可以直接访问该对象的属性

`````col
````col-md
flexGrow=1
===
```embed-js
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/setup-return-object.html"
LINES: "15-26"
```
````
````col-md
flexGrow=1
===
```embed-html
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/setup-return-object.html"
LINES: "10-12"
```

* 使用 `Vue.ref` 创建一个响应式值
	* 用于基础数据类型：`undefined`，`null`，`String`，`Boolean`，`Number`，`Symbol`
* 使用 `Vue.reactive` 创建一个响应式对象
````
`````

* 函数：`setup` 可以返回一个渲染函数。该函数中返回一个 `Vue.h` 的调用结果，可以使用同一个作用域中声明的响应状态

`````col
````col-md
flexGrow=1
===
```embed-js
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/setup-return-function.html"
LINES: "13-21"
```
````
````col-md
flexGrow=1
===
```embed-js
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/setup-return-function.html"
LINES: "9-11"
```
````
`````

# getCurrentInstance

用于访问内部组件实例，只能在 `setup` 或生命周期函数中使用，也可以访问 `globalProperties`
- `ctx`：用于开发环境
- `proxy`：用于生产环境
	- `$ref`：获取 `ref` 引用的元素

```js
import { getCurrentInstance } from "vue"
export default {
    setup() {
        const instance = getCurrentInstance()
        // 获取 globalProperties
        const {ctx, proxy} = instance.appContext.config.globalProperties;
    }
}
```