package com.example.shopping.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JWTTokenFilter extends AbstractGatewayFilterFactory<JWTTokenFilter.Config> {

    public JWTTokenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isNotEmpty(authorization) && authorization.startsWith("Bearer ")) {
                // 解析 JWT 数据
                String token = authorization.substring(7);
                byte[] bytes = Decoders.BASE64.decode(config.getJwtSigningKey());
                SecretKey key = Keys.hmacShaKeyFor(bytes);
                Claims body = Jwts.parser()  // 这里使用了 io.jsonwebtoken.jjwt 库
                        .verifyWith(key).build()
                        .parseSignedClaims(token).getPayload();
                // 将解析后的数据添加到请求头
                exchange.getRequest().mutate()
                        .header("shop-id", body.get("shopId", String.class))
                        .header("username", body.get("userName", String.class))
                        .build();
                System.out.println("JWT: ");
                body.forEach((name, value) -> {
                    System.out.println("  " + name + ": " + value);
                });
                System.out.println("=========================");
            } else {
                // 不存在 JWT 数据
                System.out.println("No JWT Token");
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        String jwtSigningKey;

        public String getJwtSigningKey() {
            return jwtSigningKey;
        }

        public void setJwtSigningKey(String jwtSigningKey) {
            this.jwtSigningKey = jwtSigningKey;
        }
    }
}
