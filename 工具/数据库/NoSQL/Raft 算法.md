分布式架构中日志一致性管理算法，也是 Zookeeper 的核心算法
- 领导者 Leader：一个，接受客户端请求，将日志复制到其他节点，告知其他节点何时应用日志是安全的
- 候选者 Candidate：多个，在 Leader 任期结束时从中选举新领导者
- 追随者 Follower：多个，响应来自 Leader 和 Candidate 的请求

# 领导者选举

1. 程序启动时，所有节点均为 Follower
2. 选举超时，即 Follower 一段时间没有接收到任何消息，开始投票选举 Leader
	1. 随机计时器机制
		- Follower 在随机时间转化为 Candidate，防止同时有多个候选者转换导致票数不足
		- Leader 心跳包可以重置 Follower 计时器，避免存在 Leader 时产生选举
	1. 自身 `term+1` 转化为 Candidate，并请求其他节点投票
	2. 当自身获取超过半数票，转化为 Leader
	3. 当有其他节点成为 Leader
		- Leader 的 `term` 不小于自身，重新转换为 Follower
		- Leader 的 `term` 小于自身，拒绝该 Leader 并继续选举

# 日志复制

当 Leader 接收到客户端修改请求时，根据请求指令追加成一个日志条目，通过 AppendEntries RPC 并行发送其他节点，当大多数节点返回成功后即可将指令放回状态机，并返回执行结果。

当 Follower 失败或无返回时，Leader 会无限重发 AppendEntries 直到所有 Follower 都执行成功

日志条目 Log Entry 包含 index 和 term
- Log Entry 在相同日志中，index 与 term 相同时，指令相同
	- Leader 在特定 term 和 index 下只会创建一个指令
	- Log Entry 不会改变在日志中的位置
- Log Entry 在不同日志中，index 与 term 相同时，该指令之前的所有指令相同 
	- AppendEntries 会附带前一个 Log Entry 的 index 和 term，当不存在时拒绝接受

# 安全性