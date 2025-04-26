Eureka 服务器本身会持续对微服务进行监控，判断服务实例是否可用。Spring 也提供 Actuator 子项目集成对应用系统配置、运行状态、相关功能统计的监控，并通过 REST 暴露数据

| 端点             | 说明                             |
| -------------- | ------------------------------ |
| `/autoconfig`  | Spring Boot 自动配置信息报告           |
| `/beans`       | Spring Boot 自动创建的对象            |
| `/configprops` | 应用配置属性报告                       |
| `/env`         | 环境变量、JVM 属性、应用配置、命令行参数等        |
| `/health`      | 健康状态报告，如是否启动、磁盘空间等             |
| `/metrics`     | 应用及宿主机性能指标，如 CPU、内存、线程信息、垃圾回收等 |
| `/mappings`    | Spring MVC 端点返回的控制器映射关系        |
| `/info`        | 自定义属性配置                        |
| `/shutdown`    | 该端口用于关闭应用                      |

使用 `endpoints.<端点>.enabled=false` 可以禁用某个端点

# 参考

