Vuex 可以将 `store` 中的状态分割为多个模块 `module`。
* 每个模块都可以包含各自的 `state`，`getters`，`mutations`，`actions`，`modules`
* 子模块通过 `getters`、`mutations`、`actions` 中的函数通过第三个参数 `rootState` 访问根模块的状态
* 通过根模块可以使用 `state.<module 1>...` 访问子模块的状态

`````col
````col-md
flexGrow=1
===
```js
const moduleA = {
    state: () => ({ ... }),
    getters: { ... },
    mutations: { ... },
    actions: { ... },
}

const moduleB = {
    state: () => ({ ... }),
    getters: { ... },
    mutations: { ... },
    actions: { ... },
}
```
````
````col-md
flexGrow=1
===
```js
const store = createStore({
    modules: {
        a: moduleA,
        b: moduleB
    }
})

// 使用
store.state.a
```
````
`````

> [!note] 导出的 `module` 对象可以包含一个 `namespaced` 布尔值，默认 `false`，`true` 可将模块限定到命名空间下，解决模块化空间问题。
