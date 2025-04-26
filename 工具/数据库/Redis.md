Redis 是一个将数据缓存在内存中，再定期写入文件中的键值对数据库

> [!warning] 官方版本 Windows 下需要 WSL 支持，也可以使用他人编译的[非官方版本](https://github.com/zkteco-home/redis-windows)

```cardlink
url: https://redis.io/docs/latest/get-started/
title: "Community Edition"
description: "Get started with Redis Community Edition"
host: redis.io
favicon: https://redis.io/docs/latest/images/favicons/favicon-196x196.png
```
# 操作指令

使用 `redis-cli` 进入 Redis 控制台

> [!note] `redis-cli` 默认中文值使用 Unicode 码显示，通过 `redis-cli --raw` 可以使用中文显示

| 命令类型         | 描述               |
| ------------ | ---------------- |
| Keys         | 管理 Redis 键值对     |
| Strings      | 管理字符串 String 类型值 |
| Lists        | 管理双向列表 List 类型值  |
| Sets         | 管理集合 Set 类型值     |
| Hashes       | 管理散列 Hash 类型值    |
| Sorted Sets  | 管理有序集合 ZSet 类型值  |
| Pub/Sub      | 发布/订阅操作          |
| Streams      | 管理流 Stream 类型值   |
| Geo          | 地理空间相关           |
| Cluster      | 集群管理             |
| HyperLog     | 键值数据基数统计         |
| Connection   | 数据库链接            |
| Transactions | 事务管理             |
| Server       | 服务器管理，如查看当前状态信息等 |
| Scripting    | 执行 Lua 脚本        |
## Keys

`````col
````col-md
flexGrow=2
===

| 命令格式                             | 描述                              |
| -------------------------------- | ------------------------------- |
| `del <key>`                      | 删除 key                          |
| `exists <key>`                   | 检查给定key是否存在，存在为 1，不存在为 0        |
| `rename <key> <newkey>`          | 修改 key 的名称为 newkey              |
| `renamens <key> <newkey>`        | 仅当 newkey 不存在时，将 key 改名为 newkey |
| `type <key>`                     | 返回 key 储存的值类型                   |
| `dump <key>`                     | 序列化 key，返回被序列化的值                |
| `expire <key> <seconds>`         | 设置 key 过期时间（秒）                  |
| `expireat <key> <timestamp>`     | 设置 key 过期时间（时间戳 UNIX Timestamp） |
| `pexpire <key> <milliseconds>`   | 设置 key 过期时间（毫秒）                 |
| `pexpireat <key> <ms-timestamp>` | 设置 key 过期时间（时间戳，毫秒）             |
| `persist <key>`                  | 移除 key 过期时间                     |
| `stl <key>`                      | 查看 key 剩余生存时间（秒）                |
| `pttl <key>`                     | 查看 key 剩余生存时间（毫秒）               |
| `keys <pattern>`                 | 通过模式匹配查找 key                    |
| `move <key> <db>`                | 将当前数据库的 key 移动到给定的数据库 db 当中     |
| `random <key>`                   | 随机返回一个 key                      |

````
````col-md
flexGrow=1
===
ttl 返回 -1 表示无超时，-2 表示已超时或键不存在

![[../../../_resources/images/Pasted image 20241108155050.png]]
````
`````
## String

字符串或任何二进制数据类型，是基本数据类型，二进制安全

`````col
````col-md
flexGrow=2
===

| 命令                                         | 描述                      |
| ------------------------------------------ | ----------------------- |
| `set <key> <value>`                        | 添加或设置 key 的值            |
| `setnx <key> <value>`                      | 仅当 key 不存在时插入值          |
| `setrange <key> <offset> <value>`          | 修改从 offset 开始的子串为 value |
| `get <key>`                                | 获取 key 的值               |
| `getrange <key> <start> <end>`             | 返回 key 字符串的子串           |
| `getset <key> <value>`                     | 设置 key 的值并返回 key 的旧值    |
| `mget <key> [<key2>..]`                    | 获取多个 key 的值             |
| `mset <key> <value> [<key> <value> ...]`   | 设置多个键值对                 |
| `msetnx <key> <value> [<key> <value> ...]` | 仅当 key 不存在时插入多个值        |
| `getbit <key> <offset>`                    | 获取 key 存储值的二进制位         |
| `setbit <key> <offset> <value>`            | 设置或清除 key 值指定位          |
| `setex <key> <seconds> <value>`            | 设置值和超时时间（set+expire）    |
| `psetex <key> <milliseconds> <value>`      | setex 毫秒版（set+pexpire）  |
| `strlen <key>`                             | 返回 key 所存储的字符串值的长度      |
| `incr <key>`                               | key 存储的值 +1（整形）         |
| `incrby <key> <n>`                         | key 存储的值 +n（整形）         |
| `incrbyfloat <key> <n>`                    | key 存储的值 +n（浮点）         |
| `decr <key>`                               | key 存储的值 -1             |
| `decrby <key> <n>`                         | key 存储的值 -n             |
| `append <key> <value>`                     | 将值追加到末尾                 |

````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20241106190457.png]]

![[../../../_resources/images/Pasted image 20241108160938.png]]
````
`````
## List

简单双向链表，支持从头部和尾部插入、删除数据，下标从 0 开始（但控制台输出标号从 1 开始）

> [!attention] 一个 List 最多存储 $2^{32}-1$ 个数据

`````col
````col-md
flexGrow=2
===

| 命令                                             | 描述                              |
| ---------------------------------------------- | ------------------------------- |
| `llen <key>`                                   | 获取列表长度                          |
| `lpop/rpop <key>`                              | 弹出并返回列表的第一个/最后一个元素              |
| `blpop/brpop <key> [<key2> ...] <timeout>`     | 弹出并返回列表的第一个/最后一个元素<br>不存在时阻塞等待     |
| `lpush/rpush <key> <value> [<value2> ...]`     | 将值插入列表头部/尾部                     |
| `lpushx/rpushx <key> <value>`                  | 将值插入已存在的列表头部/尾部                 |
| `lrange <key> <start> <end>`                   | 获取列表指定范围内的元素                    |
| `lrem <key> <count> <value>`                   | 移除列表元素                          |
| `lset <key> <index> <value>`                   | 通过索引设置列表元素的值                    |
| `ltrim <key> <start> <end>`                    | 让列表只保留指定区间内的元素                  |
| `rpoplpush <source> <destination>`             | 将列表最后一个元素移动到另一个列表头部并返回          |
| `brpoplpush <source> <destination> <timcout>`  | 将列表最后一个元素移动到另一个列表头部并返回<br>不存在时阻塞等待 |
| `lindex <key> <index>`                         | 通过索引获取列表中的元素                    |
| `linsert <key> <BEFORE/AFTER> <pivot> <value>` | 在列表的元素前或者后插入元素                  |

````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20241108163311.png]]
````
`````
## Set

无序集合，基于哈希，操作时间复杂度 `O(1)`

> [!attention] 一个 Set 最多存储 $2^{32}-1$ 个数据


`````col
````col-md
flexGrow=2
===

| 命令                                   | 描述                               |
| ------------------------------------ | -------------------------------- |
| `sadd <key> <member> [<member> ...]` | 向集合添加成员                          |
| `srem <key> <member1> [<member2>]`   | 移除集合成员                           |
| `sismember <key> <member>`           | 判断 member 元素是否是集合的成员             |
| `scard <key>`                        | 获取集合长度                           |
| `spop <key>`                         | 随机移除并返回集合中的一个元素                  |
| `srandmember <key> [<count>]`        | 随机返回集合中一个或多个元素                   |
| `sdiff <key1> [<key2>]`              | 返回给定集合的差集                        |
| `sdiffstore <dest> <key1> [<key2>]`  | 返回给定集合的差集并存储在 dest 中             |
| `sinter <key1> [<key2>]`             | 返回给定集合的交集                        |
| `sinterstore <dest> <key>l [<key>2]` | 返回给定集合的交集并存储在 dest 中             |
| `sunion <key1> [<key>2]`             | 返回给定集合的并集                        |
| `smembers <key>`                     | 返回集合中的所有成员                       |
| `smove <source> <dest> <member>`     | 将 member 元素从 source 集合移动到 dest 中 |

````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20241108172400.png]]
![[../../../_resources/images/Pasted image 20241108172440.png]]
````
`````

## ZSet

类似 `Set`，但每个键都关联一个 `score` 值（浮点类型），内部数据按关联 `score` 大小排序

> [!attention] 一个 ZSet 最多存储 $2^{32}-1$ 个数据

`````col
````col-md
flexGrow=2
===

| 命令格式                                                     | 描述                       |     |
| -------------------------------------------------------- | ------------------------ | --- |
| `zadd <key> <score> <member> [<score2> <member2> ...]`   | 添加或更新成员                  |     |
| `zcard <key>`                                            | 获取集合成员数                  |     |
| `zcount <key> <min> <max>`                               | 获取指定分数区间的成员数             |     |
| `zincrby <key> <increment> <member>`                     | 指定成员的 score += increment |     |
| `zinterstore <dist> <numkeys> <key> [<key> ... ]`        | 返回集合交集并存储在 dist 中        |     |
| `zlexcount <key> <min> <max>`                            | 计算指定字典区间内的成员数量           |     |
| `zrange <key> <start> <stop> [WITHSCORES]`               | 返回指定范围的成员（类似列表）          |     |
| `zrangebylex <key> <min> <max> [LIMIT <offset> <count>]` | 返回字典区间的成员                |     |
| `zrangebyscore <key> <min> <max> [WITHSCORES] [LIMIT]`   | 返回指定分数区间内的成员             |     |
| `zscore <key> <member>`                                  | 返回成员的分数值                 |     |
| `zrank <key> <member>`                                   | 返回指定成员的索引                |     |
| `zrem <key> <member> [<member> ...]`                     | 移除的一个或多个成员               |     |
| `zremrangebyscore <key> <min> <max>`                     | 移除给定分数区间的所有成员            |     |

````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20241108201204.png]]
````
`````

## Hash

哈希表，一个以 String 为键，任意类型为值的表。

> [!attention] 一个 Hash 最多存储 $2^{32}-1$ 个数据

`````col
````col-md
flexGrow=2
===

| 命令                                                      | 描述                                   |
| ------------------------------------------------------- | ------------------------------------ |
| `hexists <key> <field>`                                 | 查看哈希表 key中指定字段是否存在<br>0 表示不存在，1 表示存在 |
| `hset <key> <field> <value>`                            | 将哈希表 key 中的字段 field 的值设为 value       |
| `hsetnx <key> <field> <value>`                          | 只有在字段 field 不存在时设置字段的值               |
| `hmset <key> <field> <value> [...]` | 设置多个键值对                              |
| `hlen <key>`                                            | 获取哈希表中字段的数量                          |
| `hkeys <key>`                                           | 获取所有哈希表中的字段                          |
| `hmget <key> <field1> [<field2> ...]`                   | 获取所有给定字段的值                           |
| `hdel <key> <field1> [<field2> ...]`                    | 删除哈希表字段                              |
| `hget <key> <field>`                                    | 获取哈希表中指定字段的值                         |
| `hgetall <key>`                                         | 获取哈希表中指定 key 的所有字段和值                 |
| `hvals <key>`                                           | 获取哈希表中的所有值                           |
| `hincrby <key> <field> <n>`                             | 为哈希表key 中的指定字段值 +n（整形）               |
| `hincrbyfloat <key> <field> <n>`                        | 为哈希表key 中的指定字段值 +n（浮点）               |

````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20241108174621.png]]
````
`````
## Stream

实现生产者-消费者模型，多用于消息处理

## 发布/订阅

- 订阅频道：`subscribe <channel>`
- 发布消息：`publish <channel> <message>`
# 持久化管理

>[!note] 持久化：将数据从内存同步到硬盘
>- 全量数据格式：将内存中的数据写入硬盘，方便下次读取文件时加载
>- 增量请求数据：将内存中的数据序列化为请求，读文件重新请求以得到数据

Redis 支持 RDB（Redis DataBase）和 AOF（AppendOnly File）两种持久化方式，二者可共存
- RDB：将数据压缩保存到硬盘中，本质是一种快照备份机制，默认文件为 `dump.db`
- AOF：以日志形式记录服务器每一个写操作，默认文件为 `appendonly.aof`
持久化方式及触发条件等配置如下：
- `save <sec> <update>`：每 `sec` 中，触发 `update` 次写操作，触发数据持久化
- `appendonly [yes/no]`：是否开启 AOF 持久化，默认为 `no`
- `appendfsync [no/always/everysec]`：AOF 持久化方式
	- `no`：不写入
	- `always`：每接收到一次更新操作，立即将日志写入硬盘
	- `everysec`：每秒写入硬盘一次
# 数据分区

将数据分片，用于横向扩展。根据分区算法，可以是：
- 范围分区：按范围分区，一般要求 Key 为 `object_name:<id>` 的形式
	- 缺点：需要维护一个记录键区间到数据库实例的映射表
- 哈希分区：根据键的哈希值分配

根据执行数据分区的角色不同，分区方式可以是：
- 客户端分区：客户端根据分区算法计算出数据库实例节点
- 代理分区：客户端将键值发给代理，由代理决定数据库实例节点
- 查询路由：客户端请求任何一个数据库实例，数据库实例转发到正确节点

Redis Cluster 是一种**查询路由**与**客户端分区**混合的查询路由，在客户端的辅助下重定向到正确节点
# 集群
## 主从集群

Redis 主从架构通过哨兵机制监控运行状态。
- 哨兵 Sentinel 是一个分布式系统，一个框架下可以有多个哨兵
- 通过流言（Gossip）协议监视 Master 是否下线
- 通过投票协议（Agreement Protocols）实现故障迁移

哨兵实际是一个特殊模式下的 Redis 服务器，通过 `--sentinel` 运行即可
- 定时任务：每个哨兵维护三个定时任务
	- 向主从节点发送 INFO 命令获取最新主从结构（10s/次，主节点主观下线后 1s/次）
	- 通过发布订阅获取其他哨兵信息
	- 通过向其他节点发送 PING 进行心跳检查，判断下线（1s/次）
- 主观下线：心跳检查失败时，若超过一定时间没有回复，哨兵主观认为节点下线
- 客观下线：某哨兵认为**主节点**主观下线后，向其他哨兵节点查询主节点状态，得到主节点下线的反馈达到一定数量时认为**主节点**客观下线
- 领导者选举：主节点客观下线后，各哨兵节点进行协商，以[[NoSQL/Raft 算法|Raft 算法]]选举出新领导者并进行故障转移
## 无中心集群

`Redis-Cluster` 集群采用无中心架构
- 所有节点彼此互联
- 超过半数节点失效后，节点失效
- 客户端可与任意一个节点连接，不需要代理
- 所有物理节点被映射到 `[0,16383]slot` 上，由 `Redis-Cluster` 维护
	- 变量值被存于 `CRC16(key) % 16384` 对应的 `slot` 上
	- 每个节点都可以是一个主从节点集群以保证可用性

| 命令                                     | 说明                            |
| -------------------------------------- | ----------------------------- |
| `cluster info`                         | 输出集群信息                        |
| `cluster nodes`                        | 输出所有节点信息                      |
| `cluster meet <ip> <port>`             | 添加节点 `<ip>:<port>`            |
| `cluster forget <id>`                  | 移除节点 `<id>`                   |
| `cluster addslots <slots...>`          | 将一个或多个 `slot` 指派给当前节点         |
| `cluster delslots <slots...>`          | 移除该节点的一个或多个 `slot` 指派         |
| `cluster flushslots`                   | 移除指派给该节点的所有 `slot`            |
| `cluster keyslot <key>`                | 计算键 `<key>` 应存储到哪个 `slot` 上   |
| `cluster countkeysinslot <slot>`       | 计算某 `<slot>` 存储了多少键           |
| `cluster getkeysinslot <slot> <count>` | 计算某 `<slot>` 存储的 `<count>` 个键 |
# 管理与监控

## 配置

`redis` 配置文件为 `redis.conf`，在 `redis-cli` 中可以通过 `CONFIG` 命令查询和修改
- `CONFIG GET <pname>`
- `CONFIG SET <pname> <value>`

`````col
````col-md
flexGrow=1
===

| 参数             | 说明                         |
| -------------- | -------------------------- |
| timeout        | 当客户端闲置多久后关闭，0 表示关闭         |
| port           | Redis 监听端口                 |
| loglevel       | 日志记录级别，默认 notice           |
| dbfilename     | 本地数据库文件名，默认 dump.rdb       |
| rdbcompression | 本地数据库是否压缩，默认 yes，压缩方式为 LZF |
| maxmemory      | 占用最大内存                     |
| requirepass    | 设置登录密码                     |

````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20241113104703.png]]
````
`````

设置密码后，使用 `AUTH "密码"` 登录
## 备份与恢复

- 备份：`save`，执行后会在安装目录下生成 `dump.rdb`
- 后台备份：`bgsave`
- 恢复：直接将 `dump.rdb` 放到安装目录下，重启服务即可
	- 同时启用 RDB 和 AOF 时，优先使用 AOF

安装目录可以通过 `config get dir` 查看，使用 `dbsize` 可以查看所有键数量，`keys *` 可以查看所有键
## 指令批量执行

将命令写入一个文件，使用 `type <file> | redis-cli` 执行即可
- Linux 下使用 `cat <file> | redis-cli`

使用 `type <file> | redis-cli --pipe` 可以不显示每条指令执行结果，只在最后显示最终结果，效率更高
