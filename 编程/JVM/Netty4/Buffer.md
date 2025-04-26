Netty 的缓冲区使用自己的 `ByteBuf` 而非 NIO 的 `ByteBuffer`，相对起来有如下优势：
* 自定义缓冲类型，读写操作更方便
* 透明零拷贝实现，`Unpooled#wrappedBuffer()` 可以直接合并多个 Buffer 
* 开箱即用的动态缓冲区，支持自动扩充缓冲区大小
* **不需要 `flip()` 操作**
* 正常情况下，响应效率比 `ByteBuffer` 更好，没有复杂的边界和索引补偿
