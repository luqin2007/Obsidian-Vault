使用 POST 方式访问时，产生 403 Forbidden 异常：

```log
{
    "timestamp": "2024-04-23T04:28:10.556+00:00",
    "status": 403,
    "error": "Forbidden",
    "path": "/fixedDeposit"
}
```

解决方法：关闭 csrf 检查

```java
package com.example.mybank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
```


