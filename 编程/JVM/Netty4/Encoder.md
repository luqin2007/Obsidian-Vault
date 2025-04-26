接收数据有 `ByteToMessageDecoder`、`MessageToMessageDecoder`，而发送数据也有对应的 `MessageToByteEncoder`，`MessageToMessageEncoder` 将 Java 对象转换成 `ByteBuf`，对应 `ChannelOutboundHandlerAdapter`

‍
