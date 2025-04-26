![[Pasted image 20240807233213.png]]

* 表空间 Tablespace：`.ibd`  文件。一个 MySQL 实例可以对应多个表空间，存储记录、索引等数据
* 段：Segment
    * 数据段：Leaf node segment，B+ 树的叶节点
    * 索引段：Non-leaf node segment
    * 回滚段：Rollback segment
* 区：表空间的单元结构，默认每个区大小为 1M，包含 64 个连续页。InnoDB 申请内存空间以区为单位，为保证数据连续性，每次申请 4-5 个区
* 页：InnoDB 磁盘管理的最小单位，默认大小为 16K
* 行：InnoDB 引擎中的数据，除每条数据得的每个字段外，还包含以下内容：
    *  `Trx_id` ：最后一次事务操作的事务 id
    *  `Roll_pointer` ：每次写入时，会把旧版本记录到 `undo`  日志，该字段用于查找以前版本的数据
# 目录

- [[架构]]
- [[事务原理]]
- [[多版本并发控制]]