Thymeleaf 上下文为 `org.thymeleaf.context.IContext` 接口或 `IWebContext` 接口。

>[!note] Spring 下，`SpringResourceTemplateResolver` 可直接使用 Spring 的 `ApplicationContext`

* `IContext`：上下文包含模板引擎所需的数据和区域信息
* `IWebContext`：扩展了 `IContext`，额外包含 `WebExchange` 对象，用于表示请求-响应的抽象对象

`WebContext` 类实现了 `IWebContext` 接口，可以直接通过 `WebExchange` 创建。
