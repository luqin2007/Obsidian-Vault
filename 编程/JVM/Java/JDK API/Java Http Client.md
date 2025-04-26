#java9 #java19incubator #java11 

提供 HTTP 客户端和 WebSocket API

# HTTP

支持 HTTP/1.1 和 HTTP/2 协议的高层客户端接口，能够发送请求并处理响应。
- 支持  `send` 与 `sendAsync` 同步或异步访问
- 支持重定向和代理等

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpExample {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://openjdk.java.net/"))
            .build();
        
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println)
            .join();
    }
}
```