Spring  Cloud  的 Hello world 示例项目分为三个子项目，运行时先运行 `discovery`，再运行 `provider`，最后运行 `consumer`

#  `helloworld-service-discovery`

`hello` 微服务管理服务
- `@EnableEurekaServer` 开启 `Eureka` 服务器
- 由于不需要 `SpringMVC`，需要指定为 Web Sevlet 应用

```reference hl:12,16-18
file: "@/_resources/codes/spring-cloud/helloworld-service-discovery/src/main/java/com/example/helloworldservicediscovery/HelloworldServiceDiscoveryApplication.java"
start: 11
end: 21
```

`````col
````col-md
flexGrow=1
===
- 依赖：`eureka-server`，不需要 `spring-web`

```embed-xml
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-discovery/pom.xml"
LINES: "45-53"
```
````
````col-md
flexGrow=1
===
- 配置

```embed-properties
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-discovery/src/main/resources/application.properties"
LINES: "1,9-17"
```
````
`````

#  `helloworld-service-provider`

提供 `hello` 服务的实际服务器，**项目名即服务名**

`````col
````col-md
flexGrow=1
===
- `@EnableDiscoveryClient` 开启 `Eureka` 客户端

```embed-java
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-provider/src/main/java/com/example/helloworldserviceprovider/HelloworldServiceProviderApplication.java"
LINES: "10-15"
```
````
````col-md
flexGrow=1
===
- 使用 Spring Web 提供服务

```embed-java
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-provider/src/main/java/com/example/helloworldserviceprovider/api/HelloProviderEndpoint.java"
LINES: "11-16"
```
````
`````

`````col
````col-md
flexGrow=1
===
- 依赖：`eureka-client`，使用普通 `spring-web` 提供 RESTful 服务

```embed-xml
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-provider/pom.xml"
LINES: "45-58"
```
````
````col-md
flexGrow=1
===
- 配置
  - `eureka.client.register-with-eureka` 开启服务注册
  - `eureka.client.service-url.defaultZone` 配置默认地址，为 `服务器地址/eureka`

```embed-properties
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-provider/src/main/resources/application.properties"
LINES: "1,10-16"
```
````
`````

#  `helloworld-service-consumer`

`hello` 服务的消费者

- `@EnableDiscoveryClient` 开启 `Eureka` 客户端
- `@EnableFeignClients` 开启 `Feign` 客户端

```embed-java
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-consumer/src/main/java/com/example/helloworldserviceconsumer/HelloworldServiceConsumerApplication.java"
LINES: "11-14"
```

`````col
````col-md
flexGrow=1
===
- 使用 `@FeignClient` 获取服务
  - `value`：服务名
  - `fallback`：回退服务

```embed-java
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-consumer/src/main/java/com/example/helloworldserviceconsumer/service/HelloService.java"
LINES: "8-13"
```
````
````col-md
flexGrow=1
===
降级服务：实现 `FallbackFactory<TService>`

```embed-java
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-consumer/src/main/java/com/example/helloworldserviceconsumer/service/HelloServiceFallback.java"
LINES: "5-12"
```
````
`````

`````col
````col-md
flexGrow=1
===
- 依赖：`eureka-client`
- 依赖：`openfeign`
- 依赖 熔断器：`resilience4j`

```embed-xml
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-consumer/pom.xml"
LINES: "50-66"
```
````
````col-md
flexGrow=1
===
- 配置
  - `eureka.client.fetch-registry` 获取服务表
  - `eureka.client.service-url.defaultZone` 配置默认地址，为 `服务器地址/eureka`
  - `spring.cloud.openfeign.circuitbreaker.enabled` 启用熔断器

```embed-properties
PATH: "vault://_resources/codes/spring-cloud/helloworld-service-provider/src/main/resources/application.properties"
LINES: "1,9-17"
```
````
`````
