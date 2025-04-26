Feign 是一个声明式 WebService 客户端，用于防止 URL 字符串拼接，简化微服务调用
- 支持 HTTP 编解码器，支持 Spring Mvc 的 HttpMessageConverter
- 支持 Feign 注解和 JAX-RS 注解
- 支持 Hystrix 及其回退功能，支持 LoadBalancer 负载均衡
- 支持 HTTP 请求、响应压缩

>[!missing] Feign 已被替换成其社区版 `OpenFeign`

# 简化服务调用

1. 添加 `openfeign` 项目依赖

```reference
file: "@/_resources/codes/spring-cloud/shopping-product-service-before4.7/pom.xml"
start: 47
end: 50
```

2. 在微服务消费者应用入口添加 `@EnableFeignClients` 接口

```reference hl:9
file: "@/_resources/codes/spring-cloud/shopping-product-service-before4.7/src/main/java/com/example/shopping/ShoppingProductService.java"
start: 8
end: 11
```

3. 创建微服务请求接口 `UserService`，并使用 `@FeignClient` 注解标记

```reference hl:10,13,16,19,22
file: "@/_resources/codes/spring-cloud/shopping-product-service-before4.7/src/main/java/com/example/shopping/service/UserService.java"
start: 10
```

4. 注入 `UserService`，在使用微服务的地方使用接口访问

```embed-java
PATH: "vault://_resources/codes/spring-cloud/shopping-product-service-before4.7/src/main/java/com/example/shopping/api/ProductEndpointImpl.java"
LINES: "16-17,21-22,38-39,41-43,47-48"
```

# 参数绑定

OpenFeign 兼容 Spring MVC 参数绑定注解
- `@RequestParam`
- `@PathVariable`
- `@RequestHeader`
- `@ReauestBody`

# 继承

OpenFeign 支持继承，可以将服务接口拆出来单独成包，在服务提供者和接收者处同时依赖实现即可

> [!error] 注意：OpenFeign 不支持类上的 `@RequestMapping` 注解

````tabs
tab: User API

```embed-java
PATH: "vault://_resources/codes/spring-cloud/shopping-user-api-before9.4/src/main/java/com/example/shopping/api/UserService.java"
LINES: "12,20-21,27-28,35-36,42-43,44"
```

tab: User 服务

```embed-xml
PATH: "vault://_resources/codes/spring-cloud/shopping-user-service-before7/pom.xml"
LINES: "63-67"
```

```embed-java
PATH: "vault://_resources/codes/spring-cloud/shopping-user-service-before7/src/main/java/com/example/shopping/api/UserEndpoint.java"
LINES: "14-18,47"
```

tab: Product 服务

```embed-xml
PATH: "vault://_resources/codes/spring-cloud/shopping-product-service-before7/pom.xml"
LINES: "80-84"
```

```embed-java
PATH: "vault://_resources/codes/spring-cloud/shopping-product-service-before7/src/main/java/com/example/shopping/service/UserService.java"
LINES: "5-10"
```
````
