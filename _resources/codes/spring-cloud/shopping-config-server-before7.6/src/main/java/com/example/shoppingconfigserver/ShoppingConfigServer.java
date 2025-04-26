package com.example.shoppingconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ShoppingConfigServer {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingConfigServer.class, args);
    }

}
