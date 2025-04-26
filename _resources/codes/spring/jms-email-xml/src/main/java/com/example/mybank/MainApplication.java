package com.example.mybank;

import com.example.mybank.domain.FixedDepositDetails;
import com.example.mybank.service.FixedDepositService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class MainApplication {

    public static void main(String[] args) {
        var context = new ClassPathXmlApplicationContext("applicationContext.xml");

        FixedDepositDetails fdd = new FixedDepositDetails();
        fdd.setBankAccountId(1);
        fdd.setDepositAmount(1000);
        fdd.setTenure(6);
        fdd.setEmail("mail@mail.com");
        fdd.setActive("Y");
        fdd.setCreationDate(new Date());
        fdd.setMaturityDate(new Date());

        FixedDepositService service = context.getBean(FixedDepositService.class);
        service.createFixedDeposit(fdd);
    }
}
