> [!note] JPQL，JavaPersistence Query Language，Java持久化查询语言，针对关系数据库创建 SQL 查询语言。

JPQL 可以使用一条或批量的 `select`，`update`，`delete` 子句，语法与 `sql` 基本相同。

# EJB-QL

EJB-OL 用于描述查询内存对象，类似 SQL 语言

## select

```sql
select [distinct] <值> from <对象类型> [as] <别名> [where ...]
```

语句中，使用 `?n` 表示第几个参数，n 从 1 开始

```sql
select ... from Entity e where e.capacity > ?1
```

查询返回值可以为：

* 对象类型 `T` 或 `Optional<T>`，查询出 0 或 1 条结果
* `Collection<T>` 集合
* `Set<T>`，但必须是 `distinct` 的

对象类型为实体类名。

## 成员访问

直接使用 `.` 访问类成员

```sql
-- 访问 Entity#capacity 成员
select ... from Entity e where e.capacity > 100
```

对于集合，支持 `in` 操作符遍历查询

```sql
-- 访问 Entity#rooms 集合
select ... from Entity e, in e.rooms [as] r where r.smoking = true
```

## 条件操作符

* 支持 `and`，`or`，`not`，`between ... and ...` 条件连接符
* 支持 `like` 模糊查询和 `%`，`_`
* `is null` 检查对象是否为 `null`
* `in strings` 检查字符串是否在一组字符串集合中

  ```sql
  select ... from Entity e where e.name in ("a", "b", ?1)
  ```
* `member [of]` 检查值是否在一个集合中

  ```sql
  select ... from Entity e where ?1 member e.rooms
  ```
* `is empty` 检查集合是否为空

# JPQL

JPQL 是 EJB-QL 的扩展，由 JPA 转换为 SQL 语言，相比于 EJB-QL 增加了以下功能：

* 可以执行连接操作
* 可以批量更新和删除数据
* 可以使用排序和分组子句执行聚合函数（与 SQL 语句相同）
* 支持单值和多值结果类型

JPQL 支持使用 `:name` 表示命名参数的参数名

```sql
select ... from Entity e where e.capacity > :cap
```

通过 `EntityManager` 接口创建查询，返回一个 `Query` 对象

|方法|说明|
| ------| ------------------------|
|`createQuery`|创建一个查询|
|`createNamedQuery`|创建一个 `@NamedQuery` 声明的查询|

使用 `@NamedQuery` 声明的查询称为静态查询

`Query` 对象表示一个查询。刚创建的查询并没有执行

|方法|说明|
| ------| -----------------------------|
|`executeUpdate`|创建更新和删除操作|
|`setParameter`|设置参数（语句中 `?n` 或 `:name`）|
|`getXxxResult`|获取结果|
