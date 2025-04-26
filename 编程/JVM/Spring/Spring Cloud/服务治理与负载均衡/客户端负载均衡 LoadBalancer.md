# 负载均衡方案

> 传统负载均衡方案
> 集中式负载均衡，专门硬件（`F5`）或特定软件（`VS`，`HAproxy` 等）保存有所有服务的地址映射表。服务消费者调用某个服务时向均衡系统发起请求，按某种策略均衡后转发给目标服务
> - 单点失效：一旦均衡服务失效，整个应用无法访问
> - 难扩展：至少需要人工介入，难以实现快速启停
> - 复杂

微服务为单一职责的细粒度服务构成的分布式网状结构，负载均衡（`Load Balancing`，`LB`）策略主要有：
- 集中式负载均衡方案：与传统方案一致，在服务消费者与提供者之间有独立的负载均衡系统

![[../../../../../_resources/images/客户端负载均衡 Ribbon 2024-10-01 18.49.37.excalidraw|50%]]

- 进程内负载均衡方案：将负载均衡整合到服务消费者应用中
	- 服务消费者启动时从治理服务器获取所有服务信息并定期同步
	- 访问服务时通过内置均衡器以某种策略选择一个目标服务实例

![[../../../../../_resources/images/客户端负载均衡 Ribbon 2024-10-01 18.52.09.excalidraw|50%]]

- 主机独立负载均衡进程方案：类似进程内负载均衡方案，但负载均衡和服务发现功能转换为一个独立进程，供一个或多个消费者使用

![[../../../../../_resources/images/客户端负载均衡 Ribbon 2024-10-01 18.59.23.excalidraw|50%]]

`Ribbon` 子项目与 `Eureka` 无缝结合，提供第二种负载均衡策略

> [!missing] Ribbon 已停更，被 Spring Cloud LoadBalancer 代替，属于 Spring Cloud Commons 项目

# 客户端负载均衡

由于负载均衡在服务消费者端生效，只需要在消费者项目中添加对应依赖即可

>[!note] `org.springframework.cloud:spring-cloud-starter-loadbalancer` 依赖包含于 `org.springframework.cloud:spring-cloud-dependencies` 中，因此无需额外依赖

在微服务使用的入口使用 `@LoadBalanced` 注解 `RestTemplate` 对象的创建方法即可
- `@FeignClient` 注解的接口默认启用负载均衡

```reference hl:15
file: "@/_resources/codes/spring-cloud/shopping-product-service-resttemplate/src/main/java/com/example/shopping/ShoppingProductServiceUseRestTemplate.java"
start: 14
end: 18
```

## 负载均衡测试

使用 `--server.port=xxxx` 指定不同接口启动多个服务实例，多次访问服务即可查看效果
- 默认采用轮询策略，交替访问不同服务实例
- 使用 `@Value("${server.port}")` 注入所在端口

`````col
````col-md
flexGrow=1
===
![[../../../../../_resources/images/Pasted image 20241001231206.png]]
````
````col-md
flexGrow=1
===
![[../../../../../_resources/images/Pasted image 20241001231313.png]]

结果：多次请求 UserService 所用端口不同

![[../../../../../_resources/images/Pasted image 20241001231721.png]]
````
`````

# 参考

```cardlink
url: https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/loadbalancer.html
title: "Spring Cloud LoadBalancer :: Spring Cloud Commons"
host: docs.spring.io
favicon: ../_/img/favicon.ico
```
