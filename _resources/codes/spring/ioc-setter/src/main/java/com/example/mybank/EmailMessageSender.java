package com.example.mybank;

import java.util.Properties;

public class EmailMessageSender {

    private String host;
    private Properties emailProperties;

    public void setHost(String host) {
        this.host = host;
    }

    public void setEmailProperties(Properties emailProperties) {
        this.emailProperties = emailProperties;
    }
}
