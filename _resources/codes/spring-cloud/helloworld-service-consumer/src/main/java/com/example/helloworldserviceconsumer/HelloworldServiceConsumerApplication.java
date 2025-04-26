package com.example.helloworldserviceconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 微服务消费者
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class HelloworldServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloworldServiceConsumerApplication.class, args);
    }

}
