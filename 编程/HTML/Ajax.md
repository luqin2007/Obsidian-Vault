Asynchronous Javascript And XML，异步的 JS 和 XML，在网页中通过 JavaScript 主动与服务器进行交互

# XMLHttpRequest

Ajax 通过 `XMLHttpRequest` 对象实现：

```javascript
let xhr = new XMLHttpRequest()
// 配置 xhr
xhr.open(type: 'GET'|'POST', url: string[, data: { 参数 }])
// 设置请求头
xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
// 监听对应事件
xhr.addEventListener('readystatechange', ev => { ... })
xhr.addEventListener('load', ev => { ... })
// 配置请求头等信息
xhr.send([data])
```

- 请求头必须在 `open()` 函数调用后设置，事件监听则没有要求
	- 固定写法：非文件传输的 POST 类型请求应当设置 `Content-Type` 头
- `open()` 方法的 `data` 参数将以查询参数的形式拼接到 `url` 后面
- `send()` 方法的 `data` 参数将以 POST 请求的请求体发送到服务器

## 事件

- `load`：请求完全成功后调用
- `timeout`：请求超时时调用
- `error`：请求失败时调用
- `progress`：上传/下载数据过程中触发，用于进度显示等
	- `ev.lengthComputable`：数据总长度是否已经可以读
	- `ev.loaded`：已接收数据长度
	- `ev.total`：总数据长度
- `readystatechange`：当请求状态发生变化时触发

## 属性

- 请求超时：`timeout`
- 响应：`response`, `responseText`
- 响应类型：`responseType`：`text` / `json` / `blob` / `document` / `arraybuffer`
- 请求状态：`readyState`
	- 0：UNSENT，对象创建但未调用 `open()` 方法
	- 1：OPENED，对象 `open()` 方法已调用
	- 2：HEADERS_RECEIVED，响应头已被接收
	- 3：LOADING，响应数据接收中，`response` 属性中已具有部分数据
	- 4：DONE，请求完成，响应数据完全接收完成
- 服务器状态：`status`，成功为 200

## FormData

推荐创建 `FormData` 对象用于传输表单数据，数据通过 `send` 函数参数发送到服务器

- 使用 `append` 追加数据内容
- 追加的内容允许是文件，可以是 `<input type='file'>` 标签的 `files` 数组的值

# 跨域

> [!note] 同源：两个请求的协议，域名和端口完全相同

> [!note] 同源策略
> Same origin policy，通过 Javascript 发起的请求不允许在非同源网站之间进行资源交互
>
>- 无法读取非同源网页的 `Cookie`，`LocalStorage`，`IndexedDB`
>- 无法处理非同源网页的 `DOM`
>- 非同源地址 `Ajax` 响应需要 `CORS` 校验

跨域请求即非同源请求。浏览器总是允许发送跨域请求，但客户端响应信息会被拦截，根据同源策略决定是否允许接收

## JSONP

兼容低版本 IE，只支持 GET 请求，不支持 POST 请求

原理：HTML 标签不受浏览器同源策略影响，因此可通过 `<script>` 标签的 `src` 属性发起跨域请求，并通过函数调用的形式接收

```HTML
<script>
function success(data) {
    // ...
}
</script>

<script src='...'></script>
```

接收的 js 代码中调用了网页中定义的 `success` 方法，如：

```javascript
success({ ... })
```

这样，前面的 `success` 方法中即可接收到对应数据

## CORS

W3C 标准，属于 Ajax 跨域解决方案，支持 GET 和 POST 请求，需要依赖服务器端配合

### 跨域限制

要解除浏览器的跨域访问限制，只需要服务器在响应头中添加以下数据即可：

```
Access-Control-Allow-Origin: <origin>|*
```

- `<origin>|*`：允许的域名

默认情况下，CORS 只支持有限几个请求头：
- `Accept`
- `Accept-Language`
- `Content-Language`
- `DPR`
- `Downlink`
- `Save-Data`
- `Viewport-Width`
- `Width`
- `Content-Type=text/plan|mulitpart/form-data|application/x-www-form-urlencode`
若需要其他响应头，使用 `Access-Control-Allow-Headers` 响应头进行声明，多个头之间用 `,` 分隔

默认情况下，CORS 只支持 GET，POST，HEAD 请求，若要求其他请求，可在服务器返回的 `Access-Control-Allow-Methods` 响应头进行声明

### 请求类型

- 简单请求：`GET`，`POST`，`HEAD` 之一请求方式，请求头只包含 CORS 支持的 9 个响应头，直接发送到服务器
- 预检请求：浏览器先发送 `OPTION` 请求到服务器，服务器检查请求通过后服务器再发送实际请求
