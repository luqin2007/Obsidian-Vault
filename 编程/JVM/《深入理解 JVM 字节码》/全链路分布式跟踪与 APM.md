# 全链路分布式跟踪与 APM

> [!note] APM
> Application Performance Management，应用性能管理，对企业关键业务进行监控、诊断、分析、优化，提高可靠性和质量。

全链路分布式跟踪用于解决微服务出现问题时难以查找问题来源的问题。该技术起于 2005 Google 的 Dapper 设计与实现，每个请求有对应的 TraceID(一次用户请求，所有链路上子过程节点共用)，SpanID(一次处理过程)，ParentID(父节点) 完成对一次访问服务的追踪。

![[../../../../_resources/images/全链路分布式跟踪与 APM 2024-08-03 15.26.57.excalidraw]]

Dapper 促生了多个开源实现，后来出现了原生计算基金会（Cloud Native Computing Foundation，CNCF），创建了 OpenTracing 规范，定义了一套通用接口。

> [!note] Tracer：一次调用链，由多个 Span 组成

> [!note] Span：一段处理过程，可以是一个方法调用，一次 HTTP 请求，一次数据库操作等

| 状态          | 说明                                                      |
| ----------- | ------------------------------------------------------- |
| Span 名      | 一般是函数名或关键操作名                                            |
| SpanContext | 上下文，包括 TraceID，SpanID 等                                 |
| 时间戳         | 开始和结束的时间戳                                               |
| Span关系      | 父子关系<sup>（ChildOf）</sup>或顺序执行关系<sup>（FollowsFrom）</sup> |
| 附加信息        | 日志等                                                     |

> [!note] SpanContext：包含 TraceID，SpanID，ParentID 等信息，嵌套调用、跨进程调用时表示调用层级关系

> [!note] Sampling：采样率，确定信息收集的精度（等级），平衡分析数据和吞吐量、延迟的关系，包括全采样、RateLimit 采样、自适应采样等

以 Jaeger 为例，可以直接通过 Docker 镜像启动

```shell
docker run -d --name jaeger \
           -p 6831:6831/udp -p 16686:16686 -p 14268:14268 \
           jaegertracing/all-in-one:1.55
```
* `-d`：后台运行
* `-p`：对外开放端口，6831 为 UDP 数据上报端口，16686 为后台数据展示服务端口，14268 表示通过 HTTP 上报 Trace 数据的端口

OpenTracing 中每个程序初始化一个 Tracer 实例，核心参数为：
* `serviceName`：应用名
* `reporter`：调用 Span 后如何上传，常见有 HTTP，TCP，UDP
* `sampler`：采样器，可指定全采样、固定频率采样等

>[!note] 依赖：`io.jaegertracing:jaeger-client`

```java
private static Tracer tracer;

// 初始化 Tracer
RemoteReporter reporter = new RemoteReporter.Builder()
        .withSender(new UdpSender("127.0.0.1", 6831, 0))
        .build();
tracer = new JaegerTracer.Builder("my-service")
        .withReporter(reporter)
        .withSampler(new ConstSampler(true))
        .build();
GlobalTracer.register(tracer);
```

* 方法级跟踪实现：分布式跟踪的基本功能，通过对方法体内容增加 `try-finally` 块包裹，通过 `Span` 块分析。

  ```java
  public void doDbOperation(String username) {
      System.out.println("Save user " + username);
  }
  public void createUser(String username) {
      Span span = tracer.buildSpan("createUser").start();
      try {
          System.out.println("Save user " + username);
          doDbOperation(username);
      } finally {
          span.finish();
      }
  }
  ```

![[image-20240322005116-3rv3p4m.png]]
* 多函数调用链路跟踪实现：引入 `Scope` 类，将 `Span` 放入 `ThreadLocal` 中，进行下一级 `Span` 时自动将上一个 `SpanContext` 作为 `Parent`

  ```java
  public void doDbOperation(String username) {
      Span span = tracer.buildSpan("doDbOperation").start();
      try(Scope scope = tracer.scopeManager().activate(span)) {
          System.out.println("Save user " + username);
      } finally {
          span.finish();
      }
  }
  public void createUser(String username) {
      Span span = tracer.buildSpan("createUser").start();
      try(Scope scope = tracer.scopeManager().activate(span)) {
          System.out.println("Save user " + username);
          doDbOperation(username);
      } finally {
          span.finish();
      }
  }
  ```

![[image-20240322012054-44plipy.png]]
 ![[image-20240322012116-kyn3zmh.png]]
* 扩展进程调用链路跟踪：需要将 TraceID 与 SpanID 传递给下一层调用

````tabs
tab: 发送
```java
// 通过 X-B3-TraceId 和 X-B3-SpanId 传递
public void sendHttp() throws Exception {
    Tracer tracer = GlobalTracer.get();
    Span span = tracer.buildSpan("sendHttp").start();
    try(Scope ignore = tracer.scopeManager().activate(span)) {
        Request request = new Request.Builder()
                .url("...")
                .addHeader("X-B3-TraceId", span.context().toTraceId())
                .addHeader("X-B3-SpanId", span.context().toSpanId())
                .addHeader("X-B3-Sampled", "1")
                .get().build();
        // ...
    }
}
```

tab: 接收
```java
// 使用 extract 方法重建 SpanContext
@RequestMapping(...)
public void accept(@RequestHeader("X-B3-TraceId") Map<String, String> headers, HttpServletRequest request) {
    Tracer tracer = GlobalTracer.get();
    Tracer.SpanBuilder spanBuilder = tracer.buildSpan("HTTP Request");
    SpanContext context = tracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapAdapter(headers));
    if (context != null) {
        spanBuilder.asChildOf(context);
    }
    Span span = spanBuilder.start();
    try (Scope scope = tracer.scopeManager().activate(span)) {
        // ...
    } finally {
        span.finish();
    }
}
```
````

‍

‍
