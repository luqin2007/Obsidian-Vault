`SimpleJdbcInsert` 是一个利用数据库元数据简化 `insert` 语句的辅助类，可以通过 `DataSource` 创建。

```reference
file: "@/_resources/codes/spring/jdbc-simplejdbcinsert/src/main/java/com/example/mybank/FixedDepositDao.java"
start: 14
end: 21
```

使用 `SimpleJdbcInsert` 可以直接通过参数构建 `insert` 语句，也可以直接获取通过 `usingGeneratedKeyColumns` 传入的主键，不需要使用 `KeyHolder`

```reference
file: "@/_resources/codes/spring/jdbc-simplejdbcinsert/src/main/java/com/example/mybank/FixedDepositDao.java"
start: 23
end: 31
```
