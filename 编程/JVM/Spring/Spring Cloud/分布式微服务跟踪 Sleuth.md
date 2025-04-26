`Spring Cloud Sleuth` 子项目提供微服务之间调用的链路追踪，通过全局 ID 串联各个微服务节点的请求
- 服务追踪：为同一个用户的请求添加相同的 TraceID，用于识别完整链路
- 服务监控：为每个微服务处理生成独立的 SpanID，记录请求到达和离开时间

`````col
````col-md
flexGrow=1
===
![[../../../../_resources/images/分布式微服务跟踪 Sleuth 2024-10-06 13.35.24.excalidraw|70%]]
````
````col-md
flexGrow=1
===
> [!note] 相关术语
> - Span：Sleuth 基本工作单元，一个请求，一般成对出现，可携带其他数据
> - Trace：一次用户请求涉及的 Span 集合 
> - Annotation：记录时间信息
> 	- cs：Client Send，客户端发送，Span 起点
> 	- sr：Server Received，服务端接收
> 	- ss：Server Send，服务端完成
> 	- cr：Client Received，客户端接收
````
`````

只需要在项目中添加 `org.springframework.cloud:spring-cloud-starter-sleuth` 依赖即可启用

> [!missing] Spring Cloud 2022.0 开始，使用 Micrometer Tracing 替代 Spring Cloud Sleuth，该项目实质是 Sleuth 跟踪部分的一个分支

```cardlink
url: https://micrometer.io/
title: "Micrometer Application Observability"
description: "Application observability facade for the most popular observability tools. Think SLF4J, but for observability."
host: micrometer.io
favicon: https://micrometer.io/favicon-32x32.png?v=4c58a9ad30498f838fae3e07ebd4ea42
image: https://micrometer.io/img/og-micrometer.png
```

# Zipkin

`Zipkin` 可以可视化形式呈现外部请求跨多个微服务之间的服务跟踪数据和耗时

```cardlink
url: https://zipkin.io/
title: "OpenZipkin · A distributed tracing system"
host: zipkin.io
```

> [!note] 推荐 **Jaeger**

Zipkin 需要 Java17+ 启动，下载 `zipkin-server` 后使用 `java -jar` 运行即可，其默认 HTTP 端口为 9411

# Micrometer Tracing

添加依赖：
- `org.springframework.boot:spring-boot-starter-actuator`
- `io.micrometer:micrometer-tracing`
Zipkin 桥接：
- `io.micrometer:micrometer-tracing-bridge-brave`
- `io.micrometer:micrometer-observation`
- `io.zipkin.reporter2:zipkin-reporter-brave`
Feign 支持：
- `io.github.openfeign:feign-micrometer`

配置：

```reference
file: "@/_resources/codes/spring-cloud/config-repo-files/application.properties"
start: 25
end: 29
```

之后即可在 `http://127.0.0.1:9411/zipkin` 中看到请求记录

![[../../../../_resources/images/Pasted image 20241010013136.png]]