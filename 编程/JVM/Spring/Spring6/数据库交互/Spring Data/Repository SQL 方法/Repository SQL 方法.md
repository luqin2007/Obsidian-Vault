`Repository` 接口的方法可以不实现，Spring 默认根据方法名生成对应的代理实现。设 `T` 为实体类

* 数据库查找：即 `select` 语句
	* 查找语句方法签名格式为 `find<查找条目>By<查找条件>(..., [Pageable/Sort])`
  * 查找条目：可选，包括 All，First，Top。
    * `findAll` 查找所有条目，返回 `List<T>`
    * `findFirst` 或 `findTop` 表示查找第一条符合条件的条目，返回 `T` 或 `Optional<T>`
    * `findFirstN` 或 `findTopN` 表示查找前 N 条符合条件的条目，返回 `List<T>`
  * 设置查找条件，即 `where` 后的内容，且查找内容应与形参匹配
    * `findById(int id)` 相当于 `select ... where id = :id`
    * `findByIdAndName(int id, String name)` 相当于 `select ... where id = :id and name = :name`。连接词还支持 `Or`
    * `findByIdLessThanOrAgeGreaterThan(int id, int age)` 相当于 `select ... where id < :id or age > :age`
  * 分页：`find...(..., Pageable pageable)`，`Pageable` 表示分页访问，返回 `List<T>`，`Page<T>` 或 `Slice<T>`
    * `List<T>`：仅返回查询页的内容
    * `Page<T>`：额外查询实体总数
    * `Slice<T>`：额外提供是否包含下一页
  * 排序：`find...(..., Sort sort)`，`Sort` 表示排序方式
  * 可返回 `List<T>` 的对象还可以返回 `Stream<T>`，使用流不需要等待所有结果全部查询，而是查询出一个结果后立刻返回
	  * 异步：使用 `@Async` 注解标注，并返回 `CompletableFuture`，`Future` 或 `ListenableFuture`，且使用 `@EnableAsync` 注解 `@Configuration` 注解的类
	  * 自定义：使用 `@Query` 注解可以自定义 [[JPQL 查询语句]]

 > [!attention] 使用 `Stream` 查询，应当在使用后调用 `close` 方法或使用 `try-resource` 结构

```java
@Async
CompletableFuture<List<FixedDepositDetails>> findAllByFdAmount(int fdAmount);
```

* 保存：`T save(T value)`，相当于 `insert` + `update`
* 删除：`void delete(T value)`，`deleteBy...(...)`
* 计数：`countBy...(...)`，相当于 `select count(*) ...`

如果需要自定义实现，可以使用 `@PersistenceContext` 注入 `EntityManager` 对象进行操作。

>[!note] 所有接口和实现类上都不需要注解，只需要在 `@EnableJpaRepositories` 的 `basePackages` 属性指定包名即可

1. **如果有自定义实现的需求**，创建一个新接口放置自定义实现的内容，例如 `~Custom`

```reference
file: "@/_resources/codes/spring/data-repositorysql/src/main/java/com/example/mybank/dao/BankAccountRepositoryCustom.java"
start: 3
end: 6
```

2. 创建 `~Impl` 实现类，`Impl` 后缀由 `@EnableJpaRepositories` 决定

```reference
file: "@/_resources/codes/spring/data-repositorysql/src/main/java/com/example/mybank/dao/BankAccountRepositoryImpl.java"
start: 7
end: 20
```

3. 创建接口，实现 `Repository` 和 `~Custom` 接口，同时按命名加入其他需要的查询方法

```reference
file: "@/_resources/codes/spring/data-repositorysql/src/main/java/com/example/mybank/dao/BankAccountRepository.java"
start: 6
end: 7
```

4. 在需要的地方（`Service` 类）直接注入使用即可

```reference
file: "@/_resources/codes/spring/data-repositorysql/src/main/java/com/example/mybank/service/BankAccountService.java"
start: 9
end: 19
```
