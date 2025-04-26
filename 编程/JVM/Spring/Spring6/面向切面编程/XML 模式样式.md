```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans ...
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="...
                           http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="logAspect" class="com.example.mybank.aspects.LoggingAspect" />
  
    <aop:config proxy-target-class="false" expose-proxy="true">
        <aop:pointcut id="services" expression="execution(* com.example.mybank.service.*Service.*(..))"/>
        <aop:aspect id="logAspect" ref="logAspect">
            <aop:after method="log" pointcut="execution(* com.example.mybank.service.*Service.*(..))"/>
            <aop:around method="logInvokeTime" pointcut="execution(* com.example.mybank.service.*Service.*(..))"/>
        </aop:aspect>
    </aop:config>
</beans>
```

使用 XML 与使用注解类似，只需要使用 `aop` 模式即可方便的创建切面。

* `@Pointcut` 注解直接属于 `<aop:config>` 标签
* 每个 `<aop:aspect>` 标签配置一个包含 `Aspect` 方法的 bean 类
