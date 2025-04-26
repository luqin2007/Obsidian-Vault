package com.example.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                // 禁用 CSRF 以允许跨域
                .csrf(AbstractHttpConfigurer::disable)
                // 启用 OAuth2.0 Resources 服务器
                .authorizeHttpRequests(registry -> registry.anyRequest().authenticated())
                .oauth2ResourceServer(configurer -> configurer.jwt(jwtConfigurer -> {}))
                .build();
    }
}
