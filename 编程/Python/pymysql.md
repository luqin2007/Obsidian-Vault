Python 操作 MySQL 数据库的一个库。
## 连接

```python
from pymysql import Connection

conn = Connection(
    # 主机地址
    host='localhost',
    # 登陆用户名
    user='***',
    # 登陆密码
    password='***'
)

with conn:
    # 使用数据库
    pass
```
## 操作

执行 SQL 语句，主要有两种：查询语句与非查询语句。
### 执行

首先要选择表

```python
with conn:
    conn.select_db('mydatabase')
```

之后，使用 `conn.cursor()` 获取 `cursor` 对象，并通过该对象执行 SQL 语句

```python
with conn:
    conn.select_db('mydatabase')
    cur = conn.cursor()
    ret = cur.execute('create table test_table ('
                      'id smallint primary key auto_increment,'
                      'name varchar(50) not null'
                      ') comment \'test database\'')
```
### 查询

若 SQL 语句为查询语句，则 `cur.execute` 方法返回数据条数。使用 `fetch` 系列函数可获取查询结果，返回类型为元组

```python
with conn:
    conn.select_db('mydatabase')
    cur = conn.cursor()
    # 8
    count = cur.execute('show databases')
    print(count)
  
    # (('information_schema',), 
    #  ('mydatabase',), 
    #  ('mysql',),
    #  ('performance_schema',),
    #  ('sakila',),
    #  ('sys',),
    #  ('test',),
    #  ('world',))
    db = cur.fetchall()
    print(db)
```
### 修改

若要修改数据库内容，不管是针对数据库、表还是数据，均需要 `conn.commit()` 提交

```python
with conn:
    conn.select_db('mydatabase')
    cur = conn.cursor()
    cur.execute('insert into test_table (name) values (\'lq\')')
    conn.commit()
```

在 `Connection` 构造中有一个参数 `autocommit` 可用于开启自动提交

```python
conn = Connection(
    host='localhost',
    user='***',
    password='***',
    # 自动提交
    autocommit=True
)
```
# 其他可用驱动

* MYSQL-python：仅支持 Python 2
* mysqlclient：MYSQL-python 的 Python3 分支，执行效率最高，但需要配置环境
* pymysql：纯 Python 实现，效率不如 mysqlclient 但可以与 Python 无缝衔接
* mysql-connector-python：MySQL 提供的纯 Python 驱动，效率不如 pymysql
