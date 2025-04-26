基于消息传递解决分布式系统数据一致性问题。
- 提出数据修改指令的节点称为 Leader 节点
- Paxos 算法维护全局唯一序列号，决定哪个建议是最新的

Paxos 算法的写操作需要通过两步实现：

1. Leader 节点发出请求准备 Prepare 消息，等待大多数 Replica 节点返回 Promise

![[../../../../_resources/images/Paxos 算法 2024-10-10 10.55.38.excalidraw|80%]]

2. Leader 节点发出正式建议 Prepare 消息，等待大多数 Replica 节点返回 Accept

![[../../../../_resources/images/Paxos 算法 2024-10-10 10.59.05.excalidraw|80%]]

- 读数据时，客户端从系统中大多数节点获取同意接受的值
- 写数据时，不允许在没有达成共识时产生任何写入，保证数据一致性
