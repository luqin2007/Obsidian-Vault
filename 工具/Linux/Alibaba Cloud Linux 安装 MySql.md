# 安装 MySQL 服务

`sudo dnf install mysql-server`

 ![[Pasted image 20231201162451-20240513025628-0ku3b9z.png]] 

# 运行 MySQL 服务

MySQL 8.0 默认服务名为 `mysqld`，先检查当前服务状态：`systemctl status mysqld`

![[../../_resources/images/Pasted image 20231201162714-20240513025636-sx3mwk3.png]]

启动并确认服务状态：`systemctl start mysqld`

![[../../_resources/images/Pasted image 20231201163046-20240513025642-6xp4vvm.png]]

# 配置 MySQL root 用户

通过查看 mysqld.log，可见默认 MySQL root 账号无密码：`sudo vi /var/log/mysql/mysqld.log`

![[../../_resources/images/Pasted image 20231201163847-20240513025657-7pwqs5n 1.png]]

直接登录 MySQL root 账号，修改密码：

```
mysql -uroot
set password for root@localhost = '_P4mJej7s=eWuxsQcjwL';
```

![[../../_resources/images/Pasted image 20231201164456-20240513025715-5qipgcp.png]]

之后退出重新登陆检查是否设置成功。

![[../../_resources/images/Pasted image 20231201164558-20240513025722-huwobn2.png]]

# 开放远程连接

下一步是开放远程连接，用于在调试期间查看数据库能否按我们要求的进行修改。创建一个账户 `Host=%` 即可允许所有外部连接。同时需要开放 3306 端口。

```mysql
use mysql;
# 用于管理的用户
create user db_questions_admin@'%' identified by '3U4jcX_Kcyx5iMDr9_uu';

# 权限设置
use db_questions;
grant all privileges on db_questions to db_questions_admin@'%';
```

![[../../_resources/images/Pasted image 20231203170514-20240513025822-7dtl52z 1.png]]

# 自增主键

向数据库插入数据时，自增主键的值可以通过 `GeneratedKeyHolder` 获取：

```kotlin
val keyHolder = GeneratedKeyHolder()
val sql = "insert into 课程信息表(课程名称) value (?);"
val creator = PreparedStatementCreator {
    val ps = it.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
    ps.setString(1, lessonName)
    return@PreparedStatementCreator ps
}
template.update(creator, keyHolder)
return keyHolder.key!!.toInt()
```
