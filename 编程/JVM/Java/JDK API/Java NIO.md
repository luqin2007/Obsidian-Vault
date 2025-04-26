基于 `Channel` 与 `Buffer`，直接操作多个字节的方法。由于有了 `Buffer` 缓冲区的存在，可以在 IO 读写时期挂起读写操作，实现非阻塞性 IO。

* `Channel`：通道，类似一个双向的 Stream，实现从 Buffer 读取数据或向 Buffer 写入数据
    * 文件：`FileChannel`
    * UDP：`DatagramChannel`
    * TCO：`SocketChannel`、`ServerSocketChannel`
* `Buffer`：缓冲区，暂存读取到或待写入的数据
    * 对应基本类型：`ByteBuffer`，`CharBuffer`，`IntBuffer` 等
    * `MappedByteBuffer`
    * `HeapByteBuffer`
    * `DirectByteBuffer`
* `Selector`：选择器，用于监听多个 Channel 的事件，实现 IO 复用（一个线程管理多组 IO 操作）

![[Pasted image 20240806152015.png]]
# 文件读写

>[!note] JDK 传统文件读写 API、网络 API 都已经通过 NIO 重构过。

`RandomAccessFile` 表示一个文件，类似于 File

```java
try(RandomAccessFile file = new RandomAccessFile("src/nio.txt", "rw");
    FileChannel channel = file.getChannel()) {
    // 申请接收文件内容的缓冲区
    ByteBuffer buf = ByteBuffer.allocate(2048);
    while (channel.read(buf) != -1) {
        // 缓冲区复位
        buf.flip();
        // 读取缓冲区中的内容
        while (buf.hasRemaining()) {
            System.out.println((char) buf.get());
        }
        // 压缩缓冲区
        buf.compact();
    }
} catch (IOException e) {
    throw new RuntimeException(e);
}
```
# Buffer

Buffer 表示一个接收数据的缓冲区容器，存储 Channel 从文件、网络等地方读取的数据，或将 Buffer 中的数据发送到其他位置
* `channel.read(buffer)`：从 Channel 中读数据到 Buffer
* `channel.write(buffer)`：Channel 读 Buffer 中的数据
* `buffer.get()` 表示从 Buffer 中读数据
* `buffer.put(value)` 表示将数据写入 Buffer

位置标记：Buffer 内置几个位置标记表示当前缓冲区的读写位置
* `capacity`：缓冲区数组的总长度
* `position`：表示下一个要操作的数据元素的位置
* `limit`：缓冲区数组中不可操作的下一个元素的位置，满足 `limit<=capacity`
* `mark`：用于记录当前 `position` 的前一个位置，默认是 -1

![[Pasted image 20240806152219.png]]

除了直接操作标记位置，或 `get`、`put` 方法，还有几个方法如下：
* `flip()`：将 `limit` 设置成 `position` 的值，并将 `position` 位置置 0
* `clear()`：将 `limit` 设置成 `capacity`，并将 `position` 位置置 0
* `compact()`：将所有未读数据拷贝到起始处，将 `position` 置最后一个未读元素后，`limit` 置为 `capacity`
* `mark()` 方法记录当前 `position` 位置，`reset()` 方法可以恢复这个位置，`rewind()` 可以清空记录

# Socket

在 Socket 通信中，可以使用 `configureBlocking(false)` 启用非阻塞特性，`accept()` 方法将不会被阻塞。

* 客户端代码 使用 NIO

  ```java
  ByteBuffer buffer = ByteBuffer.allocate(1024);
  try(SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
      if (socketChannel.finishConnect()) {
          int i = 0;
          while (true) {
              TimeUnit.SECONDS.sleep(1);
              buffer.clear();
              buffer.put(("The " + (i++) + "-th information from client").getBytes());
              buffer.flip();
              while (buffer.hasRemaining()) {
                  socketChannel.write(buffer);
              }
          }
      }
  } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
  }
  ```

* 服务端代码 使用传统 IO

  ```java
  try(ServerSocket serverSocket = new ServerSocket(8080)) {
      while (true) {
          Socket socket = serverSocket.accept();
          try(InputStream in = socket.getInputStream()) {
              byte[] temp = new byte[1024];
              int size = 0;
              while ((size = in.read(temp)) != -1) {
                  // 已读取部分数据到 temp[]
              }
          }
      }
  } catch (IOException e) {
      throw new RuntimeException(e);
  }
  ```
