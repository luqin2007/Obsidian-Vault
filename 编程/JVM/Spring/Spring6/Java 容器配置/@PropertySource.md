使用 `@PropertySource` 加载配置文件，可以通过 `@Value("${属性}")` 的方式访问

```java
@Configuration
@PropertySource("classpath:config/mysqlDevDB.properties")
public class DBConfig {

    @Value("${url}")
    private String url;

    @Value("${driverClassName}")
    private String driverClass;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;
  
    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClass(driverClass);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
```

被该注解加载的配置文件会被添加到 `Environment` 类中，因此我们也可以通过自动注入 `Environment` 对象来访问

```java
@Configuration
@PropertySource("classpath:config/mysqlDB.properties")
public class ProdDBConfig {

    @Inject
    Environment environment;

    @Bean
    public DataSource prodDataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setUrl(environment.getProperty("url"));
        dataSource.setUsername(environment.getProperty("username"));
        dataSource.setPassword(environment.getProperty("password"));
        dataSource.setDriverClass(environment.getProperty("driverClassName"));
        return dataSource;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
```
