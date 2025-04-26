package com.example.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // 定义一个普通用户 zhangsan, 密码 pwd, 角色为 USER
        UserDetails zhangsan = User.withUsername("zhangsan")
                .password(passwordEncoder.encode("pwd"))
                .roles("USER")
                .build();

        // 定义一个管理员用户 admin, 密码 adm, 角色为 ADMIN 和 USER
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("adm"))
                .roles("ADMIN", "USER")
                .build();

        // 使用内存中的用户配置
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(zhangsan);
        userDetailsManager.createUser(admin);

        return userDetailsManager;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                // 禁用 CSRF 以允许跨域
                .csrf(AbstractHttpConfigurer::disable)
                // 启用 Http Basic 认证
                .httpBasic(configurer -> configurer.init(http))
                .authorizeHttpRequests(register -> register
                        // 对于所有 /products 的 DELETE 请求，需要 ADMIN 角色
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .build();
    }
}
