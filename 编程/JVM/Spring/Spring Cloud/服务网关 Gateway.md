> [!note] API 服务网关：微服务边界上的一个面向 API 的、串行集中式的、对访问请求强管控的服务
> API 服务网关是为微服务访问的统一入口，负责服务请求路由、组合及协议转换等功能

![[../../../../_resources/images/服务网关 2024-10-03 23.38.33.excalidraw]]

Spring Cloud 集成 `Zuul` 项目实现网关功能
- 路由：核心功能，将外部请求转发至微服务，实现统一访问入口
- 过滤：干预请求过程，实现请求校验、服务聚合等功能

> [!missing] `Zuul` 已被 `Spring Gateway` 替代

# 创建 Gateway 路由服务

创建一个 Springboot 应用，基于 [[服务治理与负载均衡/服务治理 Eureka#服务提供者|Eureka 客户端]]进行相关配置
- 添加依赖项：`org.springframework.cloud:spring-cloud-starter-gateway`
- 关闭注册服务，开启 `gateway`

```reference hl:8,18,20
file: "@/_resources/codes/spring-cloud/shopping-gateway-before7/src/main/resources/application.yml"
start: 4
end: 20
```

在服务消费者中，使用 `gateway` 访问即可

`````col
````col-md
flexGrow=2
===
```embed-java
PATH: "vault://_resources/codes/spring-cloud/shopping-product-service-before7/src/main/java/com/example/shopping/service/UserService.java"
LINES: "5-10"
```
````
````col-md
flexGrow=1
===
```embed-properties
PATH: "vault://_resources/codes/spring-cloud/shopping-product-service-before7/src/main/resources/application.properties"
LINES: "31"
```
````
`````

---

- 负载均衡：Gateway 默认集成了负载均衡，不需要额外设置
- 健康监控：`management.endpoints.web.exposure: gateway
- Resilience4j 断路器：在配置器中添加 [CircuitBreaker](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/gatewayfilter-factories/circuitbreaker-filter-factory.html) 类型的 `filter`，详见[[#自定义路由]]

# 自定义路由

在配置文件中通过设定 `spring.cloud.gateway.routes` 属性自定义微服务路径
- `id`：过滤器名
- `predicates`：拦截路径，这里匹配 `http://<gateway>/api/users` 开头的所有访问请求
- `uri`：转发的路径前缀，Eureka 服务使用 `lb://<service-name>`，也可以是其他路径
- `filters`：[过滤器](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/gatewayfilter-factories.html)，这里只会截掉第一段（即 `/api`）
	- `StripPrefix`：截断前几段
	- `CircuitBreaker`：集成 `resilience4j` 等容错
	- `RewritePath`：根据正则修改路径
	- `AddRequestHeader`、`MapRequestHeader`、`SetRequestHeader`、` RemoveRequestHeader `：编辑请求头

```reference
file: "@/_resources/codes/spring-cloud/shopping-gateway-before7/src/main/resources/application.yml"
start: 21
end: 27
```

# 过滤器

添加过滤器：
- 在 `application` 配置中，使用 `spring.cloud.gateway.routes.<route>.filters` 配置
- 在 Java 中使用 `bean` 配置：`{java}@Bean public RouteLocator routes(RouteLocatorBuilder builder);`

自定义过滤器：
1. 实现 `GatewayFilterFactory` 接口，可以选择继承自 `AbstractGatewayFilterFactory` 类，重写 `apply` 方法返回一个 `GatewayFilter` 实例

```reference fold
file: "@/_resources/codes/spring-cloud/shopping-gateway-before7/src/main/java/com/example/shopping/filter/JWTTokenFilter.java"
start: 15
end: 63
```

2. 使用：指定 `name` 属性为对应 `FilterFactory` 类名

```reference
file: "@/_resources/codes/spring-cloud/shopping-gateway-before7/src/main/resources/application.yml"
start: 28
end: 30
```

> [!note] 向 `spring.cloud.gateway.default-filters` 添加的过滤器将同时添加到所有 `routes`

