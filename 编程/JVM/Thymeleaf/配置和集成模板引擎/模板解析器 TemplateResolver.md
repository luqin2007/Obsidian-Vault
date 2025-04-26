```java
WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(applicationContext);
templateResolver.setPrefix("/templates");
templateResolver.setSuffix(".html");
templateResolver.setTemplateMode(TemplateMode.HTML);
```

`TemplateResolver` 需要一个 `IWebApplication` 应用程序上下文。Thymeleaf 默认内置了 JavaEE 和 Spring 的支持：
* `JavaxServletWebApplication`：基于 `javax.*` 的 JavaEE 支持
* `JakartaServletWebApplication`：基于 `jakarta.*` 的 Jakarta 支持
* `SpringResourceTemplateResolver`：Spring Web 支持
* `SpringWebFluxWebApplication`：Spring Webflux 框架支持

`TemplateResolver` 用于设置该 `ITemplateEngine` 的路径（前后缀）和模式，这里使用的是 HTML。
* HTML：**默认**。允许 HTML 或 XHTML 输入，不进行格式的正确性检查
* XML：允许 XML 输入，会检查格式良好、是否有未闭合标记、未引用属性，出错时解析器抛出异常
* TEXT：纯文本替换
* JAVASCRIPT：允许在 JS 脚本中使用模型数据，属于文本模式
* CSS：类似于 JS，使用文本模式
* RAW：不进行任何处理

>[!note] 使用上面配置的 `TemplateResolver` 加载 `/product/list` 模板，相当于 `applicationContext.getResource("/templates/product/list.html")`

> [!check] `TemplateResolver` 支持使用缓存。通过 `setCacheTTLMs` 设置 TTLM 属性

其他解析器默认实现：

|解析器|说明|加载方法（路径：template）|
| --------| ----------------------| ----------------------------|
|`ClassLoaderTemplateResolver`|从类加载器加载模板|`classLoader.getResourceAsStream(template)`|
|`FileTemplateResolver`|从给定文件加载模板|`new FileInputStream(new File(template))`|
|`UrlTemplateResolver`|解析为 URL 加载模板|`(new URL(template)).openStream()`|
|`StringTemplateResolver`|直接将模板名作为模板|`new StringReader(template)`<br />|

其他常用参数：

|方法|说明|
| ----------| --------------------------------------------|
|`addTemplateAlias`，`setTemplateAliases`|允许使用别名，不直接使用文件名|
|`setCharacterEncoding`|编码|
|`setCacheable`，`getCacheablePatternSpec`|是否开启缓存，以及某个模板是否允许使用缓存|
|`setCacheTTLMs`|缓存 TTLM 属性（失效时间）|
