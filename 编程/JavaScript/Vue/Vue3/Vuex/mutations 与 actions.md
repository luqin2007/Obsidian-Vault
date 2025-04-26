# `mutations`

> [!important] `mutations` 是直接修改 `store` 的唯一方法，`action` 通过提交 `mutations` 间接更改 `store` 状态，类似于事件。

* 每个 `mutation` 都有一个类型（`type`：`string`）和回调函数（`handler`：`(state, ...) => void`）
* 只能在 `mutation` 的回调函数 `handler` 中更改 `state`。
* `handler` 接收第一个参数固定为 `state`，还可以接收其他需要的参数，且必须为**同步函数**
* 不能直接调用 `mutation`，需要通过提交的方式触发 `$store.commit()` -- 类似于触发事件

`handler` 函数包含除 `state` 外的其它参数时，`commit` 函数接收的参数形式可以是：

* 提交载荷：`$store.commit(<type>, <payload>)`，`<payload>` 即额外参数，称为载荷
* 对象：`$store.commit(object)`，要求 `object` 中包含一个 `type` 属性

> [!success] ES2015 支持使用常量作为计算属性命名，推荐使用常量

```js
// mutation-types
export const SOME_MUTATION = 'SOME_MUTATION'
```

```js
// store.js
const store = createStore({
    mutations: {
        [SOME_MUTATION]: state => { .. }
    }
})
```

`mapMutations` 函数可以将 `mutations` 映射到组件中的 `methods`，**需要在根节点注入** **`store`**

* `mapMutations(string[])`
* `mapMutations(object)`，其中 `key` 为函数名，`value` 为 `mutation` 名
* 支持 `...` 对象展开

# `actions`

`actions` 类似 `mutations` 可以用于该变 `state`，但 `actions` 通过提交 `mutation` 间接修改 `state`

`actions` 与 `mutations` 的使用方式基本相同，除了：

* 使用 `$store.dispatch` 触发，`dispatch` 同样支持载荷和对象形式
* 使用 `mapActions` 辅助映射到 `methods`
* **支持异步函数**

```js
import { INCREMENT, INCREMENT_BY } from '@/mutations'

export default createStore({
  state: {
    count: 0
  },
  mutations: {
    [INCREMENT](state) {
      state.count++
    },
    [INCREMENT_BY](state, amount) {
      state.count += amount
    }
  },
  actions: {
    increment(context) {
      context.commit(INCREMENT)
    },
    incrementBy(context, amount) {
      context.commit(INCREMENT, amount)
    }
  }
})
```

提交异步任务：

```js title:store/app.js
const actions {
    loginAction(context, requestData) {
        return new Promise((resolve, reject) => {
            // 执行一个异步操作
            login(requestData).then(resolve).catch(reject)
        })
    }
}
```

```html title:component.vue
<script setup>
import { useStore } from "vuex"

const store = useStore()

function login() {
    // 创建 requestData 数据对象
    const loginData = {}
    // 调用 app module 的 loginAction actions
    store.dispatch("app/loginAction", loginData)
         .then( /* do something */ )
         .catch( /* do something */ )
}
</script>
```