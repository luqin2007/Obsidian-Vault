
`````col
````col-md
flexGrow=1
===
`Spring Cloud Stream` 子项目提供消息收发等功能，可方便的与 `Kafka`、`RabbitMQ` 等中间件集成

`Spring Cloud Bus` 子项目对 Stream 项目进行扩展，可作为消息总线，用于服务器集群中状态的传播。

当微服务 A 要发送数据给微服务 B 时，通过消息中间件负责网络处理。若 B 不在线，A 可以将消息存储于数据库，当 B 上线时再发。
- 消息发送接口 Source：用于 Spring Cloud Stream 与外界通道绑定，序列化待传送对象
- 消息通道 Channel：对消息队列的抽象
- 消息绑定器 Binder：中间层，实现应用程序与具体消息中间件细节的隔离
- 监听接口 Sink：类似消息发送接口 Source，负责反序列化 Java 对象
````
````col-md
flexGrow=1
===
![[../../../../_resources/images/消息驱动 Stream 2024-10-07 00.06.27.excalidraw]]
````
`````
# Spring Cloud Stream 结构

Spring Cloud Stream 使用订阅者模式

# 添加消息功能

## Redis 缓存数据库

> 安装详见 [[../../../../工具/数据库/Redis|Redis]]

Spring 依赖：`org.springframework.boot:spring-boot-starter-data-redis`（在 NoSQL 分类中），内置驱动和 Lettuce 客户端，配置后可直接使用 `RedisTemplate` 读写数据库

```reference
file: "@/_resources/codes/spring-cloud/config-repo-files/productservice.properties"
start: 10
end: 14
```

配置中使用 `RedisTemplate` 访问

```reference
file: "@/_resources/codes/spring-cloud/shopping-product-service-before9.4/src/main/java/com/example/shopping/config/RedisConfiguration.java"
start: 14
end: 24
```

使用时加一层 Redis 的 Repository 即可

`````col
````col-md
flexGrow=1
===
```embed-java
PATH: "vault://_resources/codes/spring-cloud/shopping-product-service-before9.4/src/main/java/com/example/shopping/repository/UserRedisRepository.java"
LINES: "22-26,31-33,38-43,48-50"
```
````
````col-md
flexGrow=1
===
```embed-java
PATH: "vault://_resources/codes/spring-cloud/shopping-product-service-before9.4/src/main/java/com/example/shopping/service/UserService.java"
LINES: "20-24,32-40,43,45-47,50,52-56"
```
````
`````

## Kafka 消息系统

Spring Cloud Stream 只是一个消息系统的接口，实际还需要一个消息系统实现，支持 Kafka、RabbitMQ 等工具

Apache Kafka 是一个分布式发布-订阅消息系统，使用 Scala 编写

```cardlink
url: https://kafka.apache.org/downloads
title: "Apache Kafka"
description: "Apache Kafka: A Distributed Streaming Platform."
host: kafka.apache.org
favicon: https://kafka.apache.org/images/apache_feather.gif
image: http://apache-kafka.org/images/apache-kafka.png
```

- 主题：Topic，一个消息类别。物理上不同消息分别储存，逻辑上相同消息也可能在多个代理 Broker 中
- 生产者：Producer，消息发布者
- 消费者：Consumer，从消息代理 Broker 获取并处理消息。
	- 消费者组：Consumer Group，消息会随机发送到同一消费者组里的一个消费者
- 消息代理：Broker，Kafka 服务器集群中的一个节点，消费者从代理中获取消息
- 消息分区：Partition，主题消息数据分割为多个分区，每个分区数据分割为多个 Segment。
	- 每个分区中消息是有序地，但多个分区之间是无序的 - 若要保证所有消息有序应指定固定分区 1
	- 偏移量：offset，消息在分区的唯一编号

```shell
# 启动内嵌 ZooKeeper 服务器
.\bin\windows\zookeeper-server-start .\config\zookeeper.properties
# 启动 Kafka 服务器
.\bin\windows\kafka-server-start .\config\server.properties
```

测试（发送端和接收端分别是两个命令行窗口），此时在发送端发送的数据，接收端应该可以看到。

```shell
# 创建 Topic
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic springcloud-msg
# 发送端
.\bin\windows\kafka-console-producer.bat --bootstrap-server localhost:9092 --topic springcloud-msg
# 接收端
.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic springcloud-msg --from-beginning
```

把 Kafka 整合到 Spring Boot 项目中，先添加 `org.springframework.cloud:spring-cloud-starter-stream-kafka` 依赖即可

## 声明消息通道

配置 Kafka 参数

```reference
file: "@/_resources/codes/spring-cloud/hello-stream-receiver/src/main/resources/application.properties"
start: 5
end: 7
```

``````tabs
tab: 消息发送端

