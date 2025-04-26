> [!note] 服务：可独立运行、提供有限范围功能（业务功能或非业务功能）的组件

> [!warning] 微服务引入了更加复杂的应用结构，应当是在业务复杂，难以理解，尤其是数据过多，且没有合适的关联时拆分

`````col
````col-md
flexGrow=1
===
# 优点
- 松耦合，服务之间使用接口和协议通信
- 足够小，快速开发、调试、上线
- 抽象，服务对自己的数据有绝对控制权，可有效控制
- 独立，每个服务可以独立的编译、打包、部署
- 多样性，为不用客户特殊需求提供不同微服务，快速部署
- 可用性和弹性，去中心化应用，每个服务随时上下线，其他同类行微服务可继续提供服务
````
````col-md
flexGrow=1
===
# 缺点
- 可用性降低，微服务之间使用远程服务协作，远程服务可靠性较低，一组微服务不可用时易产生雪崩效应
- 不易于分布式事务，涉及到多个分布式微服务时一致性难以保证，传统两阶段提交解决方案难以实现
- 全能对象难以处理
- 难度曲线加大，增加了微服务开发技术
- 组织架构变更，对于某个微服务部署简化了，但整个应用的部署更复杂了
````
`````

`````col
````col-md
flexGrow=1
===
# 微服务拆分原则

- 单一职责原则 SRP：微服务保持足够小，仅拥有一个业务职责

> [!warning] 微服务颗粒度也不能太细，不应存在一个服务被大量服务所依赖，又依赖其他大量服务的情况

- 共同封闭原则 CCP：当修改某业务时，修改范围仅在同一个服务内
````
````col-md
flexGrow=1
===
# 不适用于微服务的情况

- 难以构建分布式架构
- 服务器蔓延时
- 小型应用、快速产品原型
- 对数据事务一致性有一定要求
````
`````

>[!warning] `application.properties` 与 `bootstrap.properties`
>- `application.properties`：常规 SpringBoot 配置，如应用名数据库、接口等
>- `bootstrap.properties`：加载时间更早，优先级更高，常用于 Spring Cloud 的配置

> [!error] Spring boot 2.4 后，`bootstrap.properties` 不再自动加载，需要依赖 `spring-cloud-starter-bootstrap` 并通过参数或环境变量将 `spring.cloud.bootstrap.enabled` 设置为 `true`

---

- [[Spring Cloud 子项目]]
- [[hello world]]
- [[服务治理与负载均衡/服务治理与负载均衡|服务治理与负载均衡]]：如何在服务实例不断上下线的情况下，正常向消费者提供服务
- [[微服务容错 Resilience4j]]
- [[服务网关 Gateway]]：API 服务网关为微服务提供统一入口
- [[统一配置中心 Config]]：集中管理微服务配置
- [[分布式微服务跟踪 Sleuth]]：将分散在多个日志的调用串联成完整请求链，解决调试和跟踪分析问题
- [[消息驱动 Stream]]
- [[微服务应用安全 Security]]

# 其他子项目

| 组件                       | 功能                             |
| ------------------------ | ------------------------------ |
| `Spring Cloud Cli`       | 使用命令行管理微服务                     |
| `Spring Cloud Data Flow` | 基于原生云，用于开发、执行大数据处理的统一编程模型和托管服务 |
| `Spring Cloud Task`      | 用于短时间的任务管理和调度微服务管理             |
| `Spring Cloud Contract`  | 契约框架，用于微服务测试                   |

# 参考

```cardlink
url: https://read.douban.com/ebook/55385581/
title: "Spring Cloud微服务架构开发实战"
description: "本书首先从微服务架构兴起的背景讲起，探讨了为何在分布式系统开发中微服务架构将逐渐取代单体架构，然后对SpringCloud所提供的微服务组件及解决方案进行了一一讲解，从而让读者不但可以系统地学习SpringCloud的相关知识，而且还可以全面掌握微服务架构应用的设计、开发、部署和运维等知识。本书共11章，分为3篇。1篇为微服务开发基础——SpringBoot框架及使用；2篇为SpringClou…"
host: read.douban.com
image: https://pic.arkread.com/cover/ebook/f/55385581.1653702212.jpg!cover_default.jpg
```

```cardlink
url: https://spring.io/projects/spring-cloud
title: "Spring Cloud"
description: "Level up your Java code and explore what Spring can do for you."
host: spring.io
favicon: https://spring.io/favicon-32x32.png?v=96334d577af708644f6f0495dd1c7bc8
image: https://spring.io/img/og-spring.png
```
