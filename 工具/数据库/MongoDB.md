# 存储结构
## 数据库

数据库名称使用小写字符命名，名称长度不超过 64 字符，不得包含 ` `，`.`，`$`，`/`，`\` 和 `\0`

MongoDB 有几个默认数据库：

| 数据库名称    | 作用                               |
| -------- | -------------------------------- |
| `admin`  | 在此数据库中操作默认具有管理员权限，特定操作也只能在此数据库运行 |
| `local`  | 只存储在各自服务器实例上，存储本地单台服务器数据，不会被复制   |
| `config` | 分布式分片配置                          |
| `test`   | 默认连接的数据库                         |
## 集合

Collection，一组文档的集合，相当于关系型数据库的表 Table，但没有固定模式，不受关系范式约束。

>[!info] 集合名不能以 `system.` 为前缀，也不能是保留字符 `$`
## 文档

Document，相当于关系型数据库的一行数据。文档以嵌套键值树形式 BSON 数据的格式存储，文档除数据值外还存储了元数据。

> [!note] BSON：Binary Serialized Document Format，MongoDB 的数据存储和网络传输格式。
> 二进制存储的 JSON，在 JSON 的基础上将元素长度保存于头部信息，提高遍历速度
> - 键：字符串，不能是 `$`，不能以 `.` 开头，不能是 `null`
> - 值：JSON 基本类型，文档对象，数组，**其他类型对象**
> ```json
> {
>     "_id": ObjectId("xxxxxxx"),
>     "name": zhangsan,
>     "address": {
>         "city": "Beijing",
>         "code": 100876
>     },
>     "scores": [
>         { "name": "English", "grade": 3.0 },
>         { "name": "Chinese", "grade": 4.0 }
>     ]
> }
> ```

MongoDB 会为每个文档自动添加一个键 `_id` 作为主键，类型为 `ObjectId`，值唯一且不可变。

> [!note] ObjectId：12 字节文档唯一标识，通常以 16 字节表示
> 前 9 字节保证同一秒不同机器、不同进程产生的记录标识唯一，后 3 字节保证同一进程产生的标识唯一
> - 4 字节：时间戳，记录 `_id` 产生时间
> - 3 字节：机器识别码，主机的唯一标识码，通常为主机名的 Hash 值
> - 2 字节：进程标识符 PID
> - 3 字节：计数器，同一进程不断自增

> [!attention] `_id` 可以设置为其他类型，但需要保证其唯一性

文档之间的关系包括嵌套和引用链接两种
- 嵌套：`Embed`，文档内包含其他文档。嵌套数量和深度没有限制，但文档最大限制为 16MB
- 引用链接：`Link`，一个 `DBRef` 对象，更接近外键的概念，通过解引用可以链接到另一个文档
# 数据库工具与操作

MongoDB 提供 Server，Shell，Compass，Atlas CLI 等工具
- Server：MongoDB 数据库服务端
- Shell：开源命令行操作界面
- Compass：GUI 工具
- Atlas CLI：统一命令行界面，管理 MongoDB Atlas

| 操作命令                                    | 功能说明                                                                                |
| --------------------------------------- | ----------------------------------------------------------------------------------- |
| show dbs                                | 查询显示所有数据库                                                                           |
| show users                              | 显示当前DB所有用户                                                                          |
| show collections                        | 显示当前DB所有集合                                                                          |
| db.getName()，db                         | 可以显示当前数据库对象或集合                                                                      |
| use <dbname>                            | 切换/创建数据库                                                                            |
| db.help()                               | 显示可用数据库操作命令帮助信息                                                                     |
| db.dropDatabase()                       | 删除当前使用的数据库                                                                          |
| db.runCommand(cmdObj)                   | 执行数据库中的命令，cmdObj 为命令字符串                                                             |
| db.cloneDatabase(fromhost)              | 将指定机器上数据库的数据克隆到当前数据库                                                                |
| db.copyDatabase(fromdb, todb, fromhost) | 数据库复制，如 `db.copyDatabase("mydb", "temp", "127.0.0.1");` 表示将本机 mydb 的数据复制到 temp 数据库中 |
| db.repairDatabase()                     | 修复当前数据库                                                                             |
| db.stats()                              | 显示当前 db 状态，包含集合数、空间占用情况等                                                            |
| db.version()                            | 显示当前 db 版本                                                                          |
| db.getMongo()                           | 查看当前 db 的链接机器地址                                                                     |
| db.getCollectionNames()                 | 查看当前 db 所有集合名称                                                                      |
| db.getCollectionInfos([filter])         | 查看满足过滤条件集合的详细信息                                                                     |
| db.createUser(userDocument)             | 添加用户                                                                                |
| db.auth(username, password)             | 数据库认证                                                                               |
| db.dropUser(username)                   | 删除用户                                                                                |
> [!attention] 使用 `use <数据库名>` 创建数据库时，必须对数据库进行操作才能真正创建。仅 `use` 不会创建
> 
# 集合操作

| 操作命令                                | 功能说明                            |
| ----------------------------------- | ------------------------------- |
| db.createCollection (name, options) | 创建一个集合，集合名称需要 `""`，`options` 可选 |
| db.getCollection(name)              | 获取指定名称的 Collection，结果会加上数据库的名称  |
| db.getCollectionNames()             | 获取当前 db 所有 Collection 名称数组      |
| db.printCollectionStats()           | 显示当前 db 所有 Collection 索引等状态信息   |
| db.<集合名>.help()                     | 显示集合操作的相关命令提示                   |
| db.<集合名>.drop()                     | 删除集合                            |
| db.<集合名>.dataSize()                 | 查看集合数据占用空间大小                    |
>[!note] 创建文档时，若集合不存在，MongoDB 会自动创建集合
## 定长集合

定长集合多用于重点存储近期业务的场景，只关心最近数据

```js
db.createCollection("集合名", { capped: true, size: 6142800, max: 10000 })
```

- `capped`：是否为定长集合
- `size`：单个文档占用的字节数
- `max`：包含文档的最大数量
# 文档操作
## 文档参考

```cardlink
url: https://mongodb.net.cn/manual/
title: "参考_MonogDB 中文网"
host: mongodb.net.cn
```

## 数据类型

除 JSON 的基本数据类型外，MongoDB 还额外提供以下类型：

| 类型名称       | 说明                                               |
| ---------- | ------------------------------------------------ |
| ObjectId   | 12 字节的字符串，用于唯一标识文档，如 ` { "x": objectld() }`      |
| Date       | 日期毫秒数，不含时区，如 `{ "x": new Date() }`               |
| Timestamp  | 时间戳                                              |
| Regex      | JavaScript 正则表达式，如 `{ "x": /[abc]/ }`，可用于查询条件    |
| BinaryData | 二进制数据，任意字节的字符串，可用于存储非 UTF-8 字符串                  |
| Min key    | BSON 中的最小值                                       |
| Max key    | BSON 中的最大值                                       |
| JavaScript | 任何 JavaScript 代码，如 `{ "x": function() {/*…*/} }` |
| Array      | 数组，如 `{ "x": ["a", "b", "c"] )`                  |
| Object     | 嵌套文档，被嵌套的文档作为值来处理，如 `{ "x": { "y": 3 } }`        |
每个数据类型都包含一个数字和别名，可通过 `$type` 查询对应文档的 BSON 类型
## 操作符
### 常用操作符

| 类型   | 操作符                            | 说明                    | 操作示例                                                                           |
| ---- | ------------------------------ | --------------------- | ------------------------------------------------------------------------------ |
| 元素比较 | $lt, $lte, $gt, $gte, $eq, $ne | 大小，相等性判断              | db.c.find({ "a": { $gt: 100 } })                                               |
|      | $in, $nin                      | 属于，不属于                | db.c.find({ j: { $in: [2.4.6] } })                                             |
| 元素查询 | $exists                        | true / false          | db.c.find({ a: { $exists : true } })                                           |
|      | $type                          | 按数据类型查询               | db.c.find({ a: { $type : 2 } })                                                |
| 模式评估 | $mod                           | 取模                    | db.c.find({ a: { $mod: [10, 1] } }) 表示 a%10==1                                 |
|      | $regex                         | 正则匹配                  | db.c.find({ sku: { $regex: /^ABC/i } })                                        |
|      | $where                         | 弥补其他方式无法满足的查询条件，但效率较低 | db.c.find({ "$where": "this.a>3" })                                            |
|      | $text                          | 文本搜索，要先建索引            | db.c.find({ $text: { $search: "coffee" } })                                    |
| 逻辑运算 | $and, $or, $nor                | 与，或，异或                | db.c.find({ name: "bob", $nor: [ {a:1}, {b:2} ] })                             |
|      | $not                           | 非运算                   | db.c.find({ name: "bob", $not: [ {a:1}, {b:2} ] })                             |
| 数据查询 | $size                          | 匹配数组长度                | db.c.find({ a: { $size: 1 } })                                                 |
|      | $all                           | 数组中的元素是否完全匹配          | db.c.find({ a: { $all: [2, 3] } })                                             |
|      | $elemMatch                     | 数组中的元素是否都满足条件         | db.c.find({ product: { "$elemMatch": { shape: "square", color: "purple" } } }) |
| 位查询  | $bitsAllClear                  | 所有位的值为0               | db.c.find({ a: { $bitsAllClear: [1,5] } })                                     |
|      | $bitsAIlSet                    | 所有位的值为1               | db.c.find({ a: { $bitsAllSet: [1,5] } })                                       |
|      | $bitsAnyClear                  | 部分位的值为0               | db.c.find({ a: { $bitsAnyClear: [1,5] } })                                     |
|      | $bitsAnySet                    | 部分位的值为1               | db.c.find({ a: { $bitsAnySet: [1,5] } })                                       |
### 其他操作符

- 地理空间操作符：`$minDistance`，`$maxDistance`，`$box`，`$center`，`$geoWithin`，`$near` 等
- 类型转换符
	- `double`，`string`，`objectId`，`date`，`integer`，`long`，`decimal` 类型转换
	- `$convert` 聚合类型转换符
## 插入

> [!note] 插入数据时，若 collection 不存在，Mongodb 会自动创建对应 Collection
`````col
````col-md
flexGrow=1
===
```json
db.<Collection 名>.insertOne(<Json 数据对象>)
db.<Collection 名>.insertMany([<Json 数据对象>...])
```
````
````col-md
flexGrow=1
===
![[../../_resources/images/Pasted image 20241101120939.png]]
````
`````
## 查询

- 获取所有文档

```js
db.<collection-name>.find()
db.<collection-name>.find({})
```

- 查询所有文档，并以树形结构显示

```js
db.<collection-name>.find().pretty()
db.<collection-name>.find({}).pretty()
```

- 按条件查询：`find`，`findOne`
`````col
````col-md
flexGrow=1
===
```js
db.<collection-name>.find({ condition })
```
````
````col-md
flexGrow=1
===
![[../../_resources/images/Pasted image 20241101124559.png]]
````
`````
- 筛选
	- `limit(n)`，`skip(n)`
	- `sort({key:1/-1})`：按某键排序，1 为升序，-1 为降序，可以有多个关键字
## 删除

- 删除所有文档：`db.<collection-name>.drop()`
- 删除特定文档：`deleteOne`，`deleteMany`，`findOneAndDelete`

`````col
````col-md
flexGrow=1
===
```js
db.ct.deleteOne({ "Name": "Tian" })
```
````
````col-md
flexGrow=1
===
![[../../_resources/images/Pasted image 20241101123036.png]]
````
`````

`````col
````col-md
flexGrow=1
===
```js
db.ct.deleteOne({ "Age": { $gt: 25 } })
```
````
````col-md
flexGrow=1
===
![[../../_resources/images/Pasted image 20241101123228.png]]
````
`````
## 修改

使用 `updateOne` 和 `updateMany` 更新数据，也可以使用 `replace(<query>, <doc>)` 系列函数替换文档
`````col
````col-md
flexGrow=1
===
```js
db.<collection-name>.updateOne(<query>, <update>, {
	upsert: <bool>,
	writeConcern: <document>,
	collection: <document>,
	arrayFilters: [条件],
	hint: doc|str
})
```
````
````col-md
flexGrow=1
===
```js
db.<collection-name>.updateMany(<query>, <update>, {
	upsert: <bool>,
	writeConcern: <document>,
	collection: <document>,
	arrayFilters: [条件],
	hint: doc|str
})
```
````
`````
- `<query>`：查询条件，一个对象，同 `find` 的格式
- `<update>`：更新操作对象：`{ <op>: {k: v, ...}, ... }`
	- `$set{k,v}`：设置文档某些键的值，不存在则新建
	- `$currentDate{k, true}`：将当前时间设置为某些键的值
	- `$inc`：值+1
	- `$unset`：删除某键
- `upsert`：是否自动创建
- `writeConcern`：日志与操作超时设置，`{w: bool, j: bool, wtimeout: num}`
- `collection`：自定义查询时的语言规范、字符串比较是否区分大小写等
- `arrayFilters`：针对数组的更精细化的过滤器

> [!example] 精准查找 id 为 `67246d39eaab76f58375e695` 的文档，修改 `Name="Yan", age=38`
> ```js
> db.ct.updateOne(
>   { _id: ObjectId('67246d39eaab76f58375e695') }, 
>   { $set: { Name: "Yan", age: 38 } })
> ```

> [!example] 更新 `Name` 为 `Chen` 的第一个文档，修改 `Name="Chen Gang, age=32"`
> ```js
> db.ct.updateOne(
>   { Name: "Chen" },
>   { $set: { Name: "Chen Gang", age: 32 } })
> ``` 

> [!example] 替换 `Name` 为 `"Li"` 的对象为 `{json}{ Name: "Li Li", age: 23 }`
```js
db.ct.replaceOne(
  { Name: "Li" },
  { Name: "Li Li", age: 23 })
