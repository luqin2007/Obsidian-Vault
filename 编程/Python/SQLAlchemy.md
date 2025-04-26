Python 的 ORM 框架，主要有Engine 框架引擎、Connection Pool连接池、Dialect （DB API 类型）、Schema/Type 数据库架构类型、SQL 表达式语句五部分组成

* 开发效率高：SQLAlchemy 几乎不需要调用原生 SQL 语句
* 安全性强：ORM 最底层会自动修复一些安全问题，如 SQL 注入等
* 灵活性强：一套代码，可适配 SQLite，MySQL，Oracle，PostgreSQL 等多种关系型数据库，不用关心底层 SQL 语言细节

ORM：Object Relation Mapping，对象关系映射，解决面向对象语言与关系型数据库之间相互不匹配的技术，在提供持久化类与表的映射关系的情况下，将对象持久化到数据库中

![[Pasted image 20230730130431-20240513185636-9v81drv.png]]

```shell
pip3 install sqlalchemy
```

# 连接与绑定

根据使用的连接驱动不同，使用的连接字符串也不同

* MYSQL-Python：`mysql+mysqldb://user:pwd@host:port/dbname`
* pymysql：`mysql+pymysql://user:pwd@host:port/dbname?options`
* MYSQL-Connector：`mysql+mysqlconnector://user:pwd@host:port/dbname`
* `cx_Oracle`：`oracle+cx_oracle://user:pwd@host:port/dbname?k=v&k=v...`

根据连接字符串就可以创建引擎并绑定会话了

```python
from sqlalchemy import create_engine
# 创建 Engine
engine = create_engine(
    url='mysql+pymysql://root:...@localhost:3306/db1?charset=utf8mb4',
    # 连接池大小
    pool_size=10,
    # 超过连接池后，最大还允许创建几个连接
    max_overflow=0,
    # 连接超时，单位 秒
    pool_timeout=10,
    # 多久回收一次连接池的连接
    pool_recycle=1,
    # 是否列出执行的 SQL 语句
    echo=True
)

from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm import scoped_session
# 绑定会话
Session = sessionmaker(bind=engine)
# 开启一个会话
session = scoped_session(Session)
```
# 表映射

将一张表映射到一个 Python 类需要以下步骤：

1. 使用 `declarative_base()` 获取基类，并使类继承自该基类
2. 填充表字段

    * `__tablename__`：表名
    * `Column` 类变量：字段
    * `__table__args`：约束与索引

```python
# 用于创建基类
from sqlalchemy.orm import declarative_base
# 数据库中的数据类型
from sqlalchemy import Integer, String, Enum, DECIMAL, DateTime
# 字段
from sqlalchemy import Column
# 索引及约束
from sqlalchemy import Index, UniqueConstraint


Base = declarative_base()

class XxxTableData(Base):
    # 表名
    __tablename__ = '表名'
    # 字段
    id = Column(Integer, primary_key=True, autoincrement=True, comment='主键自增')
    name = Column(String(32), index=True, nullable=False, comment='姓名，非空')
    phone = Column(DECIMAL(6), nullable=False, unique=True, comment='电话，非空唯一')
    gender = Column(Enum('male', 'female'), comment='性别')
    addr = Column(String(64), comment='地址')
    create_time = Column(DateTime, default=datetime.datetime.now, comment='创建时间，默认当前时间')
    # 约束及索引
    __table__args__ = (
        UniqueConstraint('name', 'phone'),          # 联合唯一约束
        Index('name', 'addr', unique=True)  # 联合唯一索引
    )
```
# 表操作

使用 `Base.metadata` 可对表进行增删操作，但无法修改 - 只能删除重建，或者自己执行 SQL 语句

```python
meta = Base.metadata     # type:sqlalchemy.schema.MetaData
meta.drop_all(engine)    # 删除所有表
meta.create_all(engine)  # 根据之前定义的表创建所有表
```

记录操作基本使用 `session`
## 增加

```python
# 增加一条数据并关闭连接
instance = XxxTableData(
    name='Name',
    phone=123456,
    addr="Addr"
)                       # 准备数据
session.add(instance)   # 添加, add_all 可以一次提交多条
session.commit()        # 提交
session.close()         # 关闭，或使用 session.remove() 回收
```
## 修改

```python
# update 表名 set name=name+'son' where name='Jake'
(session.query(XxxTableData)
 .filter_by(name='Jake')
 .update({'name': XxxTableData.name + 'son'}, synchronize_session=False))

session.commit()        # 提交
session.close()         # 关闭，或使用 session.remove() 回收
```
## 删除

