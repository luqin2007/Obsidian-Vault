package com.example.mybank;

import com.example.mybank.dao.FixedDepositDao;
import com.example.mybank.domain.FixedDepositDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication(scanBasePackages = "com.example.mybank.config")
public class MainApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MainApplication.class, args);
        FixedDepositDao dao = (FixedDepositDao) context.getBean("fixedDepositDao");
        FixedDepositDetails fd = dao.getFixedDeposit(3);
        fd.setAAmount(fd.getAAmount() + 1000);
        int id = dao.createFixedDeposit(fd);
        System.out.println(id);
    }
}
