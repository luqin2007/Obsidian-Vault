`JdbcTemplate` 管理 `Connection`，`Statement` 和 `ResultSet`，捕获 JDBC 异常转化为易于理解的异常，批处理操作等。

在依赖了 `spring-boot-starter-jdbc` 且注入了 `DataSource` 对象后，Spring 会创建一个默认 `JdbcTemplate`，类似如下：

```java
@Bean 
public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(dataSource);
    return jdbcTemplate;
}
```

之后，在 `Dao` 类中使用 `JdbcTemplate` 的 `update` 方法执行 SQL 语句即可。SQL 语句中使用 `?` 替代待传入参数，在 `PreparedStatement` 中根据位置传入参数，从 1 开始。

* `PreparedStatementCreator`：提供 `Connection`，执行 SQL 语句
* `KeyHolder`：在插入 SQL 语句时自动生成的键，默认实现是 `GeneratedKeyHolder`

>[!note] 可以通过 KeyHolder 获取 GENERATED_KEY 获取主键自增的主键，具体返回类可在 `PreparedStatement` 的构造函数中设置

```reference
file: "@/_resources/codes/spring/jdbc-jdbctemplate/src/main/java/com/example/mybank/FixedDepositDao.java"
start: 16
end: 33
```

利用 `batchUpdate` 方法可以在同一个 `PreparedStatement` 上批量执行 `update` 语句
