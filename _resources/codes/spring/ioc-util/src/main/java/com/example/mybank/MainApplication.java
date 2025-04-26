package com.example.mybank;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApplication {

    public static void main(String[] args) {
        // 设 xml 文件在 resources/applicationContext.xml
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name + " : " + context.getBean(name));
        }
    }
}
