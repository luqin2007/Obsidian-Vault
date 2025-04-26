package com.example.helloworldserviceprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 微服务提供者
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HelloworldServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloworldServiceProviderApplication.class, args);
    }

}
