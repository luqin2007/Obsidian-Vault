需求：使用 Ajax 在页面中异步加载一张大图，并要求显示加载进度

方案：使用 `XMLHttpRequest` 的 `progress` 事件处理加载进度，并使用 `window.URL.createObjectURL` 方法生成图片地址，注意：
- 响应类型应为 `blob`
- 当且仅当 `ev.lengthComputable` 时可跟踪下载进度

```javascript
let req = new XMLHttpRequest()
req.open('get', '图片地址')
req.responseType = 'blob'

req.addEventListener('progress', ev => {
    if (ev.lengthComputable) {
        // 下载进度: ev.loaded/ev.total
    }
})
req.addEventListener('error', ev => { /* 处理异常 */ })
req.addEventListener('load', ev => {
    // url 即可直接用的图片地址
    let url = window.URL.createObjectURL(req.response)
})
```