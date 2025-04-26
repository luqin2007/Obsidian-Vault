使用 `filter` 可以为用于测试的客户端添加的授权

```java
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

WebClient webClient = WebClient.builder()
        .filter(ExchangeFilterFunctions.basicAuthentication("user", "pwd"))
        .build();
```
