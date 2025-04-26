package com.example.mybank;

import org.springframework.stereotype.Component;

@Component
public class Configuration {

    private String environment = "value: configuration.environment";

    public String getEnvironment() {
        return environment;
    }
}
