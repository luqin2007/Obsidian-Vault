Vuex 使用单一状态树，一个对象包含了所有应用层级状态作为唯一数据源，每个应用仅包含一个 `store` 实例

```js
import { createStore } from 'vuex'

export default createStore({
  state: {
    count: 0
  },
  mutations: {
    increment(state) {
      state.count++
    }
  },
})
```

`store` 对象通过 `this.$store.state` 访问，使用 `this.$store.commit('<mutations>')` 修改

```html
<template>
  <div>计数器：{{ $store.state.count }}</div>
  <button @click="$store.commit('increment')">+</button>
</template>
```

> [!success] 计算属性可以通过 `$store.state` 访问 `store` 中的属性，并可以自动更新

```js
computed: {
  count() {
    return this.$store.state.count
  }
}
```

若一个组件需要获取多个状态，声明计算属性会很麻烦，可以通过 `mapState` 函数辅助生成计算属性

````tabs
tab: `mapState(object)`

对象键为计算属性名，值为 `state => value` 或 `string`

```js
import { mapState } from 'vuex'

export default {
    data() { return { localCount: 0 } },
    computed: mapState({
        count: state => state.count,
        // 等价于 state => state['count']
        countAtlas: 'count',
        // 使用完整函数可以访问 this
        countPlusLocalState(state) {
            return state.count + this.localCount
        }
    })
}
```

tab: `mapState(string[])`

为每一个字符串生成计算函数，并对应 `state` 中的同名状态

```js
import { mapState } from 'vuex'

export default {
    // 等价于 mapState({ count: 'count' })
    computed: mapState(['count'])
}
```
````

`mapState` 返回的是一个对象，因此可以通过对象展开运算符与其他本地计算属性混合

```js
import { mapState } from 'vuex'

export default {
    computed: {
        localComputed() { /* do something */ },
        ...mapState(['count'])
    }
}
```

---

# 例：state 与计算属性

```js title:'src/store/index.js'
import { createStore } from 'vuex'

export default createStore({
  state: {
    bookAuthor: '陈恒',
    BISBN: '97873025985036',
    bookPrice: 99.8,
    bookPress: '清华大学出版社',
  }
})
```

```js title:src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import store from './store'

createApp(App).use(store).mount('#app')
```
* `src/App.vue`：展示数据

```html title:src/App.vue
<template>
  <!-- 直接访问 $store 的全局数据 -->
  <div>
    <h3>{{ bookName }}</h3>
    <h3>作者：{{ $store.state.bookAuthor }}</h3>
    <h3>出版社：{{ $store.state.bookPress }}</h3>
    <h3>ISBN：{{ $store.state.BISBN }}</h3>
    <h3>价格：{{ $store.state.bookPrice }}</h3>
  </div>
  <hr>
  <!-- 通过计算属性访问数据 -->
  <div>
    <h3>{{ bookName }}</h3>
    <h3>作者：{{ bookAuthor }}</h3>
    <h3>出版社：{{ bookPress }}</h3>
    <h3>ISBN：{{ BISBN }}</h3>
    <h3>价格：{{ bookPrice }}</h3>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'App',
  data() {
    return {
      bookName: 'SSM + SpringBoot + Vue.js 全栈开发从入门到实战（微课视频版）'
    }
  },
  computed: {
    bookPress() {
      return this.$store.state.bookPress
    },
    ...mapState(['BISBN', 'bookPrice', 'bookAuthor'])
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
```
