
`````col
````col-md
flexGrow=1
===
- Vue 默认历史记录模式为 Hash 模式，通过 `createWebHashHistory()` 创建
- HTML5 历史记录模式通过 `createWebHistory()` 创建
````
````col-md
flexGrow=1
===

| createWebHashHistory 模式  | createWebHistory 模式    |
| ------------------------ | ---------------------- |
| `localhost:8080/#/Login` | `localhost:8080/Login` |

````
`````

```js
import { createRouter, createWebHistory } from 'vue-router'

const routes = [ ... ]

const router = createRouter({
    history: createWebHistory(),
    routes
})
```

* 优点：看上去像一个正常的 URL，没有 `#` 标记
* 缺点：若后端服务器没有做到路由全覆盖，可能返回 404 错误
	* 解决办法：添加回退路由（若不匹配任何静态资源则返回 `index.html`）

---

原理：HTML5 历史记录 API 新增 `pushState` 和 `replaceState` 方法，允许修改历史记录时不立即发送请求