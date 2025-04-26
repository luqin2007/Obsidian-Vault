package com.example.mybank;

import com.example.mybank.domain.BankAccountDetails;
import com.example.mybank.domain.FixedDepositDetails;
import com.example.mybank.service.BankAccountService;
import com.example.mybank.service.FixedDepositService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.example.mybank.config")
public class MainApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MainApplication.class, args);

        BankAccountService accountService = context.getBean(BankAccountService.class);
        BankAccountDetails details = new BankAccountDetails();
        details.setBalanceAmount(100);
        details.setLastTransactionTs(new Date());
        details = accountService.createAccount(details);
        accountService.subtractAmount(details.getAccountId(), 10);
        System.out.println("=========================================================");

        FixedDepositService depositService = context.getBean(FixedDepositService.class);
        FixedDepositDetails example = new FixedDepositDetails();
        example.setAmount(15000);
        List<FixedDepositDetails> list = depositService.findExample(example);
        for (FixedDepositDetails fixedDepositDetails : list) {
            System.out.println(fixedDepositDetails);
        }
    }
}