# Selector

选择器，可以管理多个 IO 操作（Channel 对象），在 IO 等待期间节省 CPU 资源

Selector 是一个多路开关，当一个 Channel 有一个 IO 操作时会通知 Selector，并触发对应操作。

>[!note] Selector 配合 Channel 使用时，必须保证 Channel 是非阻塞状态的，即 `configureBlocking(false)`。

Selector 使用 `Selector.open()` 创建，使用 `Channel#register` 方法注册。

* `int ops`：`SelectionKey` 中的 `OP_XXX` 的常数，通过 `|` 连接

|OP 标识|检查方法|
| ---------| ----------|
|`OP_READ`|`isReadable()`|
|`OP_WRITE`|`isWritable()`|
|`OP_CONNECT`|`isConnectable()`|
|`OP_ACCEPT`|`isAcceptable()`|
* `Object att`：绑定一个对象可在处理时使用

每次循环处理，使用 `Selector#select()` 方法接收待处理的 `Channel`，之后使用 `Selector#selectedKeys` 获取所有待处理事件的 `Set<SelectionKey>`，处理完成后需要从其中手动删除。

|`SelectionKey` 方法|说明|
| -----------| ------------------------------------------|
|`readyOps()`|`Channel#register()` 中 ops 参数，但常用几个检查方法检查|
|`attachment()`|`Channel#register()` 中 att 参数|
|`channel()`|调用 `Channel#register()` 的 `Channel` 对象|

`Selector#select()` 方法阻塞等待 IO，且可以设置一个超时时间。若需要非阻塞直接返回，通过 `selectNow()` 方法。以上方法返回一个 int 表示待处理 Channel 的数量。

```java
// 准备多个通道
SelectableChannel[] channels = new SelectableChannel[0];
try(Selector selector = Selector.open()) {
    // 注册
    for (SelectableChannel channel : channels) {
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }
  
    // 轮询处理
    while (true) {
        // 是否有 IO 事件
        if (selector.select(TIMEOUT) == 0) {
            continue;
        }
    
        // 处理
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iter = keys.iterator();
        while (iter.hasNext()) {
            SelectionKey key = iter.next();
        
            if (key.isAcceptable()) {
                // acceptable
            }
        
            if (key.isReadable()) {
                // readable
            }
        
            if (key.isWritable() && key.isValid()) {
                // writable
            }
        
            if (key.isConnectable()) {
                // connectable
            }
        
            // 处理完成后，移除
            iter.remove();
        }
    }
} catch (IOException e) {
    throw new RuntimeException(e);
}
```

以 TCP 连接为例，一个服务端要处理多个客户端的请求，使用 NIO 处理

```java
// 服务端
class ServerConnect {

    // 缓冲区每次读数据大小
    private static final int BUF_SIZE = 1024;
    // 端口及超时
    private static final int PORT = 8080;
    private static final int TIMEOUT = 3000;

    // 入口
    public void selector() {
        try (Selector selector = Selector.open();
             ServerSocketChannel ssc = ServerSocketChannel.open()) {

            // 配置服务端
            ssc.socket().bind(new InetSocketAddress(PORT));
            // 设置 Selector
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            // 数据处理
            while (true) {
                // 超时
                if (selector.select(TIMEOUT) == 0) {
                    continue;
                }

                // 处理每个 Channel
                Iterator<SelectionKey> itr = selector.selectedKeys().iterator();
                while (itr.hasNext()) {
                    SelectionKey key = itr.next();

                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }

                    if (key.isReadable()) {
                        handleRead(key);
                    }

                    if (key.isWritable() && key.isValid()) {
                        handleWrite(key);
                    }

                    itr.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 当消息第一次被接受时调用，直接转 Read，由 selector 方法注册
     * @param key Channel Key
     */
    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        // SelectionKey.OP_READ 表示 isReadable() = true
        // 给定的 Object 可以通过 attachment() 获取
        // SocketChannel 通过 channel() 获取
        sc.register(key.selector(), 
                    SelectionKey.OP_READ,
                    ByteBuffer.allocate(BUF_SIZE));
    }

    /**
     * 当 Channel 被转发到去读方法时调用，由 handleAccept 中转发
     * @param key Channel Key
     */
    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        // 直接 print 到控制台
        while (sc.read(buf) > 0) {
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.println((char) buf.get());
            }

            buf.clear();
        }
    
        // 读完后将待回复内容重新写入 Channel
        buf.put("OK".getBytes());
        sc.register(key.selector(), SelectionKey.OP_WRITE, buf);
    }

    /**
     * 当 Channel 被转发到写方法时调用，由 handleRead 中转发
     * @param key Channel Key
     */
    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        // 直接将 Buffer 里的内容传递到 Channel
        buf.flip();
        while (buf.hasRemaining()) {
            sc.write(buf);
        }
        buf.compact();
    }
}
```
# MappedByteBuffer

