IoC 容器的配置可以通过 `xml` 或者注解完成。这里以 `xml` 的配置为例。

```reference
file: "@/_resources/codes/spring/ioc/src/main/resources/applicationContext.xml"
```

`<beans>` 为根标签，每个由 IoC 创建的对象都由 `<bean>` 标签配置。
* `id` 属性为该对象的唯一名称
* `class` 属性为对象实际类型

上面代码如果写成 Java，大概相当于：

```java
FixedDepositService fdServer = new FixedDepositService();
FixedDepositController fdController = new FixedDepositController(fdServer);
```

- [[基于构造器的 DI|基于构造器的 DI]]
- [[基于 setter 的 DI]]
- [[作用域与延迟初始化]]
- [[配置继承]]
- [[属性编辑器注入/属性编辑器注入|属性编辑器注入]]
- [[p 与 c 命名空间]]
- [[util 模式/util 模式|util 模式]]
- [[模块化 bean]]
- [[内部 bean]]

注入到 IoC 容器后，代码中可以通过 `ApplicationContext` 获取：

```reference
file: "@/_resources/codes/spring/ioc/src/main/java/com/example/mybank/MainApplication.java"
start: 9
end: 12
```

> [!note] 面向接口编程
>
> 依赖接口而非具体实现类，使依赖类与依赖项之间松耦合。
>
> * 创建引用依赖接口
> * 定义 `<bean>` 元素，注入具体实现类
