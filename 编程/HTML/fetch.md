#html5 

对 Ajax 的封装和增强，针对 `XMLHttpRequest` 功能集中于一个类、基于事件驱动的情况，适配 `Promise` 设计。

- 将头部信息、请求信息、响应信息切分到不同对象
- 使用 `Promise API` 重新封装

```javascript
fetch(url[, option]) => Promise<Response>
```

`option` 为请求[配置对象](https://developer.mozilla.org/en-US/docs/Web/API/RequestInit#instance_properties)，常用的几个选项有：
- `method`：请求方法，默认 `GET`
- `headers`：`Headers` [对象](https://developer.mozilla.org/zh-CN/docs/Web/API/Headers#%E6%96%B9%E6%B3%95)，常用于 `append`、`has` 等方法
- `body`：[请求体](https://developer.mozilla.org/zh-CN/docs/Web/API/Fetch_API/Using_Fetch#body)
- `mode`：模式，用于配置跨域访问（`cors`，`no-cors` 和 `same-origin`）

`Response` 为响应对象，常用属性有：
- `status`：响应码
	- `ok`：是否请求成功，即响应码在 `200-299` 之间
- `text()`：文本格式 Ajax 响应
- `blob()`：二进制格式 Ajax 响应
- `json()`：JSON 格式 Ajax 响应
- `redirect()`：重定向到另一个 URL

# 请求

每次 `fetch` 都会创建一个请求，也可以手动创建 `Request` 对象