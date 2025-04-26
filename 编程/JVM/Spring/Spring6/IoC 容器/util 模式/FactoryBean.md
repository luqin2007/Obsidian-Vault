```java
public interface FactoryBean<T> {
    @Nullable T getObject() throws Exception;
    @Nullable Class<?> getObjectType();
    default boolean isSingleton() { return true; }
}
```

相关方法说明：

|方法|说明|
| ------| ------------------------|
|`getObject`|由该 `FactoryBean` 管理的对象|
|`getObjectType`|由该 `FactoryBean` 管理的对象类型|
|`isSingleton`|是否是 `singleton` 范围|

实现了 `FactoryBean` 接口的类可以像普通 `bean` 一样在 XML 中配置，但在解析时，通过 `<bean-id>` 获取的是通过 `getObject` 创建的值，通过 `&<bean-id>` 获取的是 `FactoryBean` 对象。

> [!caution] 在 XML 中，使用 `&amp;` 代替 `&`（即 `ref="&amp;<bean-id>"`）

```reference
file: "@/_resources/codes/spring/ioc-util/src/main/java/com/example/mybank/ExampleFactoryBean.java"
start: 5
end: 21
```

```reference
file: "@/_resources/codes/spring/ioc-util/src/main/resources/applicationContext.xml"
start: 31
end: 31
```

通过 `example-factory` 获取的是 `getObject` 的返回值 `hello world`

```reference
file: "@/_resources/codes/spring/ioc-util/src/test/java/TestFactoryBean.java"
start: 13
end: 13
```

通过 `&example-factory` 获取的是 `ExampleFactoryBean` 实例

```reference
file: "@/_resources/codes/spring/ioc-util/src/test/java/TestFactoryBean.java"
start: 14
end: 14
```
