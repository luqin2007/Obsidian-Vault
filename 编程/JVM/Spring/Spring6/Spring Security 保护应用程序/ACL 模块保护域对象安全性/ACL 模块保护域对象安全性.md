ACL 模块允许在访问域对象（数据库数据）时，先查找数据库获取用户信息，检查用户是否具有访问权限。
- [[标准 ACL 表]]
- [[方法级安全配置]]
- [[域对象安全配置]]
- [[运行时管理 ACL]]

> [!note] 依赖：`org.springframework.security:spring-security-acl`

1. 使用 `JdbcDaoImpl` 作为 `AuthenticationManager`

````tabs
tab: XML

```xml
<bean id="userDetailsService" class="org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl">
    <property name="dataSource" ref="dataSource" />
</bean>
<security:authentication-manager>
    <security:authentication-provider user-service-ref="userDetailsService">
        <security:password-encoder hash="bcrypt" />
    </security:authentication-provider>
</security:authentication-manager>
```

tab: Java

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, DataSource dataSource) throws Exception {
    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    JdbcDaoImpl userDetailsManager = new JdbcDaoImpl();
    userDetailsManager.setDataSource(dataSource);
    httpSecurity.userDetailsService(userDetailsManager);
    
    // ...
    
    return httpSecurity.build();
}
```
````

2. 配置 `JdbcMutableAclService` 用于对数据库的 ACL 权限配置进行 CRUD 操作
    `JdbcMutableAclService` 除 `DataSource` 外，还需要一个 `LookupStrategy` 和一个 `AclCache`
    * `AclCache`：负责 ACL 缓存的 `AclCache` 对象
	    * `EhCacheBasedAclCache`： Spring 5 一个利用 `EhCache` 2.x 缓存 ACL 的实现
	    * `SpringCacheBasedAclCache`：Spring 6 之后的通用缓存实现

```java
@Bean
public SpringCacheBasedAclCache aclCache(AclAuthorizationStrategy aclAuthorizationStrategy,
                                         PermissionGrantingStrategy permissionGrantingStrategy) throws IOException {
    ClassPathResource resource = new ClassPathResource("ehcache.xml");
    CachingProvider cachingProvider = Caching.getCachingProvider();
    CacheManager manager = cachingProvider.getCacheManager(resource.getURI(), resource.getClassLoader());
    JCacheCacheManager jm = new JCacheCacheManager(manager);
    return new SpringCacheBasedAclCache(jm.getCache("myCache"), permissionGrantingStrategy, aclAuthorizationStrategy);
}
```
    
	* `LookupStrategy`：负责查找 ACL 信息，Spring 内置 `BasicLookupStrategy` 实现使用 JDBC 查询标准 ACL 表
      * `DataSource`：包含 ACL 表的数据库
      * `AclAuthorizationStrategy`：决定一个 SID 是否具有对域对象实例的 ACL 条目执行**管理操作<sup>（更改 ACL 条目信息等）</sup>**的权限
        * Spring 提供 `AclAuthorizationStrategyImpl` 接受 `GrantedAuthority` 判断
          * `SimpleGrantedAuthority` 可以根据用户角色决定是否具有管理权限
      * `PermissionGrantingStrategy`：根据分配给 SID 的权限授予或拒绝对象访问策略
        * `DefaultPermissionGrantingStrategy` 接收一个 `AuditLogger` 负责记录授权结果
          授权结果根据的是 `ACL_ENTRY` 的 `audit_success` 和 `audit_failed` 列的值
          * `ConsoleAuditLogger`：将结果输出到控制台

# 数据库表设计

记录了管理的域对象类型和对象，以及各用户或角色的权限

- `USERS` 用户表

| 列名         | 类型       | 说明     |
| ---------- | -------- | ------ |
| `username` | `string` | 用户名    |
| `password` | `string` | 密码     |
| `enabled`  | `bool`   | 用户是否启用 |

- `AUTHORITIES` 记录每个用户对应的角色

| 列名          | 类型       | 说明  |
| ----------- | -------- | --- |
| `username`  | `string` | 用户名 |
| `authority` | `string` | 角色  |

‍
