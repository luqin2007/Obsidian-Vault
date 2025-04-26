用于 C++ 的 MySQL 数据库链接库，提供两种连接和操作数据库方式：

* X DevAPI，分为 C 和 C++ 两个版本
* 传统基于 JDBC4 的 API
# 添加到 VS2022

版本：C++ 20，MySQL Server 8.0.33，VS 2022

主要参考以下文章：

```cardlink
url: https://blog.csdn.net/u011775793/article/details/135555205
title: "[C++] 如何在Windows下使用vs 2022的vc++项目访问mysql 8？_vc2022 mfc cppconn mysql 8-CSDN博客"
description: "文章浏览阅读1.3k次，点赞20次，收藏24次。本文介绍了如何在Windows下使用vs 2022的vc++项目访问mysql 8服务器，我们可以使用官方提供的Mysql Connector/C++驱动程序，通过动态库的形式实现C++对mysql8 服务器的访问（比如创建数据库，创建表，插入数据和查询表中数据等）。_vc2022 mfc cppconn mysql 8"
host: blog.csdn.net
```

1. 下载和安装驱动程序

MySQL Server：[https://dev.mysql.com/downloads/installer/](https://dev.mysql.com/downloads/installer/ "https://dev.mysql.com/downloads/installer/")，直接安装

Connector/C++：[MySQL :: Download Connector/C++](https://dev.mysql.com/downloads/connector/cpp/)，下载后解压，设解压后的目录为 `{connector}`

根据文章所说，VS 的 Debug 配置需要 Debug 版本的 Connector

![[image-20240621003951-35n1kkf.png]]

2. 将需要的路径添加到 `path` 环境变量
    * `{connector}\bin`
    * `{connector}\lib64\vs14\debug`
3. 添加头文件和链接库：右键项目选择属性
    * `配置属性` - `常规`，C++ 语言标准选择 C++17 或以上
    * `C/C++` - `常规`，附加包含目录中添加 `{connector}\include` 目录
    * `链接器` - `常规`，附加库目录中添加 `{connector}\lib64\vs14` 目录
    * `链接器` - `输入`，附加依赖项中添加以下项：
	    * `libssl.lib`
	    * `libcrypto.lib`
        * `debug\mysqlcppconn8.lib`

测试是否连接成功的代码可以在下面的项目中的 `README` 中找到

```cardlink
url: https://github.com/mysql/mysql-connector-cpp
title: "GitHub - mysql/mysql-connector-cpp: MySQL Connector/C++ is a MySQL database connector for C++. It lets you develop C++ and C applications that connect to MySQL Server."
description: "MySQL Connector/C++ is a MySQL database connector for C++. It lets you develop C++ and C applications that connect to MySQL Server. - mysql/mysql-connector-cpp"
host: github.com
favicon: https://github.githubassets.com/favicons/favicon.svg
image: https://opengraph.githubassets.com/a28f3d6efe1eaffc2247baf86e319618e7ef1adb03de468381d589270f74fdb8/mysql/mysql-connector-cpp
```

# X DevAPI

```cardlink
url: https://dev.mysql.com/doc/dev/connector-cpp/latest/devapi_example.html
title: "MySQL Connector/C++: Connector/C++ X DevAPI Example"
host: dev.mysql.com
```
## 连接

```cpp
#include <mysqlx/xdevapi.h>

mysqlx::Session conn = mysqlx::Session(mysqlx::SessionSettings(host, port, username, password, database));
```

`SessionSettings` 表示一个数据库链接的地址，表示成字符串为：

`mysqlx://<username>:<password>@<host>:<port>`
## Schema

操作 MySQL 通过 `Schema` 对象完成，一个 `Schema` 代表一个数据库，使用 `Session::createSchema` 和 `Session::getSchema` 获取

* `session.getDefaultSchema()` 可以获取默认数据库，需要在 Session 连接时指定数据库名

```cpp
mysqlx::Schema schema = session.getDefaultSchema();
```
## 操作表

MySQL 将表操作方法封装成 `Table`

```cpp
auto table = schema.getTable("<table-name>", true);
```

* 添加：通过 `insert` 插入，返回值为 `mysqlx::Result` 类型
    * `getAffectedItemsCount()`：获取影响到的数据行数
    * `getAutoIncrementValue()`：获取自动生成的数据
    * `getGeneratedIds()`：获取主键自增的主键值

  ```cpp
  // insert into users("username", "password", "is_admin") 
  //        values (<username>, <password>, <is_admin>);
  auto schema = session.getDefaultSchema();
  auto table = schema.getTable("users");
  auto insert_result = table.insert("username", "password", "is_admin")
                            .values(username, password, is_admin)
                            .execute();
  ```

* 删除：使用 `remove` 方法

  ```cpp
  // delete from news_types where news_id = 1 and type_id = 2
  auto schema = session.getDefaultSchema();
  auto table = schema.getTable("news_types");
  table.remove()
       .where("news_id = 1 and type_id = 2")
       .execute();
  ```

* 查询：使用 `select` 函数查询

  ```cpp
  auto schema = session.getDefaultSchema();
  auto table = schema.getTable("users");
  // select * from users 
  //          where username = 'aaa' and password = 'bbb'
  auto result = table.select("*")
                     .where("username = 'aaa' and password = 'bbb'")
                     .execute();
  ```

  * 查找结果 `RowResult` 是内容为 `Row` 的迭代器，可以通过 `for` 或 `fetchOne()` 获取

    ```cpp
    for (auto d : result)
    {
    	int id = d[0].get<int>();
    	auto uname = d[1].get<string>();
    	bool is_admin = d[3].get<bool>();
    	auto  user = new User(id, uname, is_admin);
    	return {};
    }

    ```

    ```cpp
    for (int i = 0; 
         d; 
         ++i, d = result.fetchOne()) { ... }
    ```

  * `Row` 表示一条数据，是内容为 `Value` 的迭代器，可以像 `RowResult` 一样遍历，也可以通过 `["<field-name>"]` 运算符获取，并可以自动转换类型或通过 `get` 转换类型
  * `Value` 表示一条数据中的每一列
## 问题

> [!error] CDK Error: unexpected message

![[image-20240621130940-2vibg1j.png]]

1. MySQL 版本过低或缺少 mysqlx 支持，使用 `mysql SHOW plugins` 确认

    ```shell
    # 设用户名 root
    mysql -u root -p -e "SHOW plugins"
    ```

![[image-20240621131146-a9ve54c.png]]

2. 端口使用了 3306，默认 mysqlx 端口为 33060
# JDBC4 API

```cardlink
url: https://dev.mysql.com/doc/dev/connector-cpp/latest/jdbc_example.html
title: "MySQL Connector/C++: Connector/C++ classic JDBC API Example"
host: dev.mysql.com
```
## 问题 

> [!error] 无法解析的外部符号 class sql::mysql::MySQL_Driver *

![[image-20240621012538-6hnpqol.png]]

暂未解决，切换使用 X DevAPI