`MappedByteBuffer` 类用于内存映射，可以直接根据内存地址操作内存，对应操作系统的虚拟内存，可用于处理超大文件读写。

> [!note] `ByteBuffer` 有两种方案
>
> * 间接模式：读写堆内存，使用 `ByteBuffer` 的静态函数申请内存或包装数组都是这种模式
> * 直接模式：直接读写操作系统的虚拟内存，无法直接创建，可以从其他 `Channel` 获取，如 `FileChannel` 的 `map` 方法

使用 `MappedByteBuffer` 对操作系统内存映射的文件进行读写可以省去一次数据复制过程，主要由于操作系统可以直接将文件映射为内存由 `MappedByteBuffer` 管理，而不需要将被映射区域再次复制到堆内存中。

跟用用处不同，`map` 的第一个参数接收一个  Buffer 的行为
* `READ_ONLY`：只读
* `READ_WRITE`：可读可写，更改会同步到文件中
* `PRIVATE`：专用，可更改但不会影响到原始文件

```java
try(RandomAccessFile file = new RandomAccessFile("...", "rw");
    FileChannel fc = file.getChannel()) {
    MappedByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
    // do something
} catch (IOException e) {
    throw new RuntimeException(e);
}
```

相对于 `ByteBuffer`，扩展的方法包括：
* `force()`：READ_WRITE 模式下，将缓冲区内的修改强制写回文件
* `load()`：将缓冲区的内容载入内存，并返回其引用
* `isLoaded()`：缓冲区内容是否已存在于物理内存中

通常，`MappedByteBuffer` 的销毁依赖于 GC，因此时间不确定
# Channel
## 分散处理

当需要将来自同一个源的数据分散到多个 `Buffer` 中，各自处理后再输入到同一个 `Channel` 中，可使用接收数组的 `read`、`write` 方法

```java
try (FileChannel channel = FileChannel.open(null)) {
    ByteBuffer[] bufs = new ByteBuffer[] {
            ByteBuffer.allocate(1024),
            ByteBuffer.allocate(1024)
    };
    channel.read(bufs);
    channel.write(bufs);
} catch (IOException e) {
    throw new RuntimeException(e);
}
```
## transfer

`FileChannel` 和 `SocketChannel` 中的 `transferFrom`、`transferTo` 可将数据从直接在两个 Channel 之间传播
# Pipe

管道是两个线程甚至进程之间的单向数据传递方式，数据从 `Sink` 通道传递到 `Source` 通道中。

```java
try {
    Pipe pipe = Pipe.open();
    ExecutorService exec = Executors.newFixedThreadPool(2);
    // 线程1 发送消息
    exec.submit(() -> {
        try {
            Pipe.SinkChannel sink = pipe.sink();
            // 发送 10 条数据
            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(1);
                byte[] message = ("Pipe message at " + System.currentTimeMillis() + "\n").getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(message);
                while (buffer.hasRemaining()) {
                    sink.write(buffer);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    });
    // 线程2 接收消息
    exec.submit(() -> {
        try {
            Pipe.SourceChannel source = pipe.source();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                source.read(buffer);
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                buffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });
} catch (IOException e) {
    throw new RuntimeException(e);
}
```
# DatagramChannel

`DatagramChannel` 用于收发 UDP 包，发送和接收的是数据包

````tabs
tab: 接收
```java
DatagramChannel channel = DatagramChannel.open();

ByteBuffer buffer = ByteBuffer.allocate(1024);
channel.socket().bind(new InetSocketAddress(8080));
channel.receive(buffer);
buffer.flip();
```

tab: 发送
```java
DatagramChannel channel = DatagramChannel.open();

ByteBuffer buffer = ByteBuffer.allocate(1024);
buffer.put("Message".getBytes());
buffer.flip();
channel.send(buffer, new InetSocketAddress(8080));
```
````
