`EvaluationContext` 在计算 SpEL 表达式时，提供上下文的属性、方法、字段等，也可以实现类型转换，还负责反射、缓存等

`StandardEvaluationContext` 是 `EvaluationContext` 的一个实现。

> [!caution] `StandardEvaluationContext` 构造成本较高且自带缓存，应尽可能复用上下文

# 上下文

* `setRootObject`：设置根对象，根对象可以忽略对象名直接访问成员，也可以通过 `#root` 访问

```reference
file: "@/_resources/codes/spring/spel/src/test/java/ContextTest.java"
start: 20
end: 25
```

* `setVariable`：注册上下文对象，使用 `#对象名` 访问
* `registerFunction`：注册上下文函数，使用 `#函数名` 访问
* 自定义 `ConstructorResolver`，`MethodResolver`，`PropertyAccessor` 扩展 SpEL 表达式

# 类型转换

默认情况下，SpEL 使用 Spring Core 的 `ConversionService` 服务进行类型转换。
* 内置常用转换，同时也保留了可扩展性
* 支持泛型感知

```reference
file: "@/_resources/codes/spring/spel/src/test/java/ContextTest.java"
start: 30
end: 35
```
