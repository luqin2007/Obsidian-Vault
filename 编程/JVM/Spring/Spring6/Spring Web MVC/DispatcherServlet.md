`DispatcherServlet` 使用 `HandlerMapping` 和 `ViewResolver` 处理请求

![[../../../../../_resources/images/DispatcherServlet 2024-09-10 19.21.13.excalidraw|50%]]

1. 请求被 `DispatcherServlet` 拦截
2. `DispatcherServlet` 根据路径查找对应 Controller 对象以处理请求
3. `DispatcherServlet` 调用控制器中对应的处理方法，获取模型数据和视图名称
4. `DispatcherServlet` 将控制器返回的视图名称发送给 `ViewResolver`，查找要渲染的实际视图
5. `DispatcherServlet` 将请求发送给待渲染的实际视图，实际视图此时可访问控制器返回的模型数据

`<init-param>` 的配置中，`contextConfigLocation` 指定一个 Spring XML 配置文件，称为 Web 应用程序上下文 XML 文件。该文件将被实例化成一个 `WebApplicationContext` 对象。若不存在该属性，则 Spring 会在 `WEB-INF` 下查找并实例化 `<servlet-name>-servlet.xml` 文件。

每个 `DispatcherServlet` 都有自己的 Web 应用程序上下文，不同 `DispatcherServlet` 实例之间上下文对象**不共享**，所有 `DispatcherServlet` 共享根 Web 应用程序上下文 XML 文件数据。

Web 应用程序上下文常用于定义控制器、视图解析等  
根 Web 应用程序上下文常用于定义数据源、服务、DAO 等

# WebApplicationContext

与通常的 Spring 对象相比，`WebApplicationContext` 还可以支持以下几种作用域：

|作用域|说明|
| --------| ---------------------------------------------------------------------|
|`request`|Spring 容器为每一个 HTTP 请求创建一个新实例，当 HTTP 请求结束时销毁|
|`session`|创建 HTTP 会话时创建一个新的实例，当会话销毁时实例被销毁|
|`globalSession`|仅适用于 Portlet 应用程序|
|`application`|创建 `ServletContext` 时创建一个新的实例，当 `ServletContext` 销毁时实例被销毁|
|`websocket`|创建 `WebSocket` 时创建一个新的实例，当 `WebSocket` 销毁时实例被销毁|

# Servlet API 对象

通过使 bean 类实现相关接口可以获取 Servlet 对象

|类|获取接口|说明|
| ------| ----------| --------------------------------------------|
|`ServletContext`|`ServletContextAware`|用于 bean 类与 Servlet 通信|
|`ServletConfig`|`ServletConfigAware`|用于 bean 类获取有关截取请求的 `DispatcherServlet` 配置信息|
