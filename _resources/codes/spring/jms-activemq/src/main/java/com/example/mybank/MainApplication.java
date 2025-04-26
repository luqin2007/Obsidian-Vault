package com.example.mybank;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApplication {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("com.example.mybank");
    }
}
