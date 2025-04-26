首先，需要定义 JMS 通道。`amq:queue` 标签创建一个 JMS 队列，`physicalName` 表示在 ActiveMQ 中的名称。当然，也可以[[动态 JMS 队列|在运行时创建队列]]。

> [!attention] `id` 是队列对应 `ActiveMQQueue` bean 对象的 `id`，`physicalName` 才是真正 JMS 队列的名称。后面用到的基本都是 `physicalName`

````tabs
tab: Java

```embed-java
PATH: "vault://_resources/codes/spring/jms-activemq-send/src/main/java/com/example/mybank/config/JMSConfig.java"
LINES: "47-55"
```

tab: XML

```embed-xml
PATH: "vault://_resources/codes/spring/jms-activemq-send-xml/src/main/resources/applicationContext.xml"
LINES: "26-27"
```
````

`JmsTemplate` 和 `JmsTransactionManager` 是一个用于信息的事务管理器，`defaultDestination` 为默认的 JMS 队列

````tabs
tab: Java

```embed-java
PATH: "vault://_resources/codes/spring/jms-activemq-send/src/main/java/com/example/mybank/config/JMSConfig.java"
LINES: "57-68"
```

tab: XML

```embed-xml
PATH: "vault://_resources/codes/spring/jms-activemq-send-xml/src/main/resources/applicationContext.xml"
LINES: "29-36"
```
````

Spring `JmsTemplate` 简化同步和发送 JMS 信息的方式，提供一个统一抽象层，不需要处理底层 API

* 使用 `send` 方法手动构建 `Message` 对象
* 使用 `convertAndSend` 使用[[内置消息类型转换]]将 `String`、`byte[]`、`Map` 对象转换为 `Message` 对象

```reference
file: "@/_resources/codes/spring/jms-activemq-send/src/main/java/com/example/mybank/service/FixedDepositService.java"
start: 15
end: 38
```

`createFixedDeposit` 指定事务管理器为 `jmsTxManager`。通过 `JmsTemplate` 向 `emailQueueDestination` 和 `aQueueDestination` 两个队列发送消息，不需要显式创建 `Connection` 和 `Session`。

如果需要使用编程式事务管理，可以使用 `TransactionTemplate`，并将 `transactionManager` 属性设置为 `jmsTxManager`

```reference
file: "@/_resources/codes/spring/jms-activemq-send-xml/src/main/resources/applicationContext.xml"
start: 38
end: 47
```

```reference
file: "@/_resources/codes/spring/jms-activemq-send-xml/src/main/java/com/example/mybank/service/FixedDepositService.java"
start: 13
end: 43
```
