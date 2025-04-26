package com.example.mybank;

import jakarta.validation.ConstraintViolationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Calendar;

public class MainApplication {

    public static void main(String[] args) {
        // 设 xml 文件在 resources/applicationContext.xml
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        CustomerRequestService customerRequestService = (CustomerRequestService) context.getBean("customerRequestService");
        try {
            // jakarta.validation.ConstraintViolationException: submitRequest.description: 个数必须在20和100之间
            customerRequestService.submitRequest("request type", "description < 20", Calendar.getInstance());
        } catch (ConstraintViolationException e) {
            System.out.println(e);
        }
        try {
            Calendar futureDate = Calendar.getInstance();
            futureDate.add(Calendar.YEAR, 1);
            // jakarta.validation.ConstraintViolationException: submitRequest.accountSinceDate: 需要是一个过去的时间
            customerRequestService.submitRequest("request type", "request: description > 20", futureDate);
        } catch (ConstraintViolationException e) {
            System.out.println(e);
        }
        try {
            Calendar pastDate = Calendar.getInstance();
            pastDate.add(Calendar.YEAR, -1);
            // jakarta.validation.ConstraintViolationException: submitRequest.<return value>: 需要是一个将来的时间
            customerRequestService.submitRequest("request type", "request: description > 20", pastDate);
        } catch (ConstraintViolationException e) {
            System.out.println(e);
        }
    }
}
