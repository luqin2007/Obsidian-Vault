```java
TemplateEngine templateEngine = new TemplateEngine();
templateEngine.setTemplateResolver(templateResolver);
```

> [!note] 对于 Spring，可以使用 `SpringTemplateEngine` 并设置 `setEnableSpringELCompiler(true)`。这将使用 SpringEL 编译脚本并提高效率

使用时，通过 `process` 处理模板

```java
void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) {
    WebContext context = new WebContext(webExchange, webExchange.getLocale());
    templateEngine.process("home", context, writer);
}
```

一个模板引擎可以连接多个模板解析器，可以通过 `TemplateResolver#setOrder` 设置优先级。

>[!note] 当引擎中包含多个模板解析器时，处理某个模板会尝试使用所有匹配类型的解析器依次解析。`TemplateResolver#checkExistence` 可以设置在选择解析器前是否检查能否查找到对应模板文件，但可能会出现性能问题。

一个模板引擎可以连接多个消息解析器。消息解析器用于查找消息文件，实现 `IMessageResolver` 接口，默认使用 `StandardMessageResolver`预处理字符串中的 _ 可以通过 _ 转义。

`WebContext` 是一个[[上下文 Context|模板上下文]]，为模板引擎提供必要信息。
