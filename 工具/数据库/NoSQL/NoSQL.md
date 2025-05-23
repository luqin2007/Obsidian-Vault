> [!note] NoSQL：非关系型数据库的总称，区别于传统 SQL
> - 存储方式：不以严格的二维表形式存储，通常以块或其他数据特点存储
> - 存储结构：每行（每条）数据结构不一定相同
> - 存储规范：为大数据高性能访问，大多使用空间换时间的形式，并引入冗余设计
> - 查询方式：通常不使用 SQL 语言查询
> - 存储扩展：SQL 常采用纵向扩展（更高性能计算机），NoSQL 常采用横向扩展（更多服务器）
> - 事务处理：SQL 事务遵循 ACID 原则，NoSQL 更强调 BASE 原则，没有强一致性

> [!hint] NewSQL：试图整合 SQL 强一致性与 NoSQL 高性能而产生新型数据库

NoSQL 通常都具有具有高可用、高性能、高并发、集群化跨平台部署等特性

| 存储类型                    | 特点                                                                                          |
| ----------------------- | ------------------------------------------------------------------------------------------- |
| [[图数据库\|图存储]]           | Graph Stores，按图存储的数据库，主要为节点与路径                                                              |
| [[文档数据库\|文档存储]]         | Document Stores，多以类似 JSON 格式存储文档，可嵌套，可以字段建立索引                                               |
| [[键值数据库\|Key-Value 存储]] | 类似 Map，可通过 Key 快速查询到 Value                                                                  |
| [[列族数据库\|列族存储]]         | Wide Column Stores，按列存储，存储结构化和半结构化数据，可快速查询某列或几列数据                                           |
| 时序存储                    | Time Series Stores，存储按时间序列组织的数据                                                             |
| 对象存储                    | Object oriented Stores，通过类似面向对象方法操作数据，通过对象存储数据                                              |
| XML 数据库                 | 高效存储 XML 数据，支持如 XPath，XQuery 等方式查询                                                          |
| RDF 数据库                 | 通过资源描述框架存储，用于语义网、知识图谱，数据主要由主语、谓语、宾语三元组组成                                                    |
| 搜索引擎                    | Search Engines，用于数据搜索，支持复杂搜索表达式、全文搜索、源搜索、空间搜索、分布式搜索、结果排序分组等                                 |
| 事件存储                    | Event Stores，记录事件发生的数据库，创建单个对象的时间序列，每个对象的每个状态都有一个时间戳，对象状态可回放                                |
| 多值数据库                   | Multivalue DBMS，类似关系数据库，但违背了第一范式可以记录多个值，又称 NF2 系统（非第一范式系统）                                  |
| 内容存储                    | Content Stores，专门管理数字内容的多媒体数据库，包括资源本体及其元数据，通常使用 SQL 或 XPath 操作查询，支持全文检索、版本控制、多媒体数据内容存储与访问控制 |
| 导航数据库                   | Navigational DBMS，只允许通过链接记录访问数据集                                                            |

各种数据库排名相见

```cardlink
url: https://db-engines.com/en/ranking/
title: "DB-Engines Ranking"
description: "Popularity ranking of database management systems."
host: db-engines.com
image: https://db-engines.com/pictures/db-engines_128x128.png
```

# CAP 定理与 BASE 原则

按 CAP 定理划分：
- 传统 SQL 数据库系统 RDBMS 满足 CA 原则
- MongoDB，HBase，Redis 等满足 AP 原则，对一致性要求低一些
- Cassandra，CouchDB，DynamoDB，Riak 等满足 AP 原则

> [!note] CAP 定理：又称布鲁尔定理。对于分布式计算系统来说，不可能同时满足一致性、可用性、分区容忍性
> - 一致性 Consistency：所有节点在同一时间具有相同的数据
> - 可用性 Availability：部分节点故障时集群整体仍可用，即保证每个请求不管成功或失败都有响应
> - 分区容忍性 Partition tolerance：稳健性，允许数据分区，系统中任意信息丢失或失败不影响系统继续运作
> 
> 根据侧重点不同，系统保证两条原则并对第三条原则要求较为宽松，划分为 CA、AP、CP 三种原则。

因此，NoSQL 通常不满足传统数据库事务 ACID 原则，而是满足非强一致性管理原则

> [!note] 非强一致性管理原则：BASE 原则
> - 基本可用 Basically Available：分布式系统出现不可预知故障时，允许损失部分可用性，如业务高峰期可以关掉非必要功能保障核心关键功能的低延迟、高性能
> - 软状态 Soft-state：允许数据出现中间状态且不影响系统整体可用性，即允许系统节点间同步存在延迟
> - 最终一致性 Eventually Consistent：短时间内，即使存在不同步风险，也要允许新数据被存储；所有数据在一段时间后总能达到一致状态

BASE 原则更倾向于简单迅速的大数据存储管理，不必要复杂的资源锁，重点是保证服务响应后处理不一致部分，保证最终一致性。

# 一致性技术

分布式系统所有节点不能保证都正常工作，系统使用多数据副本保证可用性，这对用户透明。为了处理数据副本不一致问题，按照服务保障能力不同，客户端访问一致性分为：
- 严格一致性：语义上只存在一份数据，任何更新看上去都是即时发生的
- 会话一致性客户端在同一会话域发起的请求，通常绑定在同一个节点，客户端可以立即看到自己的更新，但不保证看到其他客户端的更新
- 单调读一致性：保证时间上的单调性，即客户端在未来的请求中，只会读到比当前版本更新的数据
- 最终一致性：最弱的保证，更新时客户端可能看到不一致的视图。适用于并发访问同一数据概率很小或业务场景对一致性要求不高的场景

> [!note] 一致性管理：为满足分区容错性，如何按照一致性协议使分布式系统的多个服务器状态达成一致

常见一致性管理方法
- [[Quorum 的 NWR 策略|Quorum 的 NWR 策略]]
- [[Paxos 算法|Paxos 算法]]
- [[Raft 算法|Raft 算法]]
- [[向量时钟机制|向量时钟机制]]
	
# 参考

```cardlink
url: https://www.buptpress.com/StaticPage/bookcontent_D9B93FE67CCCD7DE.html
title: "NoSQL数据库技术"
host: www.buptpress.com
```
