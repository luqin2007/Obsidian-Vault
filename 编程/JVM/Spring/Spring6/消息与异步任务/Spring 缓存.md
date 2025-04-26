Spring 支持对多种缓存实现使用统一接口的开箱即用的解决方案，使用 `CacheManager` 提供对应的缓存管理器的包装。

|缓存管理器|实现|
| ------------| -----------------------------------|
|`ConcurrentMapCacheManager`|`ConcurrentMap`|
|`EhCacheCacheManager`|`Ehcache`|
||`Caffeine`|
||`Guava`|
||`GemFire`|
|`JCacheCacheManager`|JSR107-Java 临时缓存 JCACHE（`CacheManager`）|
|`SimpleCacheManager`|`ConcurrentMap`，简单缓存和测试场场景|

首先，声明一个 `CacheManager` 并开启 Cache 注解。注解配置需要 `@EnableCaching`

```xml
<beans ...
       xmlns:cache="http://www.springframework.org/schema/cache"
       xsi:schemaLocation="...
                           http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd">

    <cache:annotation-driven cache-manager="myCacheManager" />
    <bean id="myCacheManager" class="org.springframework.cache.support.SimpleCacheManager">
        <property name="caches">
            <set>
                <bean class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean">
                    <property name="name" value="fixedDepositList" />
                </bean>
                <bean class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean">
                    <property name="name" value="fixedDeposit" />
                </bean>
            </set>
        </property>
    </bean>
</beans>
```

> 可以使用 `xmlns:cache` 命名空间

之后，通过一系列注解使用 Cache：

* `@Cacheable`：某个函数使用缓存。

  ```java
  @Cacheable(cacheNames = "fixedDeposit", key = "#fixedDepositDetailsId")
  public FixedDepositDetails getFixedDepositDetailsFromCache(int fixedDepositDetailsId) {
      return fixedDepositRepository.findById(fixedDepositDetailsId).orElseThrow();
  }
  ```

  * `cacheName` 属性指定缓存返回值缓存的缓存区域，即 `cache` 中的 `Set` 内值的 `name` 属性（`fixedDepositList`，`fixedDeposit`）
  * `key` 和 `KeyGenerator` 属性指定一个参数或 `KeyGenerator` 用于计算缓存 Key。默认 `SimpleKeyGenerator` 使用参数值
* `@CacheEvict`：调用方法时从缓存中取出（移除）数据

  ```java
  @CacheEvict(cacheNames = "fixedDeposit", allEntries = true, beforeInvocation = true)
  public int createFixedDeposit(FixedDepositDetails fixedDepositDetails) {
      ...
  }
  ```

  * `cacheName` 属性指定缓存返回值缓存的缓存区域，即 `cache` 中的 `Set` 内值的 `name` 属性（`fixedDepositList`，`fixedDeposit`）
  * `allEntries`：指定是否取出所有缓存条目
  * `beforeInvocation`：是否在方法执行前取出缓存。默认 `false` 表示执行之后取出
  * `key`、`condition`：取出的键和条件，支持 SpEL
* `@CachePut`：方法总被调用，并将结果存入缓存中。与 `@Cacheable` 相比，若缓存已存在，`@CachePut` 仍会调用对应程序

  ```java
  @CachePut(cacheNames = "fixedDeposit", key = "#fixedDepositDetailsId")
  public FixedDepositDetails getFixedDepositDetails(int fixedDepositDetailsId) {
      return fixedDepositRepository.findById(fixedDepositDetailsId).orElseThrow();
  }
  ```

  * 使用方法与 `Cacheable` 类似

> 使用 XML 代替注解配置缓存：使用 `cache` 模式
>
> ```xml
> <beans ...
>        xmlns:cache="http://www.springframework.org/schema/cache"
>        xsi:schemaLocation="...
>                            http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd">
>
>     <cache:advice id="cacheAdvice" cache-manager="myCacheManager">
>         <cache:caching cache="fixedDepositList">
>             <cache:cache-evict method="createFixedDeposit" all-entries="true" before-invocation="true" />
>             <cache:cacheable method="findFixedDepositByBankAccount" />
>         </cache:caching>
>         <cache:caching cache="fixedDeposit">
>             <cache:cache-put method="getFixedDepositDetails" key="#fixedDepositId" />
>             <cache:cacheable method="getFixedDepositDetailsFromCache" key="#fixedDepositId" />
>         </cache:caching>
>     </cache:advice>
> </beans>
> ```
>
> 也可以使用 `aop` 模式设置缓存
>
> ```xml
> <beans ...
>        xmlns:aop="http://www.springframework.org/schema/aop"
>        xsi:schemaLocation="...
>                            http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">
>     <aop:config>
>         <aop:advisor advice-ref="cacheAdvice" pointcut="execution(* com.example.mybank.service.FixedDepositService.* (...))"/>
>     </aop:config>
> </beans>
> ```
>
> 详见面向切面编程

‍
