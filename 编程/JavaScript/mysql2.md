MySQL 连接模块

```shell
npm i mysql2
```

1. 连接数据库

```javascript
const db = sql.createPool({  
    host: "数据库地址，本机为 127.0.0.1",  
    user: "用户名",  
    password: "登录密码",  
    database: "连接数据库名"  
})

db.promise() // 获取 Promise
```

2. 测试连接状态：使用 `select 1` 检查

```javascript
db.query('select 1', (err, ret) => {  
    // 输出 [ RowDataPacket { '1': 1 } ] 表示连接成功
    console.log(err ? err : ret)  
})
```

3. 执行 SQL 语句

```javascript
db.query("SEL 语句", (err, result) => {  ...  })
```

- 数据占位：使用 `?` 表示该位置内容待定，将在执行时传入；只有一个占位符时，可直接传值，否则用数组

```javascript
let user = {
    username: "he",
    password: ".asd"
}

// 这里使用 ? 占了两个位置
const sqlInsert = 'insert into users (username, password) values (?, ?)'

// 第二个参数传入一个数组，填充占位数据
db.query(sqlInsert, [user.username, user.password],
		 (err, ret) => console.log(err ? err : ret))
```

- 简化：若对象名和插入列名一一对应，则可使用 `SET ?` 作为占位符

```javascript
let user = {
    username: "he",
    password: ".asd"
}

// 这里使用 set ? 占位
const sqlInsert = 'insert into users set ?'

// 第二个参数传入一个对象，该对象属性名与数据库列名一一对应
db.query(sqlInsert, user, (err, ret) => console.log(err ? err : ret))
```

同理，可用于 `update` 等其他语句

```javascript
// 第一个 ? 对应 user 对象，第二个对应 user.id
db.query('update users set ? where id=?', [user, user.id], ...)
```