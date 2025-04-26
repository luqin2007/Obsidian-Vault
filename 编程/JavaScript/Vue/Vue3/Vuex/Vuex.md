Vuex 是一个专为 Vue.js 应用开发的状态管理模式，采取集中式管理应用所有组件的状态，并指定状态变化规则。

> [!note] Vuex 也是 JS 的一个模块，可以通过独立 js 文件、NPM、CDN 或 Vue CLI 添加

* 状态：state，数据源，即应用中的 `data` 块
* 视图：view，以声明方式将状态映射到视图，如 `{{ count }}`
* 操作：action，响应用户视图操作造成的状态变化，即组件中的 `methods` 块

>[!note] 单向数据流：数据流仅以视图 -> 状态 -> 操作 -> 视图的方向流动

一个单向数据流的实例：

`````col
````col-md
flexGrow=1
===
```html
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <script src="../../js/vue.global.js"></script>
</head>
<body>
    <div id="counter">
        {{ counter }}
        <button @click="increment">+</button>
    </div>
</body>
</html>
```
````
````col-md
flexGrow=1
===
```js
Vue.createApp({
    data() {
        return {
            counter: 0
        }
    },
    methods: {
        increment() {
            this.counter++
        }
    }
}).mount('#counter')
```
````
`````

* `data.counter` 是状态，`increment()` 是操作，页面中 `{{counter}}` 是视图
* `counter` 在视图上显示，被按钮通过 `increment` 操作修改状态，之后重新显示到视图上

> [!caution] 应用场景：管理被多个组件共同使用的变量，如登录状态等

1. 导入并创建仓库 store，通常在 `/src/store/index.js` 中配置

    ```js
    import { createStore } from 'vuex'

    export default createStore({
      // 状态，对应 data
      state: { ... },
      // 计算属性，对应 computed
      getters: { ... },
      // 更新 state 状态（同步操作），对应 methods 同步方法
      mutations: { ... },
      // 提交 state 状态（异步操作），对应 methods 异步方法
      actions: { ... },
      // 多模块 state
      modules: { ... }
    })

    ```

2. 在 `Vue App` 中加载 Vuex

    ```js
    import { createApp } from 'vue'
    import App from './App.vue'
    import store from './store'

    createApp(App).use(store).mount('#app')
    ```

`store`是一个容器，包含应用中的大部分状态。使用 `store` 替代全局对象的优势为：
* Vuex 状态是响应式的，`store` 状态发生变化时 Vue 实例组件也会高效更新
* 用户不能直接更新 `store` 中的状态，需要显示提交 `mutation` 以便跟踪每个状态变化

---

- [[state 与计算属性]]
- [[getters]]
- [[mutations 与 actions]]
- [[modules]]
