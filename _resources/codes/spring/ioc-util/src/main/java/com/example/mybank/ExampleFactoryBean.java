package com.example.mybank;

import org.springframework.beans.factory.FactoryBean;

public class ExampleFactoryBean implements FactoryBean<String> {

    @Override
    public String getObject() throws Exception {
        return "hello world";
    }

    @Override
    public Class<?> getObjectType() {
        return String.class;
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }
}
