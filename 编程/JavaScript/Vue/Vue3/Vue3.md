Vue 是一个用于构建用户界面的渐进式 js 框架，采用自底而上的增量开发设计。

> [!note] MVVM：Model-View-ViewModel 模型
> 
> 核心是 Model 和 ViewModel 的双向数据绑定，Model 层的变化自动映射到 View 层，无须手动操作 DOM，无须关注数据状态同步
>
> * Model：数据模型，也可以在其中定义数据修改和操作的业务逻辑
> * View：UI 组件，将数据转化为 UI 展示
> * ViewModel：同步 View 与 Model，同时与双方绑定

Vue 本身只是一个 js 库，通过尽可能简单的 API 实现数据绑定，可轻松实现 SPA 单页面应用。

* 引入本地独立 js 库或 CDN：`https://unpkg.com/vue`
* 使用 Vue CLI：`npm install vue@next`

> [!attention] 单页面应用有助于减少访问请求，减轻服务器压力，但不利于 SEO 优化

# Hello Vue

* 通过 `Vue.createApp( app配置 )` 创建一个 Vue app
* 通过 `app.mount(selector)` 绑定 HTML 元素

`````col
````col-md
flexGrow=1
===
```embed-html
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/index.html"
LINES: "9-23"
```
````
````col-md
flexGrow=1
===
![[image-20240521153840-npwpols.png]]
````
`````

Vue 采取声明式渲染，利用模板将数据渲染到 DOM 中，任何数据变化都会响应式应用到页面上

`````col
````col-md
flexGrow=1
===
```embed-html
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/counter.html"
LINES: "6,9-11,13-25"
TITLE: 页面中每隔 1s 数字加一
```
````
````col-md
flexGrow=1
===
* 任何一个 app 配置中的函数 `this` 都指向该 app
* `mounted` 是一个 Vue 生命周期钩子函数，在其中每秒修改 `counter` 的值
* `{{ ... }}` 为模板的插值表达式
````
`````

# globalProperties

Vue 3.0 应用配置属性，用于挂载全局属性或方法

```js
const app = createApp({})
app.config.globalProperties.$http = () => {}
```

---

- [[生命周期]]
- [[插值表达式]]
- [[属性监听/属性监听|属性监听]]
- [[内置指令/内置指令|内置指令]]
- [[组件/组件|组件]]
- [[过渡与动画/过渡与动画|过渡与动画]]
- [[自定义指令|自定义指令]]
- [[渲染函数|渲染函数]]
- [[组合式 API/组合式 API|组合式 API]]
- [[单文件组件/单文件组件|单文件组件]]
- [[Vue Router/Vue Router|Vue Router]]
- [[Vuex/Vuex|Vuex]]
- [[../Vue UI 组件库|Vue UI 组件库]]

# 参考

```cardlink
url: https://book.douban.com/subject/36481878/
title: "Vue.js 3.x从入门到实战（微课视频版）"
description: "图书Vue.js 3.x从入门到实战（微课视频版） 介绍、书评、论坛及推荐"
host: book.douban.com
image: https://img9.doubanio.com/view/subject/l/public/s34590775.jpg
```

```cardlink
url: https://book.douban.com/subject/36116376/
title: "Vue.js 3.0企业级管理后台开发实战：基于Element Plus"
description: "《Vue.js 3.0企业级管理后台开发实战：基于Element Plus》系统全面、由浅入深介绍了管理后台开发的各方面知识、经验和技巧，包括企业内部真实的项目开发方式、项目原型、API接口文档、AP..."
host: book.douban.com
image: https://img9.doubanio.com/view/subject/l/public/s34325466.jpg
```
