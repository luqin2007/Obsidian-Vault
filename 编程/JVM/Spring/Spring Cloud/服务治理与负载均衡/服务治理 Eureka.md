# 构建服务治理

![[../../../../../_resources/images/服务治理 Eureka 2024-10-01 17.53.13.excalidraw|50%]]

## 服务治理服务器

服务治理服务器即 Eureka 服务器，在基础的 Spring 服务（不需要 Spring Web）上，引用以下依赖：
- `org.springframework.cloud:spring-cloud-dependencies`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-server`

在 Spring 配置中进行相关配置：

```properties
# Eureka 服务端口
# Spring 默认端口 8080，Eureka 默认端口 8761
server.port=8260
# 当服务器注册列表为空时，每 5ms 同步一次
eureka.server.wait-time-in-ms-when-sync-empty=5
# 禁用自我注册和发现
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.service-url.defaultZone=http://localhost:8260/eureka
```

实现 Spring 主类

```reference hl:9
file: "@/_resources/codes/spring-cloud/shopping-service-discovery/src/main/java/com/example/shopping/ShoppingServiceDiscovery.java"
start: 8
```

构建完成后，浏览器访问对应端口即可查看 Eureka 服务后台

![[../../../../../_resources/images/Pasted image 20240930234809.png]]

> [!note] `localhost:<port>/eureka/<name>` 可以查看微服务注册信息

## 服务提供者

注册服务，在提供服务所需的项目依赖（如 RESTful 服务依赖于 `spring-boot-starter-web`）上，引用以下依赖：
- `org.springframework.cloud:spring-cloud-dependencies`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client`

配置 Spring，在 `application.properties` 中：

```properties
# Spring 应用名，也是 Eureka 微服务注册名
spring.application.name=shopping-user-service
# 服务端口
server.port=11000

# eureka
# 优先使用 IP 而不是域名注册
eureka.instance.prefer-ip-address=true
# 该项目将注册到治理服务器，作为微服务
eureka.client.register-with-eureka=true
# 该项目将定期从治理服务器获取所有微服务
eureka.client.fetch-registry=true
# 微服务治理服务器地址，带有 /eureka 后缀
eureka.client.service-url.defaultZone=http://localhost:8260/eureka/
```

> [!hint] 通常情况下，Eureka 客户端的 `eureka.client.register-with-eureka` 与 `eureka.client.register-with-eureka` 都为 `true`

> 服务名可通过 `Eureka服务器/eureka/<服务名>` 读取
> - `spring.application.name`：微服务名，注册为 `application.name`，用于服务分组。同名的微服务实例所提供的服务相同
> - `instanceId`：微服务实例 ID，由 `宿主机:服务名:端口` 组成

主类中启用 Eureka 发现客户端

```reference hl:7
file: "@/_resources/codes/spring-cloud/shopping-user-service-before4.7/src/main/java/com/example/shopping/ShoppingUserService.java"
start: 7
```

## 服务消费者

> [!note] 服务消费者也可以是服务提供者
> 设微服务 A 需要依赖于另一个微服务 B 时，微服务 A 本身是一个服务提供者的同时，也是 B 服务的消费者
> ---
> 例如：用户服务 `UserService` 是一个服务提供者，商品服务 `ProductService` 也是一个服务提供者，但商品评论中需要获取评论用户信息，则 `ProductService` 服务也是 `UserService` 服务的消费者

