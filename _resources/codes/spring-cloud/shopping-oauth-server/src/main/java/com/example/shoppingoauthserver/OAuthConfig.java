package com.example.shoppingoauthserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuthConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 启用 OAuth 2 授权服务器
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // 关闭 csrf
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // 创建内存模式设置认证客户端 springclouddemo
        RegisteredClient client = RegisteredClient.withId("springclouddemo")
                .clientId("springclouddemo")
                // secret 为 scdsecret
                .clientSecret("{noop}scdsecret")
                // 允许客户端刷新令牌获取新令牌
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // 允许客户端使用用户名、密码模式获取令牌，已弃用
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                // 允许客户端使用客户端模式获取令牌
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("webclient")
                .scope("mobileclient")
                .build();
        return new InMemoryRegisteredClientRepository(client);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 定义一个普通用户 zhangsan, 密码 pwd, 角色为 USER
        UserDetails zhangsan = User.withUsername("zhangsan")
                .password("{noop}pwd")
                .roles("USER")
                .build();

        // 定义一个管理员用户 admin, 密码 adm, 角色为 ADMIN 和 USER
        UserDetails admin = User.withUsername("admin")
                .password("{noop}adm")
                .roles("ADMIN", "USER")
                .build();

        // 使用内存中的用户配置
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(zhangsan);
        userDetailsManager.createUser(admin);

        return userDetailsManager;
    }
}
