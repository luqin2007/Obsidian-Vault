基于构造器的注入包括构造函数和工厂函数，值通过 `<constructor-arg>` 子标签传入

# 匹配位置

使用 `index` 指定参数位置。

```reference
file: "@/_resources/codes/spring/ioc-constructor/src/main/resources/applicationContext.xml"
start: 10
end: 14
```

> [!note] 基于构造函数的 DI 与基于 `setter` 的 DI 可以组合使用

使用工厂方法创建对象时，工厂方法的参数被认为是构造函数的参数

```reference
file: "@/_resources/codes/spring/ioc-constructor/src/main/java/com/example/mybank/dao/FixedDepositDaoFactory.java"
start: 5
end: 15
```

在 `bean` 中创建对应 `factory` 以及对应参数：

```reference
file: "@/_resources/codes/spring/ioc-constructor/src/main/resources/applicationContext.xml"
start: 16
end: 26
```

# 匹配类型

> [!attention] 存在多个参数类型相同时无法使用。

如果值为 `bean` 对象引用，使用 `ref` 指定 `id`；如果是简单的 Java 类型（基本类型、`String`），则使用 `value` 指定值字面量。

当几个参数之间没有继承关系时，可以直接引用对象，Spring 可以根据类型判断实参位置。

```reference
file: "@/_resources/codes/spring/ioc-constructor/src/main/java/com/example/mybank/service/ServiceTemplate.java"
start: 13
end: 17
```

```reference
file: "@/_resources/codes/spring/ioc-constructor/src/main/resources/applicationContext.xml"
start: 28
end: 32
```

若两个参数类型有继承关系，使用 `type` 属性确定具体类型。

# 匹配名称

使用 `name` 属性可以指定构造函数参数名称

```reference
file: "@/_resources/codes/spring/ioc-constructor/src/main/java/com/example/mybank/service/TransferFundsServiceImpl.java"
start: 12
end: 18
```

```reference
file: "@/_resources/codes/spring/ioc-constructor/src/main/resources/applicationContext.xml"
start: 34
end: 39
```

使用 `name` 属性需要至少满足以下条件之一，以保证形参名在运行时可见：

* 编译时启动了参数名称发现的调试标志 `javac -parameters`
* 构造函数使用 `@ConstructorProperties` 注解标记

> [!attention] `@ConstructorProperties` 仅能修饰构造函数，因此开启调标志是工厂方法使用 `name` 属性的唯一选择。

