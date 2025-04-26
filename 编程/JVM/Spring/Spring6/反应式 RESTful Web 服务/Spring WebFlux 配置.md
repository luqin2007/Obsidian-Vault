
* Spring WebFlux 的 Controller 可以直接返回 `Mono` 类型值，其余规则与 Web MVC 的 Controller 层一致
* 使用 `@EnableWebFlux` 注解开启 Spring WebFlux
* 使用 `@EnableWebFluxSecurity` 注解开启用于 WebFlux 的 Spring Security，所有配置方法与用于 Web MVC 的 Spring Security 一致
* 使用 `WebFluxConfigurer` 替代 `WebMvcConfigurer` 接口用于服务器配置
* `WebClient` 内置响应式方法
