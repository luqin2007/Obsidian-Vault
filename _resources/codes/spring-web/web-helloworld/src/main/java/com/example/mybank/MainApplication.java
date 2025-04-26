package com.example.mybank;

import com.example.mybank.controller.HelloWorldController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(MainApplication.class, args);
        System.out.println(context.getBean(HelloWorldController.class));
    }
}
