允许在配置路由时通过 `props` 对象向组件中传递参数，类似 Vue 组件的 props 通信

1. 注册导航配置

```html
<router-link to="/">首页</router-link>
```

```js
const routes = [{
  path: '/',
  name: 'home',
  component: HomeView,
  // 设置 props 参数
  props: { uname: '张三', upwd: '123456' }
}]
```

2. 视图组件配置

```html
<script>
export default {
    name: 'HomeView',
    props: {
        uname: { type: String, default: 'lisi' },
        upwd:  { type: String, default: '000000' }
    }
}
</script>
```

‍
