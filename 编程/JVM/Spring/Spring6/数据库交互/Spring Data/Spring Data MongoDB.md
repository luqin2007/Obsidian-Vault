MongoDB 是一个 NoSQL 数据库，由 字段 - 值 的结构组成，类似 json 对象。

每个文件都有一个 `_id` 字段，该字段由 Mongodb 自动生成，作用类似主键。

> [!note] 依赖（Spring）：`org.springframework.boot:spring-boot-starter-data-mongodb`

> [!note] 依赖（MongoDB）：`org.mongodb:mongodb-driver-sync`，`org.mongodb:mongodb-driver-core`

> [!note] 依赖（Querydsl）：`com.querydsl:querydsl-mongodb`

1. 创建实体类，即前面 JPA 的 `@Entity` 类

```reference
file: "@/_resources/codes/spring/data-mongodb/src/main/java/com/example/mybank/domain/BankAccountDetails.java"
start: 9
end: 16
```

```reference
file: "@/_resources/codes/spring/data-mongodb/src/main/java/com/example/mybank/domain/FixedDepositDetails.java"
start: 6
end: 11
```

	* 使用 `@Document` 注解表示一个 MongoDB 文档建模域实体，`@Id` 表示主键字段
    * `FixedDepositDetails` 类作为 `BankAccountDetails` 的一个嵌入对象，不需要一个单独的文档，故没有 `@Document` 注解
    * `FixedDepositDetails` 需要唯一标识符识别定期存款，手动使用 `ObjectId.get()` 方法要求分配一个 ID（否则 `@Id` 也无效）

> [!note] 使用 `Querydsl`，需要将这两个类也使用 `@Entity`，用于生成 Q 类

2. 配置 MongoDB 连接

```reference
file: "@/_resources/codes/spring/data-mongodb/src/main/java/com/example/mybank/config/MyBankConfig.java"
start: 14
end: 33
```

`@EnableAsync` 表示允许异步。使用 `XML` 配置时使用 `mongo` 命名空间

```reference
file: "@/_resources/codes/spring/data-mongodb/src/main/resources/applicationContext.xml"
start: 4
end: 11
```

3. 创建 `Repository` 接口类型，继承 `MongoRepository`。若需要扩展方法，使用 `Repository` 的扩展方式。

```reference
file: "@/_resources/codes/spring/data-mongodb/src/main/java/com/example/mybank/repository/BankAccountRepository.java"
start: 12
end: 21
```

MongoDB 也支持 Querydsl。处理如 `List<FixedDepositDetails> fixedDeposits` 的数据时，使用 `.any()` 表示遍历，类似 `filter`

```java
BankAccountDetails bankDetails = new BankAccountDetails();
ExampleMatcher matcher = ExampleMatcher.withIngore("account", "balance", "lastTransactionTemplate");
```
