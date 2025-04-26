>[!note] Spring 表达式语言，`Spring Expression Language`，`SpEL`，是一种扩展 `UnifiedEL` 的表达式语言，可简化 bean 类配置。

>[!note] 表达式模板：将文本和一个或多个表达式相混合，其中 SpEL 表达式放在 `#{}` 中间，可用于 `@Value` 注解、XML 等场景

在上下文中使用自定义 `TemplateParserContext` 可以自定义表达式模板的前后缀

>[!summary] Spring 引入 `SpelExpressionParser` API 可脱离 Spring Application 对 SpEL 进行计算。

SpEL 的字面量与 Java 相似，但字符串支持单引号使用。`'Hello World'` 是一个 SpEL 表达式，表示一个 `Hello World` 字符串

`SpEL` 中字符串可以使用 `""`，也可以使用 `''` 引用

```reference
file: "@/_resources/codes/spring/spel/src/test/java/ExpressionTest.java"
start: 13
end: 19
```

---

- [[SpEL 成员访问]]
- [[SpEL 上下文 EvaluationContext]]
- [[SpEL 运算符]]

---

[[SpEL]]
