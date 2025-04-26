一个接收任何数据，并将其直接抛弃，不响应任何数据的服务器

```reference
file: "@/_resources/codes/netty4/src/main/java/com/example/discard_server/DiscardServer.java"
start: 10
end: 38
```

`EventLoopGroup`：处理传输的 IO 多线程事件循环器，通常使用 `NioEventLoopGroup`，负责线程池和接收信息的调度。

`ServerBootstrap`：服务器 NIO 启动辅助类。一般不会直接从该类使用 Channel

|方法|说明|
| --------------| ---------------------------------------------------------------------------------------------------------|
|group|注册事件循环器|
|channel|决定如何将传入的连接转换成 `Channel`，这里使用 `NioServerSocketChannel`|
|childHandler|处理已接收的 `Channel` 的处理器|
|option|`Channel` 实现的配置参数，`TCP/IP` 服务器可实现 `SO_KEEPALIVE` 等|
|bind|绑定监听端口并阻塞等待，返回的是一个 `Future`，表示一个（可能）还未发生的 IO 事件，Netty 的所有操作都是异步的|

`closeFuture()` 方法可以关闭一个连接，这是一个异步事件

异步事件的 `sync()` 方法可以转换成同步事件，阻塞到事件完成

实例中创建了两个 `EventLoopGroup` 循环器

* boss：接收进来的连接，会将连接传递给 worker
* worker：处理要发送出去的数据

注册了一个 `ChannelInitializer` 类型的处理器，初始化了一个处理管道，使一个 `Channel` 依次经过多个处理器。管线中只有一个 `DiscardServerHandler` 自定义的处理器，仅丢弃数据

```reference
file: "@/_resources/codes/netty4/src/main/java/com/example/discard_server/DiscardServer.java"
start: 40
end: 54
```