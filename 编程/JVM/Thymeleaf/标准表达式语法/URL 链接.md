# URL 链接

Thymeleaf 为 URL 专门添加了一个 `@` 语法，假设应用程序上下文路径为 `https://localhost:8080/gtvg`

|形式|说明|结果|
| ------| --------------------------------------------| ------|
|`@{https://www.thymeleaf.org}`|绝对 URL|`https://www.thymeleaf.org`|
|`@{/details(id=${o.id})}`|`o` 为一个对象，`/` 表示相对应用程序上下文|`https://localhost:8080/gtvg/details?id=${o.id}`|
|`@{/{orderId}/details(orderId=${o.id})}`|路径中也可以使用变量|`https://localhost:8080/gtvg/${o.id}/details`|
|`@{~/billing/processInvoice}`|访问同服务器其他 Web 应用路径||
|`@{//code.jquery.com/jquery-2.0.3.min.js}`|协议相关 URL||

默认脚本引擎通过 `StandardLinkBuilder` 解析 `@` 语法，可以自定义 `ILinkBuilder` 实现。
