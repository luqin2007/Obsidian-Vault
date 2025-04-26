通过设置 `JmsTemplate` 的 `pubSubDomain` 为 `true`，允许在运行时创建 `JMS` 队列。

```xml
<bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
    <...>
    <property name="pubSubDomain" value="true" />
</bean>
```

之后，通过 `session.createTopic` 创建

* `pubSubDomain=true`：`DynamicDestinationResolver` 将其解析为运行时由 `createTopic` 创建的 JMS 主题
* `pubSubDomain=false`：`DynamicDestinationResolver` 将其解析为 JMS 的队列

```java
jmsTemplate.send("dynamicTopic", session -> {
    session.createTopic("dynamicTopic");
    ObjectMessage objectMessage = session.createObjectMessage();
    // do something
    return objectMessage;
});
```
