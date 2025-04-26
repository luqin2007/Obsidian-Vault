package com.example.mybank.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.mybank.dao")
@ComponentScan(basePackages = "com.example.mybank.service")
public class MyBankConfig {
}