```
## 游标

`findMany` 返回一个游标实例，可通过循环迭代操作文档

```js
let cursor = db.<collection-name>.find(...)
while (cursor.hasNext()) {
    let obj = cursor.next()
    // do something
}
```

> [!note] Json 相关函数：`tojson`，`printjson` 等
## 链接引用

DBRef 对象形式为 `{ $ref: <集合名>, $id: <文档 _id>, $db: <可选，数据库> }`

```js
let ref = DBRef("<collection-name>", ObjectId(...)[, "<db-name>"])
```

> [!attention] 仅在必要时使用 DBRef，正常情况下可以直接使用 `ObjectId` 作为引用
`````col
````col-md
flexGrow=1
===
```js
db.dep.insertOne({
  name: "CS",
  num: 15,
  people: [
    DBRef('people', ObjectId('6724747deaab76f58375e69c'), 'db'),
    DBRef('people', ObjectId('6724747deaab76f58375e69d'), 'db'),
    DBRef('people', ObjectId('6724747deaab76f58375e69e'), 'db'),
  ]
})
```
````
````col-md
flexGrow=1
===
![[../../_resources/images/Pasted image 20241101143012.png]]
````
`````
## 管道聚合

对数据汇总统计

```js
db.<collection-name>.agredate(operation)
```

| 聚合操作     | 功能说明                                                                            |
| -------- | ------------------------------------------------------------------------------- |
| $match   | 过滤数据                                                                            |
| $group   | 文档分组，用于统计结果，一般需要指定分组依据的字段和聚合运算，如 `$sum`、`$avg`、`$min`、`$max`、`$first`、`$last` 等 |
| $project | 修改文档结构，可用来重命名、增加或删除输入字段                                                         |
| $limit   | 限制返回的文档数                                                                        |
| $skip    | 跳过指定数量的文档                                                                       |
| $unwind  | 将文档某一数组类型字段拆分成多条，每条包含数组中的一个值                                                    |
| $sort    | 排序                                                                              |
| $geoNear | 按与某地理位置的距离排序                                                                    |
### 聚合管道

Aggregation Pipeline，通过一个序列分阶段、分步骤的将一系列文档处理成所需结果

```js
db.<collection-name>.agredate([operations...])
```

> [!example] 根据 `status='A'` 筛选数据，根据 `cust_id` 分组，统计每组文档的 `amount` 属性和
> ```js
> db.orders.aggregate([
>   { $match: { status: "A" } },
>   { $group: { _id: "$ cust_id", total: { $sum: "$ amount" } } }
> ])
> ```
> ![[../../../_resources/images/Pasted image 20241101151737.png]]
### 单一目的聚合方法

对于简单的聚合统计方法，MongoDB 提供单独的操作函数，包括 `sort`，`count`，`distinct` 等

```js
db.inventory.distinct("item")
db.inventory.find({ status: 'A' }).count()
```
### MR 函数

类似 Hadoop 的 `Map` 和 `Reduct` 操作

> [!example] 根据 `status='A'` 筛选数据，根据 `cust_id` 分组，统计每组文档的 `amount` 属性和，将结果输出到 `order_totals` Collection 中
> ```js
> db.orders.mapReduce(
>   function() { emit(this.cust_id, this.amount) },        // map
>   function(key, values) { return Array.sum(values) },    // reduce
>   {
>     query: { status: 'A' },                              // query
>     out: "order_totals"                                  // output
>   })
> ```
> ![[../../../_resources/images/Pasted image 20241101152921.png]]
# 索引

## 基本索引

利用 B-Tree 结构创建索引，提高查询速度，但会略微降低写入速度用于更新索引
### 创建索引

```js
db.<collection-name>.createIndex(<keys>, <options>)
```

- `<keys>`：`{ "键名": 1/-1 }` 对象，1 为升序创建索引，-1 为降序，支持多个键创建复合索引
- `<options>`：参数
	- `background: true`：后台创建，不会阻塞其他数据库操作
### 查询索引信息

- `db.<collection-name>.getIndexes()`：查看所有创建的索引

![[../../../_resources/images/Pasted image 20241101153811.png]]

- `db.<collection-name>.totalIndexSize()`：获取索引表总大小

![[../../../_resources/images/Pasted image 20241101153832.png]]

- `db.<collection-name>.reIndex()`：查看集合索引信息

![[../../../_resources/images/Pasted image 20241101153956.png]]

- `<expression>.explain()` 可查看任意操作使用的索引
### 删除索引

- `db.<collection-name>.dropIndex("<index-name>")`：删除索引
	- `<index-name>`：`getIndexes()` 中的 `name` 字段
- `db.<collection-name>.dropIndexes()`：删除除 `_id` 外的所有索引
## 全文索引

针对文本类字段创建 `text` 类型全文索引，一个集合只能创建一个

```js
db.<collection-name>.createIndex( { <field-name>: "text" } )
```

全文索引也支持多个字段创建复合索引

```js
db.<collection-name>.createIndex( { <field-name1>: "text", <field-name2>: "text" } )
```

不确定哪些字段是文本类型时，支持使用 `$**` 通配符为文档所有字符类字段创建索引

```js
db.<collection-name>.createIndex( { “$**”: "text" } )
```
### 文本查询

在 `find` 方法中使用 `$text` 利用全文索引查询符合条件的文档数据

> [!example] 查询所有包含 `bad` 的文档
> ```js
> db.ct.find({ $text: { $search: "bad" } })
> ```

使用 ` ` 表示或条件

> [!example] 查询所有包含 `bad` 或 `spoiled` 的文档
> ```js
> db.ct.find({ $text: { $search: "bad spoiled" } })
> ```

使用 `-` 表示排除

> [!example] 查询所有包含 `bad` 但不包含 `ok` 的文档
> ```js
> db.ct.find({ $text: { $search: "bad -ok" } })
> ```

使用 `""` 引用带有空格的字符串，注意转义

> [!example] 查询所有包含 `not ok` 的文档
> ```js
> db.ct.find({ $text: { $search: "\"not ok\"" } })
> ```
## 地理位置索引

在包含地理空间形状和点集的集合上执行空间查询
- 2d 索引：平面地理位置索引，创建和查找平面上的点
- 2dsphere 索引：球面地理位置索引，存储和查找球面上的点

> [!note] GeoJSON：一种 Json 的描述图形的方法
> 格式为 `{ type: <图形类型>, coordinates: <点或点集合> }`，图形可以是单个图形或多个图形
> - 单个图形：包括点、线、多边形
> - 多个图形：`type` 为对应图形前加 `Multi` 前缀，`coordinates` 为每个子图形的 `coordinates` 组成的数组
> - 几何集合：`{ type: "GeometryCollection", coordinates: [<GeoJSON 对象>...] }`
> ````tabs
> tab: 点
> ```js
> { type: "Point", coordinates: [x: number, y: number] }
> ```
> tab: 线
> ```js
> { type: "Line", coordinates: [[x0, y0], [x1, y1]] }
> ```
> tab: 单环多边形
> 存在至少 4 个点，最后一个点与第一个点相同以形成闭环
> 
> ```js
> { type: "Polygon", coordinates: [[ [x0, y0], [x1, y1], ..., [x0, y0] ]] }
> ```
> tab: 多环多边形
> - 第一个环必须是外环，外环不能自交叉
> - 剩下的环必须在包含在外环内，内环之间不能交叉或覆盖
> - 内环不能共变
> 
> ```js
> { type: "Polygon", coordinates: [
>   [ [x00, y00], [x01, y01], ..., [x00, y00] ],
>   [ [x10, y10], [x11, y11], ..., [x10, y10] ],
>   ...
> ] }
> ```
> ````

```js
db.<collection-name>.createIndex({ <field-name>: “2d” })
// <field-name> 对应对象必须符合 GeoJSON 格式
db.<collection-name>.createIndex({ <field-name>: “2dsphere” })
```

相关操作符如下，若要求为 `GeoJSON` 类型，属性对应对象必须符合 `GeoJSON` 格式

| 查询类型                             | 几何类型 | 备注              |
| -------------------------------- | ---- | --------------- |
| $near: { GeoJSON点: ... }         | 球面   | 某邻域范围内文档        |
| $near: { 传统坐标: … }               | 平面   | 某邻域范围内文档        |
| $nearSphere: { GeoJSON 点: ... }  | 球面   | 某邻域范围内文档        |
| $nearSphere: { 传统坐标: … }         | 球面   | 可使用 GeoJSON 点替换 |
| $geoWithin: { $geometry: … }     | 球面   | 某区域范围内文档        |
| $geoWithin: { $box: … }          | 平面   | 矩形区域内文档         |
| $geoWithin: { $polygon: … }      | 平面   | 多边形区域内文档        |
| $geoWithin: { $center: … }       | 平面   | 某圆形区域内文档        |
| $geoWithin: { $centerSphere: … } | 球面   | 某点球面距离区域内文档     |
| $geolntersects: { $geometry: … } | 球面   | 邻近相交区域范围内文档     |
>[!example] 查询经纬度为 `-73.93414657, 40.82302903` 附近最近的居民
> ```js
> db.neighboorhoods.findOne({
>     geometry: {
>         $geoIntersects: {
>             $geometry: {
>                 type: "Point",
>                 coordinates: [ -73.93414657, 40.82302903 ]
>             }
>         }
>     }
> })
> ```

> [!example] 已知节点 person 的位置为 `person.geometry`，查找该节点 5 英里内的饭店，按由近到远排序
> ```js
> db.restaurants.findMany({
>     location: {
>         $nearSphere: {
>             $geometry: person.geometry
>         },
>         $maxDistance: 5 * METERS_PER_MILE
> })
> ```
# 数据库架构
## 分片与集群架构

>[!note] 分片：在多台机器上分割数据，使数据库系统能存储和处理更多数据

> [!note] 片键：Shared Key，文档中的一个键

MongoDB 分片以集合为单位，在片键处拆分数据，形成多个数据块，数据块均衡分布到所有分片中
- 每个文档必须包含片键
- 每个文档必须包含片键的索引或复合索引

横向扩展时，数据被分为多个 chunk。有关 chunk 的操作包括：
- Splitting：当某个 chunk 超过配置的大小时，MongoDB 后台将该 chunk 切分为更小的 chunk
- Balancing：指 chunk 的迁徙，保证每个 Shared Server 的均衡，由后台进程 balancer 完成

MongoDB 分片机制包含 Shared，Config Server 和 Router 三个组件：
- Shared：存储实际的数据块，实际中多由多台机器组成 replica set
- Config Server：配置服务器，存储 Cluster 元数据，包括 Chunk 分块信息等
- Router：Mongos，前端路由，负责客户端接入
## 数据冗余复制集

MongoDB 支持主从式分布部署，Primary 宕机后 Secondary 可以自动切换为 Primary。

MongoDB 复制集群至少有两个节点，各个节点角色有：
- 1 主节点 Primary，负责处理客户端请求，并将所有操作记录为 `oplog` 文件
	- 非阻塞的从节点读：借助引擎层可从任意时间戳读取的功能，实现节点读功能不受写入 `olog` 记录阻塞的影响
- 多个从节点 Secondary，定期轮询主节点，同步数据
- 0 或 1 个仲裁者 Arbiter，发送心跳包确认集群中集合数量，在主节点故障时作为仲裁者决定从节点转为主节点
	- 只读，只负责选举
- Hidden 节点：不会被选举为 Primary 节点，且对客户端应用不可见，不会接受客户端应用请求
	- 常用于数据备份、离线计算等服务
	- Delayed 节点：落后于 Primary 节点一段时间的 Hidden 节点，可用于写入无效数据时的恢复
### 主节点

```shell
mongod --port <端口> --dbpath "<数据库存储位置>" --replSet 集群名
```

启动主节点后，通过 `rs` 管理复制集群
- `rs.initiate()`：启动新副本集
- `rs.conf()`：查看副本集配置
- `rs.status()`：查看副本集状态
- `rs.add("<host>:<port>")`：添加副本集成员
- `db.isMaster()`：查看当前节点是否为主节点
### 从节点选举

`rs.initiate()` 进行初始化后发起 Primary 选举操作，获得大多数节点通过后可成为主节点。

当不存在仲裁节点 Arbiter 节点时，当集群节点数量不足一半节点以上时，主节点宕机后无法从从节点中选举出新主节点（票数不足）

节点可以设置优先级 Priority，优先级越高越容易被选举为 Primary 节点。

Priority = 0 的节点不会被选举为 Primary 节点，这类节点还可以被设置为 Hidden 节点。
## 分布式文件存储

> [!note] 管理工具[下载](https://www.mongodb.com/try/download/database-tools)

MongoDB 提供基于文件的分布式文件存储系统 GridFS，用于存储和检索超过限制 16 MB 的文件。
- `fs.files`：存储文件元数据，如名称、类型、自定义属性等
- `fs.chunks`：存储文件的实际内容，以二进制形式存储于 Chunks 中

存入文件需要使用 mongofiles 工具

> [!note] GridFS 管理工具 `mongofile` 不包含在 mongo shell 中，需要独立下载

```shell
# 存入
mongofiles -d gridfs put <文件名>
```

可以直接使用 MongoDB 查询文件

```js
db.<collection-name>.files.find()
```

根据查询出的 ObjectId，获取对应 chunks

```js
db.<collection-name>.chunks.find({ files_id: ObjectId("xxxxx") })
```
## Journaling 日志

默认开启，用于保证数据库意外断电故障的恢复

Journaling 日志包含两个内存视图，通过内存映射实现：
- `private view`：对该内存的修改不会影响磁盘文件
- `shared view`：系统周期性刷新到文件上
# 管理与监控
## 导入导出

>[!attention] 导入导出工具不适用于数据备份

````tabs
tab: 导出

```shell
mongoexport \
-d <数据库名> \
-c <Collection名> \
-o <导出文件名> \
--type <类型:json|csv> \
# 可选，指定导出字段，逗号分割
-f <字段名>
```

tab: 导入

```shell
mongoimport \
-d <数据库名> \
-c <Collection名> \
--file <导入文件名> \
--type <类型:json|csv> \
# 可选，若格式为 csv，第一行是否为标题
--headerline\
# 可选，指定导出字段，逗号分割
-f <字段名>
```
````
## 备份恢复

````tabs
tab: 备份

```shell
mongodump \
  -h <数据库服务器地址> \
  -d <数据库名> \
  -o <输出目录>
```

tab: 恢复

```shell
mongostore \
  -d <数据库名> \
  -o <备份目录>
```
````
## 多文档事务

> [!note] MongoDB 单文档操作本身具有原子性

MongoDB 支持复制集内一个或多个文档的事务，需要在同一个 Session 中

```json
s = db.getMongo().startSession()
s.startTransaction()
// do something
// 回滚：s.abortTransaction()
s.commitTransaction()
```
## 数据库监控

`mongostat`：查看 MongoDB 运行状态

![[../../../_resources/images/Pasted image 20241106131345.png]]

`mongotop`：查看 MongoDB 操作用时

![[../../../_resources/images/Pasted image 20241106131427.png]]

监控服务：MMS，MongoDB Monitoring Service