```python
# delete from 表名 where name='Jake'
(session.query(XxxTableData)
 .filter_by(name='Jake')
 .delete())

session.commit()        # 提交
session.close()         # 关闭，或使用 session.remove() 回收
```
# 表查询
## 一般查询

```python
session.query(表或列).filter(条件)...

session.commit()        # 提交
session.close()         # 关闭，或使用 session.remove() 回收
```

* 表或列：即查询的列及 `from` 字句

  * 可以是数据表（如 XxxTableData），相当于 `select * from 表名`
  * 可以是数据表的几个字段对象（以下简称字段），相当于 `select 列名 from 表名`
  * 可以是数据表字段的 `label(name)` 方法返回值，相当于 `select 列名 as name from 表名`
* `where` 字句：

  * `filter(字段 ==/>=/<= 值)`：自定义的运算符返回的不是 bool

    * `sqlalchemy.and_`：and 连接的多个条件
    * `sqlalchemy.or_`：or 连接的多个条件
    * `sqlalchemy.not_`：not 修饰的条件
    * `字段.between(a, b)`：字段 between a and b
    * `字段.in_((...))`：字段 in (...)

      * `~数据字段.in_((...))`：字段 not in (...)
    * `字段.like(str)`：字段 like str
    * `字段.is_(obj)`：字段 is obj
  * `filter_by(**kvargs)`：判断某个键与值是否相等，相当于 `where k=v`
* limit：分页
* order by：使用 `order_by(字段.asc())`，`order_by(字段.desc())`

  * having：使用 `having` 方法
* 聚合函数：使用 `sqlalchemy.func` 包下的函数包装
* 数据获取：可使用 `.all()[:]` 对 `all()` 返回的值进行切片

  * 仅返回第一个：`.first()`

    * `one()`，`one_or_null()`：第一个，表空则异常或 NULL
  * `all()` 返回的值支持 for 循环和 `len()` 长度查询
  * `get(n)`：返回第 n 个
  * `exists()`：是否非空
  * `count()`：个数

## join

SQLALchemy 只支持 LEFT JOIN 查询，RIGHT JOIN 只要交换下表位置即可。

* `join(右表, 连接条件, isouter=True)`

  * 右表：联查的右表
  * 连接条件：on 字句
  * isouter：True 表示 OUTER，False 表示 INNER

## union

这里的第一个查询、第二个查询是指 `all()`，`first()` 之前的

* `第一个查询.union(第二个查询)`
* `第一个查询.union_all(第二个查询)`

## 子查询

子查询通过 `subquery` 创建

* `第一个查询.subquery()`

## relationship

若两个包存在外键的关联，可使用 `relationship` 直接根据外键访问另一个表的数据

```python
from sqlalchemy import ForeignKey
from sqlalchemy.orm import relationship

class Address(Base):
    __tablename__ = 'addresses'
    # ...
    # 外键 连接到 users 表的 id 字段
    user_id = Column(Integer, ForeignKey('users.id'))
    # user 字段连接到 User 类对应的表（即 users 表）
    # 会自动找到连接到 users 表的外键，即 user_id，并以此查询 users 表数据返回
    user = relationship('User', back_populates='addresses')


class User(Base):
    __tablename__ = 'users'
    # ...
    id = Column(Integer)
```

如果需要处理多对多的关系，则需要一张中间表（可没有具体表只有对象），中间表保存两个外键都是主键，分别连接两张表；创建 relationship 时传入中间表即可

```python
# 每一个 Post 都有多个 Keyword
# 每一个 Keyword 都有多个 Post
# 我们不需要具体的表类，有一个对象用于查询的中间联系表即可
# 但表名不能与现有表重复
from sqlachemy import Table
post_keywords = Table('post_keywords', Base.metadata,
					 Column('post_id', ForeignKey('posts.id'), primary_key=True),
					 Column('keyward_id', ForeignKey('keywards.id'), primary_key=True))


class Post(Base):
    __tablename__ = 'posts'
    id = Column(Integer, primary_key=True)
    keyward_id = Column(Integer, ForeignKey('keywards.id'))
    # 建立关系
    keywards = relationship('Keyward', secondary=post_keywords, back_populates='posts')


class Keyward(Base):
    __tablename__ = 'keywards'
    id = Column(Integer, primary_key=True)
    post_id = Column(Integer, ForeignKey('posts.id'))
    posts = relationship('Post', secondary=post_keywords, back_populates='keywards')
```

使用 back_populates 时需要两个表都有对应的字段。如果只有一个可用 `backref`，由 SQLAlchemy 自动创建（不建议）
# SQL

* 若一次查询以 `filter()` 结尾，通过 `__str__` 可获取其 SQL 语句
* 通过 `session.execute()` 可直接执行 SQL 语句，返回一个 cursor 对象
