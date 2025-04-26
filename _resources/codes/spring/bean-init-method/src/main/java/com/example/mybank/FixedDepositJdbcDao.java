package com.example.mybank;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class FixedDepositJdbcDao extends FixedDepositDao implements InitializingBean, DisposableBean {

    public void defaultInitialize() {
        System.out.println("Default initialize method");
    }

    public void defaultDestroy() {
        System.out.println("Default destroy method");
    }

    public void initializeDbConnection() {
        System.out.println("Initializing DB connection");
        // do something
    }

    public void closeDbConnection() {
        System.out.println("Close DB connection");
        // do something
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet method");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy method");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Post-construct method");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("Pre-destroy method");
    }
}
