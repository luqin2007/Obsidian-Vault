Spring Validation API 提供 `Validator` 接口，允许对应用程序层中的对象进行验证。

1. 创建一个 `org.springframework.validation.Validator` 接口的实现类：

* `supports`：校验给定 `bean` 类型是否需要校验
* `validate`：进行校验，若出现错误，将错误放入 `Errors` 参数中

```reference
file: "@/_resources/codes/spring/annotation-validator/src/main/java/com/example/mybank/FixedDepositValidator.java"
start: 6
end: 20
```

2. 在需要的地方进行校验

```reference
file: "@/_resources/codes/spring/annotation-validator/src/main/java/com/example/mybank/FixedDepositDao.java"
start: 7
end: 17
```

基础的 `Validator` 校验接口可以直接使用，基于 JSR380 与 Hibernate Validator 的校验需要依赖于 Spring Validation API

同时，Spring 也支持 JSR380 对方法的校验。
- [[JSR380 注解约束]]
- [[JSR380 方法校验]]

> Spring Validation API 需要引入 `org.springframework.boot:spring-boot-starter-validation` 依赖，包含 JSR380 与 Hibernate Validator
>
> JSR380 单独使用需要引入 `jakarta.validation:jakarta.validation-api` 依赖
>
> Hibernate Validator 提供了 JSR380 的参考实现，并依赖于 JSR341（统一表达式语言）
