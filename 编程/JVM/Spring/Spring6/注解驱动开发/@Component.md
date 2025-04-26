`@Component` 相当于 `<bean>` 标签，可以用来声明一个 bean。
-  修饰类时，相当于 `<bean>` 的 `class` 属性
-  用于元注解，使目标注解可声明 bean 的能力

> [!note] 带有 `@Component` 功能的注解包括：`@Service`，`@Controller`，`@Repository` 等。

`@Component` 声明的 bean 名称（相当于 `id` 属性）默认为首字母小写的类名，可以通过传入 `value` 属性修改。

```reference
file: "@/_resources/codes/spring/annotation-component/src/main/java/com/example/mybank/EmailMessageSender.java"
start: 5
end: 7
```

```reference
file: "@/_resources/codes/spring/annotation-component/src/main/java/com/example/mybank/TxServiceImpl.java"
start: 5
end: 7
```

除 `id` 外，其他属性也有对应的注解：

| 注解           | XML 属性      | 说明                                                                                                                                                                  |
| ------------ | ----------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `@Scope`     | `scope`     | 接收一个 String<br>- `ConfigurableBeanFactory`：`SCOPE_SINGLETON`、`SCOPE_PROTOTYPE`<br>- `WebApplicationContext`：`SCOPE_REQUEST`，`SCOPE_SESSION`，`SCOPE_APPLICATION`<br> |
| `@Lazy`      | `lazy-init` | `singleton` 作用域的 `bean` 延迟初始化；<br />与 `@Autowire` 等自动装配注解配合，延迟字段依赖项的自动装配<br />                                                                                      |
| `@DependsOn` | `depend-on` | 依赖                                                                                                                                                                  |
| `@Primary`   | `primary`   | 优先自动装配                                                                                                                                                              |
