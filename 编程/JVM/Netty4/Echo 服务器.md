应答服务器，接收数据并原样返回。

比起 Discard 抛弃服务多了一步返回数据，所以只需要将 `DiscardServerHandler` 改动一下即可。这里将其名称重命名为 `EchoServerHandler`

```reference
file: "@/_resources/codes/netty4/src/main/java/com/example/echo_server/EchoServer.java"
start: 39
end: 53
```

`ChannelHandlerContext` 类提供了大量与流相关的操作，`write` 和 `flush` 分别表示写入缓冲区、提交缓冲区。当然也可以使用 `writeAndFlush` 一步到位

‍
