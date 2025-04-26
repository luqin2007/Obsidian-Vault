package com.example.mybank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

@SpringBootApplication(scanBasePackages = "com.example.mybank")
public class MainApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class);
        FixedDepositDao dao = context.getBean(FixedDepositDao.class);
        FixedDepositDetails details = new FixedDepositDetails();
        details.setBankAccountId(1);
        details.setCreationDate(new Date());
        details.setTenure(12);
        details.setDepositAmount(10000);
        details.setActive(true);
        int id = dao.createFixedDetail(details);
        System.out.println(id);
        System.out.println(dao.getFixedDetail(id));
    }
}
