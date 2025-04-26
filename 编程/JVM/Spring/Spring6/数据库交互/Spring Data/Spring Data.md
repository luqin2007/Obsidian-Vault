Spring Data 是一个关系型数据库、NoSQL、大数据数据库等各类数据库的抽象层，减少样板代码，实现持久存储的数据访问层。

|项目|描述|
| -----------------------| -----------------------------------------------|
|Spring Data JPA|简化使用 JPA 进行数据访问的应用程序开发|
|Spring Data SoIr|简化使用 Apache SoIr 搜索服务器的配置和访问<br />|
|Spring Data MongoDB|简化使用 MongoDB 的应用程序开发|
|Spring Data Aerospike|简化使用 Aerospike 的应用程序开发|
|Spring Data Hadoop|简化使用 Apache Hadoop 的应用程序开发|

Spring Data 核心接口为 `Repository`，由 Spring 实现对实体进行 CRUD 操作。

```plantuml
interface Repository<T, ID>

interface CrudRepository<T, ID> extends Repository {
  +save(T entity)
  +findById(ID id)
  +deleteById(ID id)
}
interface PagingAndSortingRepository<T, ID> extends CrudRepository {
  +findAll(Sort sort)
  +findAll(Pageable pageable)
}

interface ReactiveCurdRepository<T, ID> extends Repository {
  +save(T entity): Mono
  +findById(ID id): Mono
  +deleteById(ID id): Mono
}
interface ReactiveSortingRepository<T, ID> extends ReactiveCurdRepository {
  +findAll(Sort sort): Flux
}

interface RxJava2CurdRepository<T, ID> extends Repository {
  +save(T entity): Single
  +findById(ID id): Maybe
  +deleteById(ID id): Complelable
}
interface RxJava2SortingRepository<T, ID> extends RxJava2CurdRepository {
  +findAll(Sort sort): Flowable
}
```

继承自 `Repository` 的 Dao 类，按规则创建的方法若没有实现，Spring 将创建对应代理实现。

```java
public interface FixedDepositSpringDataDao 
       extends CrudRepository<FixedDepositDetails, Integer> {

    FixedDepositDetails save(FixedDepositDetails fixedDepositDetails);

    FixedDepositDetails findFirstByFixedDepositId(int id);
}
```

可以使用 `@RepositoryDefinition` 注解标记

```java
@RepositoryDefinition(
 domainClass = FixedDepositDetails.class, idClass = Integer.class)
public interface FixedDepositSpringDataDao {

    FixedDepositDetails save(FixedDepositDetails fixedDepositDetails);

    FixedDepositDetails findFirstByFixedDepositId(int id);
}
```

实现 Spring Data 步骤如下：

1. [[配置 Spring Data JPA]]
2. 设置 `Repository` 类的[[Repository SQL 方法/Repository SQL 方法|查询方法]]

---

- [[Querydsl]]
- [[Spring Data MongoDB]]
