
`````col
````col-md
flexGrow=1
===
> 统一配置应当具有以下特点：
> - 微服务配置数据与服务实例分离，服务部署时配置从集中配置源获取
> - 非侵入式数据织入，不使用硬编码导入
> - 微服务配置统一修改和发布的同时建立版本机制，集中式管理、回溯
> - 高可用，防止雪崩效应

`Spring Cloud Config` 子项目具有中心化、版本控制、动态更新和语言独立等特性
- 具有服务器 `Config Server` 与客户端 `Config Client` 两种角色，集中管理，分布配置
- 支持 Git、SVN、文件系统等多种方式资源管理
- 与 Spring Boot 深度整合
- 与 Eureka、Consul 等整合
````
````col-md
flexGrow=1
===
<br/>

![[../../../../_resources/images/统一配置中心 2024-10-05 17.48.27.excalidraw]]
````
`````

# 创建服务器

## config 服务器

1. 创建一个 Spring Boot 项目，依赖于 `org.springframework.cloud:spring-cloud-config-server`，配置服务器

```reference hl:7
file: "@/_resources/codes/spring-cloud/shopping-config-server-before7.6/src/main/java/com/example/shoppingconfigserver/ShoppingConfigServer.java"
start: 7
end: 9
```

2. 设置配置仓库，git 默认仓库为 `main` 或 `master`（这里使用本地仓库作为测试，也没有配置用户名和密码）
	- `searchPaths`：配置文档所在子目录
	- `basedir`：本地缓存目录，默认 `/tmp-`

```reference hl:7
file: "@/_resources/codes/spring-cloud/shopping-config-server-before7.6/src/main/resources/application.properties"
start: 4
end: 7
```

> 使用本地文件，支持 `{application}`、`{profile}`、`{label}` 占位符：
> - `spring.profiles.active=native`
> - `spring.cloud.config.server.native.searchLocations=<目录>`

`````col
````col-md
flexGrow=2
===
此时，已经可以通过 `http://<config-uri>/<properties-name>/<profile>` 获取到相关配置了。
- `http://<config-uri>/productservice/default` 可以获取以下文件的配置选项：
	- `<git-repo>/application.properties`（或 yml）
	- `<git-repo>/productservice.properties`（或 yml）
- `http://<config-uri>/productservice/dev` 可以获取以下文件的配置选项：
	- `<git-repo>/productservice-dev.properties`（或 yml）
	- `<git-repo>/application-dev.properties`（或 yml）
	- `<git-repo>/productservice.properties`（或 yml）
	- `<git-repo>/application.properties`（或 yml）
