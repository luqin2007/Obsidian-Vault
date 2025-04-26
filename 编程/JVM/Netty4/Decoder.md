类似于 `TCP/IP` 等基于流的传输方法，每次连接都是接收一系列字节包。若传输过程中进行了拆分，一次 `channelRead` 接收的数据可能是不完整的。且即使发送端通过多次 `flush` 发送数据，每次接收的也不一定是与每次发送的相同，只能保证最后所有的字节连接起来是相同的

````col
```col-md
flexGrow=1
===
比如，发送一条信息 `ABCDEFGHI`，可能发送的过程被拆分成多个部分
```
```col-md
flexGrow=1
===
![[Pasted image 20230902124645-20240311180414-27z550o.png]]
```
````

````col
```col-md
flexGrow=1
===
但多次 `channelRead` 中，每次接收的数据可能是
```
```col-md
flexGrow=1
===
![[Pasted image 20230902124706-20240311180425-hjuvo99.png]]
```
````

网络传输只能保证最后接收的数据一定是 `ABCDEFGHI`，因此需要在 `Handler` 中合并。
* `handleAdded`：当连接加入 Netty 事件序列时调用
* `handlerRemoved`：当连接从 Netty 事件序列移除时调用

```java title:ComposeBufferHandler fold
public class ComposeBufferHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf buffer;
  
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buffer = ctx.alloc().buffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        ReferenceCountUtil.release(buffer);
        buffer = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        buffer.writeBytes((ByteBuf) msg);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 数据读取完成，使用数据
        System.out.println(buffer.toString());
    }
}
```
# MessageToMessageDecoder

`MessageToMessageDecoder` 用于处理来自 `ByteToMessageDecoder` 输出的数据，其中泛型 `<T>` 是 `ByteToMessageDecoder` 解析出的数据类型。
# ByteToMessageDecoder

`ByteToMessageDecoder` 用于处理流数据，是 `ChannelInboundHandler` 的实现，应重写 `decode` 方法
* 自动累计接收的 `ByteBuf` 数据流，第二个参数 `in` 即累积的数据流
* 每当判断数据流可以组成一个完整的数据，在方法中构造出对应数据并加入第三个参数 `out` 列表中

以下示例中，当接收的数据超过 4 个字节，就读出一个无符号整型数据存入输出列表

```java
public class MyByteToMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() >= 4) {
            out.add(in.readUnsignedInt());
        }
    }
}
```

Netty 内置了一系列 `ByteToMessageDecoder` 实现，用于处理不同数据协议（如 HTTP，SSL 等）、不同数据类型（XML，JSON，Zlib 等）

![[Pasted image 20230902144618-20240311180520-aukeqch.png]]

经过这类 Decoder 后，通过管道传递到后面的对象就不是 ByteBuf 了，对应 `channelRead` 的 `msg` 对象就是对应上个 Decoder 输出的类型了。
