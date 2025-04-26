在 `application.properties` 中可以配置多个数据源，使用不同的名称

```properties
# 用于存放数据的数据库
spring.datasource.url=jdbc:mysql://localhost:3306/spring_bank_app_db
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
# 用于安全检查的数据库
spring.security-database.url=jdbc:mysql://localhost:3306/securitydb
spring.security-database.driverClassName=com.mysql.cj.jdbc.Driver
```

之后，需要在代码中初始化数据库，使用 `@ConfigurationProperties` 指定前缀

必要情况下可以使用 `@Primary` 设置主数据库

```java
@Primary
@Bean
@ConfigurationProperties(prefix = "spring.datasource")
public DataSource dataSource() {
    return DataSourceBuilder.create().build();
}
@Bean
@ConfigurationProperties(prefix = "spring.security-datasource")
public DataSource securityDataSource() {
    return DataSourceBuilder.create().build();
}
```
