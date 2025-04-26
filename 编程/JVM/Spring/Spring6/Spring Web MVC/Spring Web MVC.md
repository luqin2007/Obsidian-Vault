> [!note] MVC：Model-View-Controller

Spring Web MVC 框架是一个非侵入式 MVC 框架。

[[Controller 控制器/Controller 控制器|Spring 控制器]]负责根据请求处理逻辑，可以实现 `Controller` 接口或 `@Controller` 注解，其中控制函数返回 `ModelAndView` 对象：
* 模型数据：向用户显示数据，通常为一个 Map 作为 JSP 模板的各个值。
* 逻辑名称：展示模型数据的页面

![[../../../../../_resources/images/Spring Web MVC 2024-09-10 00.36.41.excalidraw|50%]]

> [!hint] 使用 `pageContext` 可以获取当前请求的目录，可用于组装链接：
`<form name="fixedDepositList" method="post" action="${pageContext.request.contextPath}/fixedDeposit/list}">`

> [!error] 新版本 Spring 不再支持 jsp，使用 Thymeleaf 替代
> 依赖：`org.springframework.boot:spring-boot-starter-thymeleaf`
> 配置：
> ```properties title:application.properties
> spring.thymeleaf.prefix=classpath:/templates/
> spring.thymeleaf.suffix=.html
> spring.thymeleaf.mode=HTML5
> spring.thymeleaf.encoding=UTF-8
> spring.thymeleaf.cache=true
> ```

`HandlerMapping` 负责将 HTTP 请求映射到控制器。

````tabs
tab: XML

`SimpleUrlHandlerMapping` 子类将 URL 请求转发给指定控制器

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean name="helloWorldController" class="com.example.mybank.web.HelloWorldController" />

    <bean id="handlerMapping" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
        <property name="urlMap">
            <map>
                <entry key="/sayHello" value-ref="helloWorldController" />
            </map>
        </property>
    </bean>
</beans>
```

tab: Java

注解配置只需在对应控制器上使用 `@RequestMapping` 配置即可 

```embed-java
PATH: "vault://_resources/codes/spring-web/web-helloworld/src/main/java/com/example/mybank/controller/HelloWorldController.java"
LINES: "10-12"
```
````

`ViewResolver` 类负责根据 `ModelAndView` 中包含的视图名称查找实际视图
- `InternalResourceViewResolver` 可以指定前缀和后缀
- `ThymeleafViewResolver` 用于 `thymeleaf`

![[../../../../../_resources/images/Spring Web MVC 2024-09-10 02.13.42.excalidraw|50%]]

`web.xml` 为 Web 应用部署描述，该文档根标签为 `<web-app>`。使用 SpringBoot 时不需要该配置
-  `<servlet>` 标签声明了一个 Servlet，这里使用 [[DispatcherServlet]]，请求由该 Servlet 截取并发送到相应服务器。

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">

    <servlet>
        <servlet-name>hello</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/spring/myapp-config.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>hello</servlet-name>
        <url-pattern>/helloworld/*</url-pattern>
    </servlet-mapping>
</web-app>
```

Web 应用程序共享 `<web-app>` 配置的对象。可以将程序的 XML 配置在此处通过 `ContextLoaderListener` 加载

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">

    ...

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath*:/META-INF/spring/applicationContext.xml</param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
</web-app>
```

> [!note] `<mvc:annotation-driven />` 标签对应 `@EnableWebMvc` 注解

---

- [[Controller 控制器/Controller 控制器|Controller 控制器]]
- [[验证与数据绑定/验证与数据绑定|验证与数据绑定]]
- [[RESTful Web 服务/RESTful Web 服务|RESTful Web 服务]]
- [[类型转换与格式化]]
- [[文件上传]]
