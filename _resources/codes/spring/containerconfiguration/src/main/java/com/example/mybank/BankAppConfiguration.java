package com.example.mybank;

import com.example.mybank.service.FixedDepositService;
import com.example.mybank.service.FixedDepositServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankAppConfiguration {

    @Bean
    public FixedDepositService fixedDepositService() {
        return new FixedDepositServiceImpl();
    }
}
