`@Value` 注解类似于 `value` 属性或 `<value>` 标签，可以直接输入装配对象的值，也可以使用 SpEL 动态的进行一些运算。该注解可用于字段、方法（包括构造方法）、参数等。

`@Value` 和 `SpEL` 也是由 `AutowiredAnnotationBeanPostProcessor` 负责处理的

使用 SpEL 时，需要使用 `"#{}"` 包围

```reference
file: "@/_resources/codes/spring/annotation-value/src/main/java/com/example/mybank/Sample.java"
start: 6
end: 14
```

`@Value` 应用于字段或参数时表示该字段或参数的装配值，应用于方法时表示方法参数的装配值。

`@Value` 应用于函数或函数参数时，仅在 `@Autowired`，`@Resource` 或 `@Inject` 注解对应函数时有效。

‍
