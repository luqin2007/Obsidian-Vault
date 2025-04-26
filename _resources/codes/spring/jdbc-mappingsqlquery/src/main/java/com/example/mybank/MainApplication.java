package com.example.mybank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Date;

@SpringBootApplication(scanBasePackages = "com.example.mybank")
public class MainApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class);
        FixedDepositDao dao = context.getBean(FixedDepositDao.class);
        System.out.println(dao.getFixedDepositDetails(3));
    }
}
