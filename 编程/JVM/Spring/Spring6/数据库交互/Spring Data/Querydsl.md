一种 JPQL 查询的替代方案，与 JPA Criteria API 相比，查询类型是安全的，编写起来也更加紧凑。

Querydsl 通过 `JPAAnnotationProcessor` 注解处理器生成元模型类，位于 `target/generated-sources/annotations` 中，命名规则为 `Q<domain-entity-name>`

>[!note] 依赖：`com.querydsl.querydsl-apt`， `com.querydsl.querydsl-jpa`

> Gradle 依赖配置：
>
> 指定 `jakarta` 分支并添加 `jakarta.persistence`，并开启注解处理器，build 一次后即可生成 Q 类
>
> ```gradle
> // https://mvnrepository.com/artifact/com.querydsl/querydsl-jpa
> implementation group: 'com.querydsl', name: 'querydsl-jpa', version: '5.1.0', classifier: 'jakarta'
> // https://mvnrepository.com/artifact/com.querydsl/querydsl-apt
> implementation group: 'com.querydsl', name: 'querydsl-apt', version: '5.1.0', classifier: 'jakarta'
>
> annotationProcessor group: 'jakarta.persistence', name: 'jakarta.persistence-api', version: '3.1.0'
> annotationProcessor group: 'com.querydsl', name: 'querydsl-apt', version: '5.1.0', classifier: 'jakarta'
> ```
>
> ![[image-20240405182415-n4979kv.png]]

在 `Repository` 类上实现 `QuerydslPredicateExecutor` 接口即可，其他配置与普通 Repository 想同

```reference
file: "@/_resources/codes/spring/data-querysql/src/main/java/com/example/mybank/dao/BankAccountRepository.java"
start: 7
end: 8
```

查询时，可以使用生成的 Q 类创建 `Predicate`。但 Querydsl 不支持更新，因此保存需要依赖于 `JpaRepository`

```reference
file: "@/_resources/codes/spring/data-querysql/src/main/java/com/example/mybank/service/BankAccountService.java"
start: 11
end: 28
```

Querydsl 还支持根据实例查询，通过 `QueryByExampleExecutor` 接口实现。对于给定实例，Querydsl 自动忽略 `null` 值，其他需要排除的需要通过 `ExampleMatcher` 设置

```reference
file: "@/_resources/codes/spring/data-querysql/src/main/java/com/example/mybank/service/FixedDepositService.java"
start: 20
end: 25
```
