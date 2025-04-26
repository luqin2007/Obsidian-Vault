既可以使用 `JmsTemplate` 同步接收 JMS 消息，也可以通过 Spring 消息侦听器容器进行异步接收。

# 同步接收

`JmsTemplate` 有各种 `receive` 方法，可以阻塞接收 JMS 消息。`setReceiveTimeout` 可以指定一个接收超时限制。

# 异步接收

使用 `<jms:listener-container>` 注册 `JmsListenerContainerFactory` 实例，指向一个 `MessageListener` 接口实现类 `bean` 对象，当收到对应消息时由 Spring 负责调用接收。

````tabs
tab: XML

```embed-xml
PATH: "vault://_resources/codes/spring/jms-activemq-receive-xml/src/main/resources/applicationContext.xml"
LINES: "51-57"
```

* 使用 `jms:listener-container` 注册 Container，使用 `jms:listener` 注册 Listener
* `jms:listener` 指向对象应实现 `jakarta.jms.MessageListener` 接口

tab: Java

通过 `@JMSConnectionFactory`，或使用 `@JmsListener` 的 `containerFactory` 属性设置 `JmsListenerContainerFactory`

```embed-java
PATH: "vault://_resources/codes/spring/jms-activemq-receive/src/main/java/com/example/mybank/config/JMSConfig.java"
LINES: "74-80"
```

```embed-java
PATH: "vault://_resources/codes/spring/jms-activemq-receive/src/main/java/com/example/mybank/jms/JmsListeners.java"
LINES: "8-28"
```
````
