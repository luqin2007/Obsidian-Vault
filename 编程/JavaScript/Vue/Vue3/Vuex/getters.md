使用 `getters` 属性可以从 `store.state` 中派生出一些状态，即全局共享的计算属性。

`getter(state, getters)` 函数参数可以用于访问其他参数

```js
import { createStore } from 'vuex'

export default createStore({
  state: {
    todos: [
      { id: 1, text: '...', done: true },
      { id: 2, text: '...', done: false },
    ]
  },
  getters: {
    doneTodos(state) {
      return state.todos.filter(todo => todo.done)
    }
  }
})
```

`getters` 也可以返回一个函数，这时可以作为一个普通函数使用

```js
getters: {
  getTodoById(state) {
    return id => state.todos.find(todo => todo.id === id)
  }
}

// 使用
store.getters.getTodoById(1)
```

`getters` 也有对应的辅助创建函数 `mapGetters`，用法与 mapState 类似，将 `store` 中的 `getters` 映射到局部属性 `computed` 中

# 例：使用 getters

本例承接 `state` 与计算属性中的<span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">例子</span>

- 在 `index.js` 中添加 `getters` 函数

```js title:src/store/index.js
import { createStore } from 'vuex'

export default createStore({
  state: {
    bookPrice: 99.8,
  },
  getters: {
    getBookPrice(state) {
      return state.bookPrice
    },
    getThreeBookPrice(state, getters) {
      return state.bookPrice + getters.getBookPrice * 2
    },
    getManyBookPrice(state) {
      return count => state.bookPrice * count
    }
  }
})
```

* 在 `App.vue` 中添加到 `computed` 中，直接映射的可以使用 `mapGetters` 简化

```js title:App.vue
import { mapGetters } from 'vuex'

export default {
  name: 'App',
  computed: {
    getFiveBookPrice() {
      return this.$store.getters.getManyBookPrice(5)
    },
    ...mapGetters(['getBookPrice', 'getThreeBookPrice'])
  }
}
```

* `App.vue` 模板部分调用

```html title:App.vue
<template>
  <div>
    <h3>使用 getter: </h3>
    <h3>买一本需要花的钱：{{ $store.getters.getBookPrice }}</h3>
    <h3>买三本需要花的钱：{{ $store.getters.getThreeBookPrice }}</h3>
    <h3>买五本需要花的钱：{{ $store.getters.getManyBookPrice(5) }}</h3>
    <h3>买一本需要花的钱：{{ getBookPrice }}</h3>
    <h3>买三本需要花的钱：{{ getThreeBookPrice }}</h3>
    <h3>买五本需要花的钱：{{ getFiveBookPrice }}</h3>
  </div>
</template>
```
* ‍
