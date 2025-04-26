package com.example.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(registry -> registry
                        // 使用 Basic 认证
                        .requestMatchers("/eureka/**").authenticated()
                        .anyRequest().permitAll())
                // 关闭 csrf 防御机制
                .csrf(configurer -> configurer.ignoringRequestMatchers("/eureka/**"))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
