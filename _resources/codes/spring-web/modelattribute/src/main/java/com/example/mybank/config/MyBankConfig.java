package com.example.mybank.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan(basePackages = {"com.example.mybank.controller", "com.example.mybank.service"})
@EnableScheduling
@EnableWebMvc
@Configuration
public class MyBankConfig {
}
