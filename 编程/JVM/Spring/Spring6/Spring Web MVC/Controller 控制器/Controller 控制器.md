`Controller` 控制器负责处理来自外部的请求。处理方法有两种：

* 实现 `org.springframework.web.servlet.mvc.Controller` 接口，`HttpServletRequest` 即请求对象，向 `HttpServletResponse` 输出内容
* 使用 `@RequestMapping` 注解指定不同请求条件的方法，并返回模型和数据等由 Spring 处理

在 `Controller` 控制器中常用的注解有：

| 注解                  | 注解对象               | 对应 XML 配置                                  |
| ------------------- | ------------------ | ------------------------------------------ |
| `@RequestMapping`   | `Controller` 实现类   | `SimpleUrlHandlerMapping` 中 `urlMap` 配置的路径 |
| [[@RequestMapping/@RequestMapping]] | `Controller` 中的方法  | 注解映射的子路径，支持通配符                             |
| `@RequestParamaram` | `Controller` 中方法参数 | 指定查询参数                                     |

- [[@ExceptionHandler]]
- [[处理拦截器]]
- [[国际化]]
