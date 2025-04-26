> [!note] 依赖：`org.springframework.boot:spring-boot-starter-data-jpa`

* `@EnableJpaRepositories` 注解表示启用 Spring Data JPA
	* `transactionManagerRef`：`TransactionManager` 对象，默认 `transactionManager`

```java fold
@Configuration
@EnableJpaRepositories(basePackages = "com.example.mybank.dao")
public class JpaDataProfileConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        // 配置 DataSource
        entityManagerFactory.setDataSource(dataSource);
        // 配置 Hibernate
        entityManagerFactory.setPackagesToScan("com.example.mybank.domain");
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.id.new_generator_mappings", "false");
        entityManagerFactory.setJpaProperties(properties);
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager) {
        return new TransactionTemplate(platformTransactionManager);
    }
}
```

>[!success] Spring Boot 3 后只用 `@EnableJpaRepositories` 即可，默认 JPA 提供器为 `hibernate`，在 `application.properties` 中进行额外配置

| 参数                                | 说明                                           |
| --------------------------------- | -------------------------------------------- |
| `basePackages`                    | 指定实现了 `Repository` 接口的类所在包                   |
| `repositoryImplementationPostfix` | 查找实现了 `Repository` 接口的类的后缀，默认 `Impl`         |
| `transactionManagerRef`           | 事务管理的 `PlatformTransactionManager` bean 对象引用 |
| `queryLookupStrategy`             | 查询策略，即从方法名称解析 SQL 语句的方法                      |

```reference
file: "@/_resources/codes/spring/data-jpa/src/main/java/com/example/mybank/config/MyBankConfig.java"
start: 6
end: 9
```

```reference
file: "@/_resources/codes/spring/data-jpa/src/main/resources/application.properties"
```

XML 注册使用 `xmlns:jpa` 命名空间
- `jpa:repositories base-package`：指定注册的 `Repository` 实现
- `LocalContainerEntityManagerFactoryBean`：配置 JPA 的 `EntityManagerFactory`
	- `setJpaVendorAdapter` 设置 JPA `PersistenceProvider` 实现
	- `setJpaProperties` 配置 JPA 属性

  ```xml
  <beans ... 
         xmlns:jpa="http://www.springframework.org/schema/data/jpa"
         xsi:schemaLocation="http://www.springframework.org/schema/jpa http://www.springframework.org/schema/data/jpa/spring-jpa.xsd
                             ...">

      <jpa:repositories base-package="..." />
  </beans>
  ```

