开发一个基于 Spring JDBC 进行数据库交互的应用程序，需要执行以下两步操作：

> [!note] 依赖：`org.springframework.boot:spring-boot-starter-jdbc`

1. 配置数据源，通常为 `javax.sql.DataSource` 对象，有两种方法：

````tabs
tab: 三方库数据源

以 `Apache Commons DBCP 2` 为例，Spring 会自动配置好 `JdbcTemplate`
<br/>

```embed-java
PATH: "vault://_resources/codes/spring/jdbc-datasource-apachecommonsdbcp2/src/main/java/com/example/mybank/BankConfiguration.java"
LINES: "12-28"
```

tab: JNDI

使用 `jee` 模式的 `<jndi-lookup>` 元素
<br/>

```xml
<beans ...
       xmlns:jee="http://www.springframework.org/schema/jee"
       xsi:schemaLocation="...
                           http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee.xsd">
    <jee:jndi-lookup jndi-name="java:comp/env/jdbc/bankAppDb" id="dataSource" />
</beans>
```

<br/>
使用 Java 配置，使用 `JndiLocatorDelegate`。注意清除默认 `destroyMethod`，因为 JNDI 绑定的 DataSource 由应用程序服务器管理
<br/>

```java
@Bean(destroyMethod = "")
public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    JdbcTemplate template = new JdbcTemplate();
    template.setDataSource(dataSource);
    return template;
}
```
````

2. 实现使用 Spring 的 JDBC 模块类进行交互的 Dao

| 数据库操作类                         | 说明                                         |
| ------------------------------ | ------------------------------------------ |
| `javax.sql.DataSource`         | JavaEE(现在叫 Jakarta)中表示一个数据源                |
| [[JdbcTemplate]]               | Spring JDBC 基础数据库操作类，负责管理和执行 SQL 语言        |
| [[NamedParameterJdbcTemplate]] | `JdbcTemplate` 的包装类，可以使用 `Map` 传递 SQL 中的参数 |
| [[SimpleJdbcInsert]]           | 通过 `DataSource` 创建，简化 `insert` 语句          |
| `SimpleJdbcCall`               | 执行存储过程和函数                                  |
| [[MappingSqlQuery]]            | 以面向对象形式访问关系数据库，将每条 `ResultSet` 数据转换为一个对象   |
- [[使用 Hibernate 框架]]
- [[事务管理/事务管理|事务管理]]