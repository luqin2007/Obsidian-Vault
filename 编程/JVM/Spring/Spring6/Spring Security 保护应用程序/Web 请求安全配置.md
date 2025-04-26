通过 `DelegatingFilterProxy` 过滤器实现 Web 请求安全，可通过 `security` 命名空间或注册 `SecurityFilterChain` 实现

>[!note] Spring 会自动处理和创建 `DelegatingFilterProxy` 过滤器，只需要完成安全配置即可，Spring 会自动完成对应的 `DelegatingFilterProxy` 注册

````tabs
tab: XML

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:security="http://www.springframework.org/schema/security"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="...
                           http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security.xsd">

    <bean id="authFailureHandler" class="com.example.mybank_xml.handler.MyAuthFailureHandler" />

    <security:http>
        <security:access-denied-handler error-page="/access-denied" />
        <security:intercept-url pattern="/**" access="hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')"/>
        <security:form-login login-page="/login" authentication-failure-handler-ref="authFailureHandler" />
        <security:logout />
        <security:remember-me />
        <security:headers>
            <security:cache-control />
            <security:xss-protection />
        </security:headers>
    </security:http>
</beans>
```

tab: Java

使用 Java 配置时，直接配置 `SecurityFilterChain` 即可：

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling(configurer -> configurer
                        .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/access-denied")))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/**").hasAnyAuthority("ROLE_CUSTOM", "ROLE_ADMIN")
                        .anyRequest().authenticated())
                .formLogin(login -> login.loginPage("/login").failureHandler(new MyAuthFailureHandler()))
                .logout(logout -> {})
                .rememberMe(rememberMe -> {})
                .headers(headers -> headers
                        .cacheControl(cache -> {})
                        .xssProtection(xss -> {}));
        return httpSecurity.build();
    }
}
```
````

* 对于匹配 `/**`（即所有页面）的访问请求，持有 `ROLE_CUSTOM`、`ROLE_ADMIN` 身份的用户可以访问
* 对于登录页面（`form login`），任何用户都可以访问
    * `login page` 指定访问地址，默认 `/login`
* 对于登出请求（`logout`），任何用户可以访问
    * `logout url` 指定访问地址，默认 `/logout`
    * 可以配置 `delete cookies`，`invalidate session` 等额外功能
* 启用 `rememberMe` 身份验证，Spring 为认证的用户产生唯一令牌自动认证，可存于 cookie 等
* 添加 Spring Security 的安全响应头
    * `cache control` 添加 `Cache-Control`、`Pragma`、`Expries` 头
    * `xss protection` 添加 `X-XSS-Protection` 头

> `MyAuthFailureHandler` 是一个认证失败后的 `Handler`
>
> ```java
> public class MyAuthFailureHandler implements AuthenticationFailureHandler {
>     @Override
>     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
>         request.setAttribute("exceptionMsg", exception.getMessage());
>         response.sendRedirect(request.getContextPath() + "/login?exceptionMsg=" + exception.getMessage());
>     }
> }
> ```

之后，配置 `AuthenticationManager` 完成用户认证和身份请求。`AuthenticationManager` 通过一个或多个 `AuthenticationProvider` 进行身份验证。

> [!note] 针对 LDAP 服务器可以使用 `LdapAuthenticationProvider`

````tabs
tab: XML

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:security="http://www.springframework.org/schema/security"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/security https://www.springframework.org/schema/security/spring-security.xsd">

    <security:authentication-manager>
        <!-- 这里只注册了一个使用 Bcrypt 加密保存密码的 Provider -->
        <security:authentication-provider>
            <security:password-encoder hash="bcrypt" />
            <security:user-service>
                <security:user name="admin" password="$2a$10$tedljrxGtyQ/IjpfhTYblOSgBhtCYUMFVoWBdhzCtsGWxwxR2aCZu" authorities="ROLE_ADMIN" />
                <security:user name="cust1" password="$2a$10$GAyoJ5qh2tejzVzW8jYPQetLLbvHyZwM5WprAzk4Uthh.LqhSIW9G" authorities="ROLE_CUSTOMER" />
                <security:user name="cust2" password="$2a$10$CX7U5Q4gShRnPMVPq6n43O16D9yYC3fRc6BeuRW5mNqGj6aN596ai" authorities="ROLE_CUSTOMER" />
            </security:user-service>
        </security:authentication-provider>
    </security:authentication-manager>
</beans>
```

tab: Java

```java
@Bean
public AuthenticationManager authenticationManager() {
    InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
    userDetailsManager.createUser(User
            .withUsername("admin")
            .password("{bcrypt}$2a$10$tedljrxGtyQ/IjpfhTYblOSgBhtCYUMFVoWBdhzCtsGWxwxR2aCZu")
            .authorities("ROLE_ADMIN")
            .build());
    userDetailsManager.createUser(User
            .withUsername("cust1")
            .password("{bcrypt}$2a$10$GAyoJ5qh2tejzVzW8jYPQetLLbvHyZwM5WprAzk4Uthh.LqhSIW9G")
            .authorities("ROLE_CUSTOM")
            .build());
    userDetailsManager.createUser(User
            .withUsername("cust2")
            .password("{bcrypt}$2a$10$CX7U5Q4gShRnPMVPq6n43O16D9yYC3fRc6BeuRW5mNqGj6aN596ai")
            .authorities("ROLE_CUSTOM")
            .build());
    return new ProviderManager(provider);
}
```
````

# 生成 `bcrypt` 加密的密码密文

```java
void genPassword() {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    System.out.println(encoder.encode("admin"));
    System.out.println(encoder.encode("cust1"));
    System.out.println(encoder.encode("cust2"));
}
```

# 网页请求附加 CSRF

为请求附加 `csrf`：
* JSP：使用 `<security:csrfInput/>`
* `Thymeleaf`：一般情况下不需要手动配置，Spring Security 默认自动处理这一过程

```html
<form ...>
    <input type="hidden" name="_csrf" th:value=${_csrf.token}>
</form>
```
