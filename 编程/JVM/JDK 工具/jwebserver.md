#java18

简单的静态 Web 服务器

```shell
javawebserver -p <端口> -d <资源目录> -o <日志级别>
```

# API

```java
import java.net.InetSocketAddress;
import jdk.jwebserver.SimpleFileServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Simple Web Server...");
        var addr = new InetSocketAddress(9000);
        var dir = Path.of("/");
        var server = SimpleFileServer.createFileServer(addr, dir, SimpleFileServer.OutputLevel.INFO);
        server.start();
    }
}
```
