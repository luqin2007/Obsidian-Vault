package com.example.helloworldservicediscovery;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 微服务治理服务
 */
@SpringBootApplication
@EnableEurekaServer
public class HelloworldServiceDiscoveryApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HelloworldServiceDiscoveryApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
