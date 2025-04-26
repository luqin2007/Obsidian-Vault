> [!note] 依赖（Spring）：`org.springframework.boot:spring-boot-starter-activemq`

> [!note] 依赖（ActiveMQ）：`org.apache.activemq:activemq-all`，若产生 Log 冲突可以单独导入 `broker`，`client`，`spring`，`kahadb-store` 四个包

````tabs
tab: Java

```embed-java
PATH: "vault://_resources/codes/spring/jms-activemq/src/main/java/com/example/mybank/MyBankConfig.java"
LINES: "14-42"
```

tab: XML

```embed-xml
PATH: "vault://_resources/codes/spring/jms-activemq-xml/src/main/resources/applicationContext.xml"
LINES: "4-24"
```
````

1. `amq:broker` 标签用于声明一个 ActiveMQ 代理
2. 创建 JMS ConnectionFactory 实例，用于创建与 ActiveMQ 的连接
    * `broker` 指定 ActiveMQ 代理 URL，`vm:localhost` 指定当前 VM 虚拟机。
    * `amq:trustedPackages` 定义了 ActiveMQ 信任包，即 ActiveMQ 仅能交换信任包中的对象。由于 `BankAccountDetails` 用到了 `java.util.Date` 类，因此还要信任 `java.util` 包。
3. `CachingConnectionFactory` 是一个 JMS ConnectionFactory 的适配器，提供缓存 JMS Session，MessageProducer 和 MessageConsumer
