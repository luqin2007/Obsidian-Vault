package com.example.mybank;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

public class MainApplication {

    public static void main(String[] args) {
        var context = new ClassPathXmlApplicationContext("applicationContext.xml");
        TaskExecutor taskExecutor = context.getBean("myTaskExecutor", TaskExecutor.class);
        taskExecutor.execute(() -> System.out.println("Hello World"));
    }
}
