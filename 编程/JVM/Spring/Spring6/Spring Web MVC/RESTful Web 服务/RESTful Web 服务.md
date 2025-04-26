>[!note] `RESTful`，表征状态传输，是一种架构风格。应用程序资源由 URI 统一管理，客户端通过 HTTP 请求与资源进行交互。

与基于 SOAP 的 Web 服务相比，实现更简单，更具有扩展性。传输的数据不必要是 XML，渲染等也可以完全交给客户端完成。
* 服务器端，控制器中直接将对象[[构建状态信息|构建为 Json 格式的 REST 信息]]
* Spring 也提供 `RestTemplate` 和 `WebClient` 用于[[RESTful 客户端|构建访问 RESTful 服务的客户端]]
