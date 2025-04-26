# Spring Boot 应用安全

`Spring Security` 为 Spring 应用提供认证和鉴权机制：`org.springframework.boot:spring-boot-starter-security`，配置时声明一个 `SecurityFilterChain` 对象即可

1. 创建用户认证

```reference
file: "@/_resources/codes/spring-cloud/shopping-product-service-before-10.3/src/main/java/com/example/shopping/config/SecurityConfiguration.java"
start: 26
end: 46
```

2. 实现用户鉴权

```reference
file: "@/_resources/codes/spring-cloud/shopping-product-service-before-10.3/src/main/java/com/example/shopping/config/SecurityConfiguration.java"
start: 48
end: 60
```

> [!note] 其他更详细使用见 [[../Spring6/Spring Security 保护应用程序/Spring Security 保护应用程序|Spring Security 保护应用程序]]

# 微服务安全

微服务的安全是服务器-服务器与用户-服务器的双重认证场景，也需要更细粒度的安全管控方案。David Borsos 在 2016 微服务大会提出四种方案：

- 单点登录：SSO，每个与用户交互的服务都必须与认证服务通信，常见但产生大量额外琐碎流量
- 分布式会话：Session，将用户会话信息存储于共享存储（Redis 等），扩展性好，但需要保护数据安全，实现复杂
- 客户端令牌：由客户端生成 Token 并包含足够信息，在认证服务器签名，问题是如何及时注销用户认证信息，如 JWT
	- Token 为用户信息集合，而非无意义 ID，且包含足够多信息可用于直接完成身份校验
	- 由服务器签发，因此只要可解码，就可以认为信息为有效
	- Token 通过请求头 Authorization 携带，不需要服务端额外存储，允许跨端跨域
- 客户端令牌+API 网关：在 API 网关将客户端 Token 转换为内部会话 Token，

# OAuth2.0

`````col
````col-md
flexGrow=1
===
一种基于令牌的开放、安全的用户认证协议
- 客户端：或第三方应用，向资源服务器发出访问受保护资源请求的实体
- 资源拥有者：用户，即对资源有授权能力的人
- 授权服务器：认证服务器，为客户端应用提供访问令牌
- 资源服务器：资源所在服务器，受安全认证保护
````
````col-md
flexGrow=1
===
![[../../../../_resources/images/微服务应用安全 Security 2024-10-16 01.03.01.excalidraw]]
````
`````

OAuth 2.0 提供四种客户端授权模式：

`````col
````col-md
flexGrow=1
===
- 授权码（Authorization Code）模式：OAuth 2.0 最完整、最严密的模式
  1. 用户访问授权客户端，客户端引导用户到授权服务器
  2. 用户选择是否允许给客户端授权
  3. 用户同意授权，授权服务器重定向到客户端指定地址并携带授权码
  4. 客户端收到授权码，附加重定向页面，经客户端后台向授权服务器请求令牌
  5. 授权服务器校验授权码后，向客户端发送请求令牌和更新令牌并重定向
````
````col-md
flexGrow=1
===
![[../../../../_resources/images/微服务应用安全 Security 2024-10-16 01.16.47.excalidraw|80%]]
````
`````
- 简化（Implicit）模式：客户端不通过后台服务器，直接向授权服务器申请令牌
- 密码模式（Resource Owner Password Credentials）：客户端通过用户名、密码直接从授权服务器获取授权
- 客户端模式：客户端直接以客户端名义申请授权，实际不存在授权问题

> [!note] 依赖：在 Security 同时，还应添加对应依赖：
> - OAuth 认证服务端：`org.springframework.boot:spring-boot-starter-oauth2-authorization-server`
> - OAuth 资源服务端：`org.springframework.boot:spring-boot-starter-oauth2-resource-server`
> - OAuth 客户端：`org.springframework.boot:spring-boot-starter-oauth2-client`

## 认证服务器

使用 Spring Security 创建一个普通 OAuth2 认证服务器，可向 `localhost:<port>/oauth2/token` 发送带有 OAuth2.0 参数的 post 请求测试

```reference fold
file: "@/_resources/codes/spring-cloud/shopping-oauth-server/src/main/java/com/example/shoppingoauthserver/OAuthConfig.java"
start: 18
```

![[../../../../_resources/images/Pasted image 20241018013235.png]]

## 客户端

1. 将原本 `spring-security` 依赖替换成 `org.springframework.boot:spring-boot-starter-oauth2-resource-server`

2. 在 `application.properties` 中配置认证检查地址

```reference
file: "@/_resources/codes/spring-cloud/config-repo-files/productservice.properties"
start: 19
end: 20
```

3. 在安全配置中指定 `oauth2ResourceServer`

```reference
file: "@/_resources/codes/spring-cloud/shopping-product-service/src/main/java/com/example/shopping/config/SecurityConfiguration.java"
start: 14
end: 23
```
