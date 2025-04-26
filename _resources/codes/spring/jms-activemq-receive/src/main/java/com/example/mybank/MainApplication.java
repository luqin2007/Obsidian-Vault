package com.example.mybank;

import com.example.mybank.domain.FixedDepositDetails;
import com.example.mybank.service.FixedDepositService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class MainApplication {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("com.example.mybank.config");

        FixedDepositDetails fdd = new FixedDepositDetails();
        fdd.setBankAccountId(1);
        fdd.setDepositAmount(1000);
        fdd.setTenure(6);
        fdd.setEmail("abc@email.com");
        fdd.setActive("Y");
        fdd.setCreationDate(new Date());
        fdd.setMaturityDate(new Date());

        FixedDepositService service = context.getBean(FixedDepositService.class);
        service.createFixedDeposit(fdd);
    }
}
