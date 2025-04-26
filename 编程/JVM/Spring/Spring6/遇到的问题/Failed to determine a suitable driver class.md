> [!error] Failed to determine a suitable driver class

```
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dataSource' defined in class path resource [org/springframework/boot/autoconfigure/jdbc/DataSourceConfigurationHikari.class]: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Failed to determine a suitable driver class
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:651) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:639) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1335) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1165) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambdadoGetBean0(AbstractBeanFactory.java:325) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:975) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:959) ~[spring-context-6.1.4.jar:6.1.4]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:624) ~[spring-context-6.1.4.jar:6.1.4]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754) ~[spring-boot-3.2.3.jar:3.2.3]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:456) ~[spring-boot-3.2.3.jar:3.2.3]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:334) ~[spring-boot-3.2.3.jar:3.2.3]
	at org.springframework.boot.test.context.SpringBootContextLoader.lambdaloadContext3(SpringBootContextLoader.java:137) ~[spring-boot-test-3.2.3.jar:3.2.3]
	at org.springframework.util.function.ThrowingSupplier.get(ThrowingSupplier.java:58) ~[spring-core-6.1.4.jar:6.1.4]
	at org.springframework.util.function.ThrowingSupplier.get(ThrowingSupplier.java:46) ~[spring-core-6.1.4.jar:6.1.4]
	at org.springframework.boot.SpringApplication.withHook(SpringApplication.java:1454) ~[spring-boot-3.2.3.jar:3.2.3]
	at org.springframework.boot.test.context.SpringBootContextLoaderContextLoaderHook.run(SpringBootContextLoader.java:553) ~[spring-boot-test-3.2.3.jar:3.2.3]
	at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:137) ~[spring-boot-test-3.2.3.jar:3.2.3]
	at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:108) ~[spring-boot-test-3.2.3.jar:3.2.3]
	at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:225) ~[spring-test-6.1.4.jar:6.1.4]
	at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:152) ~[spring-test-6.1.4.jar:6.1.4]
	... 86 common frames omitted
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Failed to determine a suitable driver class
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:177) ~[spring-beans-6.1.4.jar:6.1.4]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:647) ~[spring-beans-6.1.4.jar:6.1.4]
	... 110 common frames omitted
Caused by: org.springframework.boot.autoconfigure.jdbc.DataSourcePropertiesDataSourceBeanCreationException: Failed to determine a suitable driver class
	at org.springframework.boot.autoconfigure.jdbc.DataSourceProperties.determineDriverClassName(DataSourceProperties.java:186) ~[spring-boot-autoconfigure-3.2.3.jar:3.2.3]
	at org.springframework.boot.autoconfigure.jdbc.PropertiesJdbcConnectionDetails.getDriverClassName(PropertiesJdbcConnectionDetails.java:49) ~[spring-boot-autoconfigure-3.2.3.jar:3.2.3]
	at org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration.createDataSource(DataSourceConfiguration.java:55) ~[spring-boot-autoconfigure-3.2.3.jar:3.2.3]
	at org.springframework.boot.autoconfigure.jdbc.DataSourceConfigurationHikari.dataSource(DataSourceConfiguration.java:117) ~[spring-boot-autoconfigure-3.2.3.jar:3.2.3]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:140) ~[spring-beans-6.1.4.jar:6.1.4]
	... 111 common frames omitted
```

原因：引入了 JDBC 相关 Spring 库（如 `spring-boot-starter-data-jdbc`）但没有配置数据库

解决方式：配置一个数据库即可。

```properties
#resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&useSSL=false
spring.datasource.username=root
spring.datasource.password=lq2007
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

‍
