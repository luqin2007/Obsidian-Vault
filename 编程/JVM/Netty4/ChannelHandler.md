`ChannelHandler` 接口定义了 `handlerAdded`，`handlerRemoved`，`exceptionCaught` 方法，便于实现自定义业务，如日志、编解码、过滤等。
* `handlerAdded`：添加到实际上下文并开始处理事件时调用
* `handlerRemoved`：从上下文中移除时调用
* `exceptionCaught`：处理时出现异常时调用
* `@Sharable`：注解，表示该 `ChannelHandler` 可以在多个 `ChannelPipeline` 中共享
# ip 过滤

Netty 内置了一套实现 IP 地址过滤的机制，位于 `io.netty.handler.ipfilter` 包，便于实现白名单、黑名单等功能。
## RuleBasedIpFilter

根据一定的规则进行过滤，设定对应 IP 的 `ACCEPT` 或 `REJECT`，对于单个 IP 可以通过 `IpSubnetFilterRule` 定义
## UniqueIPFilter

保证每个 ip 在同一时间只有一个连接到服务器的通道。
## 流量整形

限制服务端发送速度，防止客户端来不及接收造成丢包

> [!note] 原理：报文发送过快时，将其进行缓存，当缓冲区满时向客户端发送信息告知服务器不再接受消息

> [!danger] 流量整形可能增加延迟。

`AbstractTrafficShapingHandler` 用于流量整形，`TrafficCounter` 用于流量统计。
* 管道流量整形：`ChannelTrafficShapingHandler`
* 全局管道流量整形：`GlobalChannelTrafficShapingHandler`
* 全局流量整形：`GlobalTrafficShapingHandler`
## TrafficCounter

统计限速流量。在给定 `checkInterval` 内，周期性计算入站、出站流量信息，并调用 `AbstractTrafficShapingHandler#doAccounting` 方法。若 `checkInterval == 0` 则不进行计数。
# Adapter

扩展的 `ChannelHandler`，具体业务在各种 `ChannelHandlerAdapter` 中实现。