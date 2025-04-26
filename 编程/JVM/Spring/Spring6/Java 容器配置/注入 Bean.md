`@Bean` 表示声明一个 `bean`，相当于一个 `<bean>` 标签。如果存在多个相同的 `bean`，在后面 `register` 的容器配置类中的 `bean` 会覆盖前面的

|`@Bean` 属性|等效 `<bean>` 属性|说明|
| -----------| --------------| -----------------------------------------------------------------|
|`value`，`name`|`id`|如果留空，使用方法名|
|`autowireCandidate`|`autowire-candidate`|<br />|
|`initMethod`|`init-method`|<br />|
|`destroyMethod`|`destroy-method`|如果一个 bean 定义了一个公有的 `close()` 或 `shutdown()`，则将其作为默认销毁方法|

> [!note] 若要取消该默认的 `close()` 和 `shutdown()` 销毁方法，使用 `destroyMethod=""` 即可

其他属性则由额外的注解实现：`@Scope`，`@DependsOn`，`@Lazy`，`@Primary`，`@Autowired`，`@Value`

> [!summary] `@Bean` 注解还可以在被 `@Component` 或 `@Named` 注解的类中的方法上使用。

# 注入 BeanPostProcessors 与 BeanFactoryPostProcessors

> [!note] JSR250、JSR380 无需注入 `PostProcessor` 也可生效

`BeanPostProcessors` 和 `BeanFactoryPostProcessors` 可以直接以 `@Bean` 的方式注入，但需要用 `static` 函数

```java
@Bean
public static ApplicationConfigurer applicationConfigurer() {
    ApplicationConfigurer configurer = new ApplicationConfigurer();
    configurer.setOrder(0);
    return configurer;
}
```

# 条件注入

可以通过 `@Conditional` 自定义注入条件。该注解也可以作为元注解。

# 函数注册

- `context.registerBean(name, ...) ` 注册 ` bean `
- `context.registerAlias(name, ...)` 注册 `bean` 别名

注册后，应使用 `refresh()` 方法刷新容器
