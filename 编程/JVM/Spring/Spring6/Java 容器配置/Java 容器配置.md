使用 Java 注解替代 XML 配置容器，核心为 `@Configuration` 和 `@Bean` 注解。

```reference
file: "@/_resources/codes/spring/containerconfiguration/src/main/java/com/example/mybank/BankAppConfiguration.java"
start: 8
end: 15
```

配置好的容器通过 `AnnotationConfigApplicationContext` 创建。
* 将所有被 `@Configuration` 注解的类传入 `AnnotationConfigApplicationContext`
    * `@Component` 或 `@Named` 注解的类使用了 `@Bean`，则也应作为容器配置类传入
* 可先创建 `AnnotationConfigApplicationContext` 再注入容器配置类，通过 `refresh()` 刷新容器
    * 通过 `scan` 传入包名，由 Spring 扫描容器配置类
    * 通过 `register` 传入容器配置类
* 作为容器配置在构造函数传入的类也被初始化为 `bean`，因此可以通过 `getBean` 获取

```reference
file: "@/_resources/codes/spring/containerconfiguration/src/main/java/com/example/mybank/MainApplication.java"
start: 6
end: 13
```

> 或者使用 `SpringApplication.run(class)`，配置位于 `@SpringBootApplication` 中
> 
> ```java
> @SpringBootApplication(scanBasePackages = "com.example.mybank")
> public class MainApplication {
> 
>     public static void main(String[] args) {
>         ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class);
>     }
> }
> ```

`@Configuration` 声明了一个容器配置，相当于 `<beans>` 标签。该注解依赖于 GCLIB 库，由 GCLIB 负责子类化，因此不能将其声明为 `final` 的。

> [!caution] `@Configuration` 也受 `@Profile` 注解的影响

`<beans>` 的其他子标签通过对应的元素在其中配置：
* `<bean>`：通过 `@Bean` 修饰[[注入 Bean|注入方法]]
* `<component-scan>`：使用[[路径扫描与组件索引]]
* `<import>`：[[导入 Java 容器配置]]
* `<util:properties>`：泛指导入 `properties` 配置文件的方式，使用 [[@PropertySource]] 注解