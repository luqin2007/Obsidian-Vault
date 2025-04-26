package com.example.mybank;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(BankAppConfiguration.class);
        BankAppConfiguration configuration = context.getBean(BankAppConfiguration.class);
    }
}
