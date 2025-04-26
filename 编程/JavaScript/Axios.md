专用于网络数据请求的库
# 请求

## 即时请求

`axios(config).then(response) ...`
- `axios(url[, config])`
- `axios.request(config)`
- `axios.get/post/put/delete(url[, config])`
- `axios.head(url[, config])`
- `axios.options(url[, config])`
- `axios.patch(url[, data][, config
## 请求配置

[请求配置 | Axios 中文文档 | Axios 中文网 (axios-http.cn)](https://www.axios-http.cn/docs/req_config)
## 默认配置

`axios` 全局的默认请求配置位于 `axios.defaults` 中，配置合并的优先级为：
`lib/defaults.js` < 请求实例的 `defaults` 对象 < 请求的 `config` 对象

### 请求实例

使用自定义的 `config` 创建 `axios` 实例，该实例的默认配置将根据 `config` 的值修改

`const instance = axios.create(config)`

可从任意 `axios` 实例创建实例，可从 `axios.request`，`axios.get`，`axios.post` 等 `axios` 实例创建实例

## 取消请求

使用 `fetchAPI` 取消请求

```javascript
const controller = new AbortController()

// 请求
axios({..., signal: controller.signal})

// 取消请求
controller.abort()
```

# 响应

`axios` 发起请求后返回的值为一个 `Promise` 对象，通过 `then` 函数可以传入响应处理函数

`response` 对象结构如下

```json
{
  // `data` 由服务器提供的响应
  data: {},

  // `status` 来自服务器响应的 HTTP 状态码
  status: 200,

  // `statusText` 来自服务器响应的 HTTP 状态信息
  statusText: 'OK',

  // `headers` 是服务器响应头
  // 所有的 header 名称都是小写，而且可以使用方括号语法访问
  // 例如: `response.headers['content-type']`
  headers: {},

  // `config` 是 `axios` 请求的配置信息
  config: {},

  // `request` 是生成此响应的请求
  // 在node.js中它是最后一个ClientRequest实例 (in redirects)，
  // 在浏览器中则是 XMLHttpRequest 实例
  request: {}
}
```


# 拦截

在 `axios.interceptors` 中可以设置请求和响应的拦截器
- 请求拦截：`const ireq = axios.interceptors.request.use((config) => config, (error) => ...)`
	- 移除：`axios.interceptors.request.eject(ireq)`
- 响应拦截：`const irsp = axios.interceptors.response.use((response) => response, (error) => ...)`
	- 移除：`axios.interceptors.resonse.eject(irsp)`

# 异常处理

返回的是 `Promise` 对象，因此可以通过 `catch` 函数传入异常处理函数
- `error.request`
- `error.response`
- `error.message`
- `error.config`
- `error.toJSON()`
- `config.validateStatus: (status) => bool`：异常处理函数仅处理某些状态码
# 参考

```cardlink
url: https://www.axios-http.cn/
title: "Axios中文文档 | Axios中文网"
description: "Axios 是一个基于 promise 的网络请求库，可以用于浏览器和 node.js"
host: www.axios-http.cn
favicon: https://www.axios-http.cn/img/favicon.ico
```
