`NamedParameterJdbcTemplate` 是 `JdbcTemplate` 的一个包装类，将 SQL 中的 `?` 替换成以 `:` 开头的名称作为占位符

```reference
file: "@/_resources/codes/spring/jdbc-namedparameterjdbctemplate/src/main/java/com/example/mybank/BankConfiguration.java"
start: 31
end: 34
```

通过 Map 传入对应的参数；如果只有一个参数，可以直接创建 `SqlParameterSource`

该类包含多种 `query` 方法，`queryForObject` 可以通过返回值创建一个对象，`queryForList` 可以返回 `List` 等

```reference
file: "@/_resources/codes/spring/jdbc-namedparameterjdbctemplate/src/main/java/com/example/mybank/FixedDepositDao.java"
start: 18
end: 59
```