在[[#服务提供者]]的基础上，使用 `RestTemplate` 即可访问注册的微服务，服务地址为 `http://<服务名>/...`

```reference hl:17
file: "@/_resources/codes/spring-cloud/shopping-product-service-resttemplate/src/main/java/com/example/shopping/ShoppingProductServiceUseRestTemplate.java"
start: 14
end: 18
```

```reference hl:48-51
file: "@/_resources/codes/spring-cloud/shopping-product-service-resttemplate/src/main/java/com/example/shopping/api/ProductEndpointImpl.java"
start: 41
end: 56
```

---

# 服务注册与续约

Eureka 实现中遵循 AP 原则，即优先保证可用性、分区容忍性，因为对于微服务消费者，能消费最重要

| application.properties                                 | 默认值    | 说明              |
| ------------------------------------------------------ | ------ | --------------- |
| `eureka.client.register-with-eureka`                   | `true` | 开启服务注册          |
| `eureka.instance.lease-renewal-interval-in-seconds`    | 30     | 服务提供者发送心跳包的时间间隔 |
| `eureka.instance.lease-expiration-duration-in-seconds` | 90     | 服务治理服务器注销服务时间间隔 |

- 服务注册：服务实例状态发生变化时，向 Eureka 服务器更新状态，同时通过 `replicateToPeers()` 同步其他 Eureka 服务器
- 服务续约 Renew：注册成功后，每 30s 向 Eureka 发送心跳包续约，90s 未续约从 Eureka 服务器注册表删除
- Eureka 客户端默认每 30s 向服务器获取注册信息

> [!note] 从注册到可用可能经过的延迟
> - Eureka 客户端（服务实例）启动后不直接注册，等待默认 40s 后才注册
> - Eureka 服务器对服务注册表的缓存使注册后没有直接出现在 `/eureka/apps`，默认 30s
> - Eureka 客户端对注册信息的缓存，默认 30s
> - Eureka 客户端负载均衡后的结果缓存，默认 30s

# 自我保护模式

>[!note] 自我保护模式：当 Eureka 服务器满足某个条件即可进入自我保护模式。此时 Eureka 服务器不再注销任何服务实例
>- 条件：以下条件二选一
>	- 每分钟心跳续约数量低于一个阈值：$服务实例数量 \times \frac{60}{每个实例心跳间隔数}\times 自我保护系数$
>	- 每 5 分钟向其他对等节点复制服务注册数据失败
>- 基于 AP 原则，宁可保留错误服务实例，也不盲目注销可能健康的服务实例

| properties                                      | 默认   | 说明                         |
| ----------------------------------------------- | ---- | -------------------------- |
| `eureka.server.enable-self-preservation`        | true | 是否启用自我保护                   |
| `eureka.client.service-url.defaultZone`         |      | 其他对等节点                     |
| `eureka.server.renewal-percent-threshold`       | 0.85 | 自我保护系数                     |
| `eureka.server.wait-time-in-ms-when-sync-empty` | 0    | 当服务为空时，向其他对等节点复制服务的时间间隔 ms |

进入自我保护模式后，Eureka 服务器后台将出现警告提示：

> [!failure] EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.

# 高可用服务集群

![[../../../../../_resources/images/服务治理 Eureka 2024-10-02 12.10.10.excalidraw]]

- 设定服务实例名称：`eureka.instance.hostname`
- 开启客户端注册与发现：`eureka.client.register-with-eureka`，`eureka.client.fetch-registry`
- 设定其他服务器地址：`eureka.client.service-url.defaultZone`

>[!attention] Eureka 服务器不支持同步信息的二次传播，因此 `service-url` 应列出所有可用服务器

```reference
file: "@/_resources/codes/spring-cloud/shopping-service-discovery/src/main/resources/application-sdpeer0.properties"
start: 4
end: 7
```

> [!warning] 使用 IP 代替主机名通信
> 由于开发环境可能没有 DNS，Docker 随机生成主机名等原因，可以使用 IP 代替主机名注册微服务
>  - 使用 IP 注册：`eureka.instance.prefer-ip-address=true`
>  - 使用指定 IP： `eureka.instance.ip-address=<IP 地址>`，用于多网卡选择

# 访问安全

使用 Spring Security 认证，依赖 `org.springframework.boot:spring-boot-starter-security`

> [!attention] 多个 Eureka 服务器都有安全配置时，只会统一配置第一个

>[!error] 服务器需要关闭 CSRF 防御机制

```reference
file: "@/_resources/codes/spring-cloud/shopping-service-discovery/src/main/resources/application.properties"
start: 13
end: 14
```

```reference hl:19,22
file: "@/_resources/codes/spring-cloud/shopping-service-discovery/src/main/java/com/example/shopping/config/SecurityConfig.java"
start: 10
```

在客户端 `eureka.client.service-url.defaultZone` 提供安全认证：

`eureka.client.service-url.defaultZone=http://<username>:<password>@<hostname>:<port>/eureka`

```reference
file: "@/_resources/codes/spring-cloud/shopping-user-service-before4.7/src/main/resources/application.properties"
start: 8
end: 8
```