- 配置：`spring.cloud.stream.bindings.<绑定名>-out[-<分区>]` 表示通过某通道（某分区）发送的数据配置
	- **绑定名**用于后面发送数据
	- `destination` 属性指定通道名

```embed-properties
PATH: "vault://_resources/codes/spring-cloud/hello-stream-sender/src/main/resources/application.properties"
LINES: "8-9"
```

- 发送：通过自动装配 `StreamBridge` 的 `send(绑定名, 数据)` 发送

```embed-java
PATH: "vault://_resources/codes/spring-cloud/hello-stream-sender/src/main/java/com/example/hellostreamsender/SenderController.java"
LINES: "12-19"
```

> 还可以通过其他方式发送数据：
> - 注入一个名为 `绑定名` 的 Supplier 对象，在其他对象中获取该对象并调用
> - 注入一个 `KafkaTemplate` 类型的对象，通过该对象发送数据
> - 使用 Kafka 原生 `KafkaProducer` 类发送，该方法不依赖于 Spring Cloud Stream

tab: 消息接收端

- 配置：`spring.cloud.stream.bindings.<绑定名>-in[-<分区>]` 表示通过某通道（某分区）接收数据
	- **绑定名**用于后面接收数据
	- `destination` 属性指定通道名

```embed-properties
PATH: "vault://_resources/codes/spring-cloud/hello-stream-receiver/src/main/resources/application.properties"
LINES: "8-10"
```

- 接收：使用 `@EnableKafka` 开启 Kafka 监听，使用 `@KafkaListener(topics, groupId)` 注解消息处理函数

`````col
````col-md
flexGrow=1
===
```embed-java
PATH: "vault://_resources/codes/spring-cloud/hello-stream-receiver/src/main/java/com/example/hellostreamreceiver/HelloStreamReceiver.java"
LINES: "7-9"
```
````
````col-md
flexGrow=1
===
```embed-java
PATH: "vault://_resources/codes/spring-cloud/hello-stream-receiver/src/main/java/com/example/hellostreamreceiver/ReceiverController.java"
LINES: "17-21"
```
````
`````

> 其他接收数据方式：
> - 使用 Kafka 本身的 `KafkaConsumer` API
> - 使用 `@EnableKafkaStreams` 通过 Kafka Stream API 处理复杂数据
``````

## 错误处理

出现异常时，Spring 会将异常信息封装为 `ErrorMessage` 并通过 `spring.cloud.stream.bindings.error.destination` 指定的通道发送，默认为 `errorChannel`

## 消息分区

消费者组：多个消费者实例共享一个 ID，组内所有成员共同处理一个通道的消息，一条消息会被一个组中的一个实例处理

再平衡：rebalance，规定一个消费者组下的成员如何分配订阅的消息通道的消息分区

数据分片：一个主题可分为多个分片，并保证具有相同特征的数据被同一消费者处理

## 消息绑定器

Spring Cloud Stream 提供抽象绑定器作为中间层，实现同时与多个消息中间件（Kafka，RabbitMQ 等）连接，使应用代码与中间件解耦。

- 通常引入对应中间件的依赖即可，也可以自定义绑定器，需要实现 Binder 接口，并在 `META-INF/spring.binders` 中声明
- 默认绑定器使用 `spring.cloud.stream.bindings.[input/output].binder` 配置
- 绑定器设置使用 x `spring.cloud.stream.binders.<绑定器名>` 配置
- 具体某个通道的绑定器使用 `spring.cloud.stream.bindings.<name>.binder=<绑定器名>` 配置

# 消息总线 Bus

基于 Spring Cloud Stream + Spring 事件模型的消息库
- 事件：ApplicationEvent
- 事件监听器：ApplicationListener
- 事件发布者：ApplicationEventPublisher

引入依赖 `org.springframework.cloud:spring-cloud-starter-bus-kafka`，按照前面的方法配置好 Kafka 参数即可完成 Spring Cloud Bus 的引入

> [!note] 针对 RabbitMQ 的依赖为 `spring-cloud-starter-bus-amqp`，通过 `string.rabbitmq` 配置

## 自动更新配置

在微服务子项目和配置服务器都引入 Spring Cloud Bus，在配置服务器修改配置文件后，通过 `/bus/refresh` 即可刷新配置
- `/bus/refresh`：刷新所有配置
- `/bus/refresh?destination=<service>`：更新某微服务
- `/bus/refresh?destination=<service>:<port>`：更新某端口上的某微服务
- 支持 `**` 通配符

## 自定义事件

- 事件类：创建继承自 `RemoteApplicationEvent` 的类
	- 发布者和接收者都应可访问该类，或有一致类
	- 发送时使用 Json 序列化，可通过 `@JsonTypeName` 手动指定类名
- 监听器：注入一个实现 `ApplicationListener<T>` 的类
- 发布事件：通过 `ApplicationContext#pushEvent` 发送事件，使用 `@RemoteApplicationEventScan` 指定事件类包