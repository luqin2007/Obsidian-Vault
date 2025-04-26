package com.example.mybank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Sample {
    // 直接使用字符串
    @Value("Some currency")
    private String currency;

    // 使用 SpEL: context.getBean("configuration").getEnvironment()
    @Value("#{configuration.environment}")
    private String environment;

    @Override
    public String toString() {
        return "Sample{" +
                "currency='" + currency + '\'' +
                ", environment='" + environment + '\'' +
                '}';
    }
}
