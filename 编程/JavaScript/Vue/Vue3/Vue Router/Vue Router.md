Vue Router 是 Vue 官方路由管理器，用于单页面应用管理，可通过单独文件，`CDN` 等导入，也可以使用 `Vue CLI` 工具导入。

使用 Vue Router 动态加载组件时需要将组件映射到路由中，并在特定的地方显示即可。

- [[路由信息]]
- [[嵌套路由]]
- [[编程式导航]]
- [[命名路由]]
- [[重定向]]
- [[props 传参]]
- [[HTML5 历史记录模式]]
- [[路由钩子函数]]
- [[路由元信息]]
- [[例：登录权限验证]]

# 路由

路由配置通常在 `src/router` 目录下的 `js` 文件中进行，并在 `main.js`  导入。

路由记录：`routes` 中记录的路由信息

1. 创建用于显示的组件与页面

````tabs
tab: CommonView 显示信息

  ```html title:src/components/CommonView.vue:显示信息
  <template>
      <div>
          <img src="../assets/logo.png" alt="Vue logo">
          <h1>{{ msg }}</h1>
      </div>
  </template>
  <script>
  export default {
      name: 'CommonView',
      props: {
          msg: String
      }
  }
  </script>
  ```

tab: FirstView query传参

  ```html title:src/views/FirstView.vue:query传参
  <template>
      <div>
          <CommonView msg="First View" />
          <br>
          <h4>uname: {{ $route.query.uname }}</h4>
          <h4>pwd: {{ $route.query.pwd }}</h4>
      </div>
  </template>
  <script>
  import CommonView from '@/components/CommonView.vue'
  export default {
      name: 'FirstView',
      components: { CommonView }
  }
  </script>
  ```

tab: SecondView params传参

   ```html title:src/views/SecondView.vue:params传参
   <template>
       <div>
           <CommonView msg="Second View" />
           <br>
           <h4>uname: {{ $route.params.uname }}</h4>
           <h4>pwd: {{ $route.params.pwd }}</h4>
       </div>
   </template>
   <script>
   import CommonView from '@/components/CommonView.vue'
   export default {
       name: 'SecondView',
       components: { CommonView }
   }
   </script>
   ```

````

2. 配置路由
    * `path`：路由路径
    * `name`：路由名称，用于[[命名路由]]
    * `component`：映射组件，可以是一个组件对象或其工厂函数

```js title:src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import FirstView from '@/views/FirstView.vue'

// 路由列表
const routes = [
    {
        path: '/first',
        name: 'first',
        component: FirstView
    },
    {
        path: '/second',
        name: 'second',
        component: () => import('@/views/SecondView.vue')
    }
]

// 路由实例
const router = createRouter({
    // 创建 HTML5 的历史记录
    history: createWebHistory(process.env.BASE_URL),
    routes
})
export default router
```

3. 使用路由

```js title:main.js
// main.js
import { createApp } from 'vue'
import App from './App.vue'
// 从 src/router 加载 router 模块
import router from './router'
// 注册 router
createApp(App).use(router).mount('#app')
```

4. 创建根组件，测试

```html title:src/App.vue
<template>
  <nav>
    <router-link to="/first?uname=chenheng&pwd=123456">第一个页面</router-link> |
    <router-link to="/second/shenheng1/654321">第二个页面</router-link>
  </nav>
  <!-- <router-view> 是路由出口 -->
  <!-- 路由返回的组件将输出到这里 -->
  <router-view />
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
```

# 跳转

* 使用 `<router-link>` 标签跳转，功能上类似 `<a>` 标签
    * `to`：目标地址
    * `tag`：指定渲染成的标签，默认为 `<a>`
    * `replace`：添加该属性后，不会留下历史记录，无法后退

```html
<router-link to="/" tag="li">Home</router-link>
<router-link to="/about">About</router-link>
```

* 使用 `$router.push('路径')` 等编程式导航 API 跳转

# 查询参数

Vue Router 支持两种查询参数传递方法：

* `query` 传参：如 `to=/?id=888&pwd=999`，使用 `$route.query.<param-name>` 获取参数值
* `params` 传参：如 `/888/999`，将符合某种匹配模式的所有路由路径映射到同一个组件，使用动态路径参数 `:参数名` 实现。

`````col
````col-md
flexGrow=1
===
```js
{
  path: '/:id/:pwd',
  name: '...',
  component: ...
}
```
````
````col-md
flexGrow=1
===
```js
const routes = [{
  path: 'user/:uname/:pwd',
  name: 'user',              // 路由名称
  component: UserView        // 路由对应组件
}]
```
````
`````

使用 `$route.params.<param-name>` 获取参数值，如 `$route.params.uname`

> `$route` 对象：记录路由相关信息
>
> |属性|类型|说明|
> | ----| ----| ------------------------------------|
> | `path` | `string` |当前路由路径|
> | `params` | `object\|{}` |一个 `key:value` 对象，包含所有((20240529140108-lghljgd "动态参数"))|
> | `query` | `object\|{}` |一个 `key:value` 对象，包含所有 ((20240529135814-wt9eub5 "URL 查询参数"))|
> | `hash` | `string\|""` |当前路由哈希值（不带 `#`）|
> | `fullPath` | `string` |解析后的完整 URL，包含查询参数和哈希|
> | `matched` | `object[]` |当前匹配路径中包含的所有片段对应配置|
> | `name` | `string` |当前路径名称|
> | `meta` | `object` |路由元信息|

# 参考

```cardlink
url: https://router.vuejs.org/
title: "Vue Router | The official Router for Vue.js"
description: "The official Router for Vue.js"
host: router.vuejs.org
favicon: https://router.vuejs.org/logo.svg
```
