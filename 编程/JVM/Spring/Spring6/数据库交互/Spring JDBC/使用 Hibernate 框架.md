Spring ORM 模块提供与 Hibernate，Java Persistence API（JPA），Java 数据对象（JDO）的集成。

Hibernate 是一个 JPA 提供程序，可以使用 JPA 注解将持久实体映射到数据库表。

>[!note] 依赖：`org.hibernate.orm:hibernate-core`

> [!note] 依赖：`org.springframework:spring-orm`

1. 配置持久类，将持久类使用 JPA 的 `@Entity` 等注解配置好

```reference
file: "@/_resources/codes/spring/jdbc-hibernate/src/main/java/com/example/mybank/domain/FixedDepositDetails.java"
start: 7
end: 28
```

> [!note] 自定义映射
> 如果需要自定义映射规则，可使用 `spring.jpa.properties.hibernate.physical_naming_strategy` 配置一个 `PhysicalNamingStrategy` 实现
> - 可继承自 `PhysicalNamingStrategyStandardImpl`

2. 配置 `SessionFactory`：创建 Hibernate 的 Session 对象的工厂
	- 使用 `packageToScan` 指定一个包用于自动扫描，或使用 `annotatedClasses` 手动指定持久类

````tabs
tab: Java

```embed-java
PATH: "vault://_resources/codes/spring/jdbc-hibernate/src/main/java/com/example/mybank/config/MyBankConfig.java"
LINES: "33-39"
```

tab: XML

```embed-xml
PATH: "vault://_resources/codes/spring/jdbc-hibernate-xml/src/main/resources/applicationContext.xml"
LINES: "21-24"
```
````

3. 创建使用 `Hibernate API` 交互的 Dao

````tabs
tab: Java

```embed-java
PATH: "vault://_resources/codes/spring/jdbc-hibernate/src/main/java/com/example/mybank/dao/FixedDepositDao.java"
LINES: "13-35"
```

tab: XML

```embed-java
PATH: "vault://_resources/codes/spring/jdbc-hibernate-xml/src/main/java/com/example/mybank/dao/FixedDepositDao.java"
LINES: "9-31"
```
````