````
````col-md
flexGrow=1
===
```dirtree
- config-repo-files
  - .git/...
  - application.properties
  - application-dev.properties
  - productservice.properties
  - productservice-dev.properties
  - userservice.properties
  - userservice-dev.properties
```
````
`````

> 配置文件分为 `application`、`profile`、`label` 三部分，组合方式可以为：
> - `application/profile[/label].properties/yml`
> - `application-profile.properties/yml`
> - `label/application-profile.properties/yml`

### 默认服务配置

当服务器名为 `configserver` 时，Spring 会自动加载 `spring-cloud-config-server.jar` 中的 `configserver.yml` 配置文件

## 微服务客户端

![[../../../../_resources/images/统一配置中心 2024-10-05 20.40.21.excalidraw|80%]]

客户端部分，依赖 `org.springframework.cloud:spring-cloud-starter-config` 并完成相关配置即可
- `spring.application.name` 组成 `<properties-name>` 部分
- `spring.cloud.config.profile` 组成 `<profile>` 部分
- `spring.cloud.config.uri` 组成 `<config-uri>` 部分
- `spring.cloud.config.label` 组成 `<label>` 部分
- `spring.config.import=configserver:`

```reference
file: "@/_resources/codes/spring-cloud/shopping-user-service-before7.6/src/main/resources/application.properties"
start: 4
end: 6
```

> Spring 配置加载优先级，自上而下降低
> 1. 程序启动时命令行传入的 `--xxx` 参数
> 2. `java:comp/env` 加载的 JNDI 属性
> 3. Java 系统属性，使用 `-Dxxx` 配置的属性
> 4. 系统环境变量
> 5. `random.*` 配置的属性，多用于随机数
> 6. 应用特定 `properties` 或 `yml` 文件
> 7. `application.properties` 或 `application.yml`
> 8. `@Configuration`、`@PropertySource`、`@ConfigurationProperties`

# 配置加密

Spring 支持加密传输配置文件，主要依赖于 `JCE`（Java Cryptography Extension）

> [!note] Java 加密扩展（JCE）是一组包，提供加密、密钥算法、协议、认证码算法等，实现对称、非对称、块、流密码加解密支持
> Java 8 及更低版本需要下载扩展 https://www.oracle.com/java/technologies/javase-jce-all-downloads.html ，其他版本不需要

## 对称加密

### 服务端加密 

>[!note] 服务端配置文件中的数据是加密过的，客户端接收到的是解密后的

- 添加配置：`encrypt.key=<密码>`，通常密码存在环境变量而不是配置文件中
- 传送需要加密的数据时添加 `{cipher}` 前缀，YAML 要使用 `''` 包围
	- `{properties}spring.datasource.password={cipher}<加密后的字符串>`
	- `{properties}spring.datasource.password={cipher}{key:<自定义密码>}<加密后的字符串>`
	- `{yaml}password: '{cipher}<加密后的字符串>'`
	- `{yaml}password: '{cipher}{key:<自定义密码>}<加密后的字符串>'`

加密字符串可以通过 Spring Cloud Config 服务器提供的接口获取：
- 加密：POST 协议，`/encrypt` 端点
- 解密：POST 协议，`/decrypt` 端点

通过 Spring Cloud Config 的接口即可看见解密后的配置文件

### 客户端解密

服务端、客户端传递的均为加密数据，客户端接收后解密。此时需要在服务端加密的基础上进行配置：
- 禁用服务端解密：配置 `spring.cloud.config.server.encrypt.enabled=false`
- 客户端解密：以下均在客户端设置
	- 添加依赖：`spring-security-rsa`
	- 将服务端 `encrypt.key` 属性**移动**到客户端 

## 非对称加密

利用 `Keytool` 等工具生成密钥和证书

```shell
# alias     - alias
# keypass   - secret
# storepass - password
# dname     - 证书信息
# keystore  - 生成文件名
keytool -genkeypair -alias tsfjckey -keyalg RSA \
        -dname "CN=SpringCloud Demo,OU=cd826dong,O=xohaa,L=gz,S=gd,C-=china" \
        -keypass javatwostepsfrom -keystore server.jks -storepass twostepsfromjava
```

将生成的 `server.jks` 复制到 `resources` 中，在 `application.properties/yml` 中配置：

```properties
encrypt.key-store.location=server.jks
encrypt.key-store.alias=tsfjckey
encrypt.key-store.password=twostepsfromjava
encrypt.key-store.secret=javatwostepsfrom
```

# 访问安全

同样依赖 `Spring Security`（`org.springframework.boot:spring-boot-starter-security`），配置 `application.properties`：

```reference
file: "@/_resources/codes/spring-cloud/shopping-config-server-before7.6/src/main/resources/application.properties"
start: 9
end: 11
```

客户端不需要依赖 Spring Security，配置下即可：

```reference
file: "@/_resources/codes/spring-cloud/shopping-user-service-before7.6/src/main/resources/application.properties"
start: 8
end: 9
```

# 高可用

## 整合 Eureka

Spring Cloud Config 同样可以作为一个微服务注册到 Eureka 服务器中

```reference
file: "@/_resources/codes/spring-cloud/shopping-config-server/src/main/resources/application.properties"
start: 13
end: 17
```

```reference hl:9
file: "@/_resources/codes/spring-cloud/shopping-config-server/src/main/java/com/example/shoppingconfigserver/ShoppingConfigServer.java"
start: 8
end: 11
```

在微服务中使用 Spring Cloud Gateway 网关访问

```reference
file: "@/_resources/codes/spring-cloud/shopping-product-service-before9/src/main/resources/application.properties"
start: 5
end: 5
```

## 快速失败

在应用启动时检查配置仓库，若不存在则产生错误

1. 在项目启动时向仓库请求配置

```properties
# 单个项目
spring.cloud.config.server.git.repos.shop-repo.pattern=*
spring.cloud.config.server.git.repos.shop-repo.clone-on-start=true
# 全局
spring.cloud.config.server.git.clone-on-start=true
```

2. 有一个配置服务器无法连接时快速失败

```properties
spring.cloud.config.fail-fast=true
```

或者可以设置重连。需要依赖于 `spring-retry` 和 `spring-boot-starter-aop`，并配置 `spring.cloud.config.retry` 相关属性

## 动态刷新

依赖于 `spring-boot-starter-actuator` 后，使用 `/refresh` 端点刷新，或通过 `Spring Cloud Bus` 刷新，详见[[消息驱动 Stream#消息总线 Bus|消息总线]]。
