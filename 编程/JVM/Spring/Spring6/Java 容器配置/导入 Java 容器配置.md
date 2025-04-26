使用 `@Import` 和 `@ImportResource` 导入其他容器，相当于 `<import>` 标签。

* `@Import<AnotherConfig.class>`：导入被 `@Configuration` 注解的类
* `@ImportResource(path)`：导入 XML 配置的容器

```java
@Configuration
public class BankAppPropConfig {
}

@Import(BankAppPropConfig.class)
@ImportResource("applicationContext.xml")
@Configuration
public class BankAppDomainConfig {
    // ...
}
```

‍
