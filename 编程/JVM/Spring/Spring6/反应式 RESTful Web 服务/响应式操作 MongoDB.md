传统 SQL 数据库通常使用声明式的查询方式，而 NoSQL 提供响应式驱动。这里使用 MongoDB。

> [!note] 依赖：响应式 MongoDB 数据库驱动 `org.mongodb:mongodb-driver-reactivestreams`

MongoDB 的配置使用 `com.mongodb.reactivestreams` 包的对象，创建 `ReactiveMongoTemplate` 用于操作数据库

```java
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Bean
public MongoClient mongoClient() {
    return MongoClients.create("mongodb://localhost");
}
@Bean
public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
    ReactiveMongoDatabaseFactory factory = new SimpleReactiveMongoDatabaseFactory(mongoClient, "test" /*数据库名*/);
    return new ReactiveMongoTemplate(factory);
}
```

使用 SpringData 时，使 `Repository` 类接口继承自 `ReactiveMongoRepository`，返回 RxJava 的对象类型
- 如果没有返回值，则返回 `Completable`
- 如果原本返回值为 `Optional`，则返回 `MayBe`

> [!attention] 响应式数据库编程暂不支持 Querydsl

```java
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccountDetails, String> {

    Single<Long> countByBalance(float balance);

    Flowable<BankAccountDetails> removeByBalance(float balanceAmount);

    @Query("{'balance' : {'$lte':  ?0}}")
    Flowable<BankAccountDetails> findByCustomQuery(int balanceAmount);
}

```

也可以使用 Reactor 替代 RxJava，返回 `Mono` 和 `Flux`
- 如果没有返回值，则返回 `Mono<Void>`
- 如果原本返回值为 `Optional`，返回 `Mono`

```java
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccountDetails, String> {

    Mono<Long> countByBalance(float balance);

    Flux<BankAccountDetails> removeByBalance(float balanceAmount);

    @Query("{'balance' : {'$lte':  ?0}}")
    Flux<BankAccountDetails> findByCustomQuery(int balanceAmount);
}
```

```java
public interface BankAccountRepositoryCustom {

    Mono<Void> addFixedDeposit(String bankAccountId, int amount);
}
```

自定义函数可以通过 `ReactiveMongoTemplate` 操作数据库

```java
@Autowired
private ReactiveMongoTemplate template;

@Override
public Mono<Void> addFixedDeposit(String bankAccountId, int amount) {
    return template.findById(bankAccountId, BankAccountDetails.class)
            .map(bankAccount -> addFd(bankAccount, amount).subscribe())
            .then();
}

private Mono<BankAccountDetails> addFd(BankAccountDetails bankAccount, int amount) {
    if (bankAccount.getBalance() < amount) {
        throw new IllegalArgumentException("Not enough money on account");
    }
  
    FixedDepositDetails fixedDepositDetails = new FixedDepositDetails();
    fixedDepositDetails.setFdAmount(amount);
  
    bankAccount.getFixedDeposits().add(fixedDepositDetails);
    bankAccount.setBalance(bankAccount.getBalance() - amount);
    return template.save(bankAccount);
}
```

使用 `@EnableReactiveMongoRepositories` 替代  `EnableJpaRepositories` 注解

```java
@EnableReactiveMongoRepositories("com.example.webflux.repository")
```

如果使用 RxJava，可以使用 `rxjava-adapter` 库的 `RxJava3Adapter` 中的工具方法将 Reactor 的类转换成 RxJava 的类

> [!note] 依赖：`io.projectreactor.addons:reactor-adapter`
