防止某个服务调用失败引起“雪崩效应”，保证核心业务正常运行
- 服务降级
- 熔断机制
- 超时管理
- 回退机制
- 服务限流

Spring Cloud 集成 `Netflix` 的 `Hystrix` 项目，通过断路器模式处理服务降级

> [!missing] 由于 `Hystrix` 不再积极维护，Spring Cloud 推荐使用其他断路器实现，如 `Resilience4j` 等 

> [!note] 断路器模式：服务单元发生故障后，通过断路器故障监控，向调用方返回符合预期的服务降级处理，而不是长期等待或无处理异常

- `CircuitBreakerFactory#configure`
- `CircuitBreakerRegistry.of(CircuitBreakerConfig)`
- `application.properties`：`resilience4j` 开头的配置

# 引入  `Resilience4j`

- 添加依赖：`org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j`
- 开启支持：注册 `Customizer<Resilience4JCircuitBreakerFactory>` 全局配置，通过 `CircuitBreakerFactory` 访问

```reference hl:43,44,54,55
file: "@/_resources/codes/spring-cloud/shopping-product-service-resttemplate/src/main/java/com/example/shopping/api/ProductEndpointImpl.java"
start: 41
end: 56
```

使用 Feign 客户端时，在配置文件中启用，之后可以直接使用 `@FeignClient` 注解的 `fallback` 或 `fallbackFactory` 属性指定
- `fallback`：指定一个对应接口类
- `fallbackFactory`：指定一个 `FallbackFactory` 类

```reference
file: "@/_resources/codes/spring-cloud/shopping-product-service-before7/src/main/resources/application.properties"
start: 27
end: 28
```

- 注解：使用 `@CircuitBreaker(name = "<降级服务名>", fallbackMethod = "<服务方法>")`
- 通过相关 API 编程实现
	- `CircuitBreakerFactory#create(name).run(func, fallback)`
	- `CircuitBreakerRegistry#circuitBreaker(name).executeSupplier`
- OpenFeign 集成：添加配置并使用 `@FeignClient(name, fallback/fallbackFactory)`

# 服务隔离

> [!note] 防止任何单一依赖使用掉整个容器的全部用户线程

Resilience4j 提供线程池隔离与信号量隔离两种方式

> [!note] 线程池隔离：不同服务使用不同线程池，同时用户请求与业务执行线程隔离
> - 优点：高度隔离，快速失败
> - 缺点：线程上下文切换开销

> [!note] 信号量隔离：通过信号量大小控制业务并发访问量
> - 优点：不需要新线程池，高效、易用
> - 缺点：灵活性低

`resilience4j.bulkhead`：服务隔离方式，需要 `io.github.resilience4j:resilience4j-bulkhead` 依赖，已包含于 `spring-cloud-starter-circuitbreaker-resilience4j` 中

| 配置                             | 默认值  | 说明                                  |
| ------------------------------ | ---- | ----------------------------------- |
| `max-concurrent-calls`         | 10   | 最大并发量                               |
| `max-wait-duration`            | 1000 | 最大等待时间，单位 ms，通常设置为平均响应时间的 120%-200% |
| `writable-stack-trace-enabled` |      | 发生错误时，是否保持异常栈，而不是使用 R4j 的异常替代       |

```properties title:application.properties
# 创建一个名为 backendA 的隔离配置
resilience4j.bulkhead.instances.backendA.max-concurrent-calls=10  
resilience4j.bulkhead.instances.backendA.max-wait-duration.seconds=2
```

使用时，通过 `@Bulkhead` 注解指定
- `name`：之前创建的隔离配置名
- `fallbackMethod`：降级服务方法
- `type`：隔离模式（线程池，信号量等）

```java
@Bulkhead(name = "backendA", fallbackMethod = "fallbackMethod" type = Bulkhead.Type.SEMAPHORE)
@GetMapping("/test")
public String testBulkheadIsolation() {
    // 执行需要隔离的操作
    return "Success";
}
```

# 请求缓存

依赖： `resilience4j-cache`

```xml title:pom.xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-cache</artifactId>
</dependency>
```

通过 `JCache` 获取缓存对象，`resilience4j` 实际支持任何类型缓存

```java
// Create a CacheContext by wrapping a JCache instance.
javax.cache.Cache<String, String> cacheInstance = Caching
  .getCache("cacheName", String.class, String.class);
Cache<String, String> cacheContext = Cache.of(cacheInstance);

// Decorate your call to BackendService.doSomething()
CheckedFunction1<String, String> cachedFunction = Decorators
    .ofCheckedSupplier(() -> backendService.doSomething())
    .withCache(cacheContext)
    .decorate();
String value = Try.of(() -> cachedFunction.apply("cacheKey")).get();
```

```cardlink
url: https://resilience4j.readme.io/docs/cache
title: "Cache"
description: "Getting started with resilience4j-cache"
host: resilience4j.readme.io
image: https://files.readme.io/8701241-small-Resilience4j.png
```

# 监控

resilience4j 支持 `Actuator`，也支持 `Prometheus`、`Grafana` 等系统
- `/actuator/health`：检查系统的健康状况，包括 Resilience4j 的各个组件状态。
- `/actuator/circuitbreakers`：查看熔断器的状态和统计信息。
- `/actuator/retries`：查看重试机制的状态和统计信息。
- `/actuator/ratelimiters`：查看限流器的状态和统计信息。

也可以注册相关事件处理

```java
@Component
public class CustomCircuitBreakerEventListener {

    @EventListener
    public void onCircuitBreakerEvent(CircuitBreakerEvent event) {
        // 处理熔断器事件，例如记录日志或发送通知
        System.out.println("Circuit Breaker Event: " + event);
    }
}
```
