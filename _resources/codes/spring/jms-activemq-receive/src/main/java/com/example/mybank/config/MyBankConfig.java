package com.example.mybank.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.example.mybank.service", "com.example.mybank.dao"})
public class MyBankConfig {

}
