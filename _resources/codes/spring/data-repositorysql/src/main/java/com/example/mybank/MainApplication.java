package com.example.mybank;

import com.example.mybank.domain.BankAccountDetails;
import com.example.mybank.service.BankAccountService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Date;

@SpringBootApplication(scanBasePackages = "com.example.mybank.config")
public class MainApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MainApplication.class, args);
        BankAccountService service = context.getBean(BankAccountService.class);
        BankAccountDetails details = new BankAccountDetails();
        details.setBalanceAmount(100);
        details.setLastTransactionTs(new Date());
        service.createAccount(details);
    }
}
