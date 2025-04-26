```java
public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```

`Spring` 在初始化时通过 `setApplicationContext` 方法传入 `ApplicationContext` 对象。

```reference
file: "@/_resources/codes/spring/applicationcontext-aware/src/main/java/com/example/mybank/ConsumerRequestService.java"
start: 7
end: 14
```

`ApplicationContextAware` 接口是一个生命周期接口。`setApplicationContext` 方法会在 `bean` 对象实例化后，初始化前调用。

> [!warning] 使用 `ApplicationContextAware` 会使类直接依赖 `ApplicationContext`，造成耦合，不推荐使用

