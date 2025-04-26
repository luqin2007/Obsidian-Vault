package com.example.mybank;

import org.springframework.beans.factory.config.BeanPostProcessor;

public class InstanceValidationBeanPostProcessor implements BeanPostProcessor {

    public InstanceValidationBeanPostProcessor() {
        System.out.println("Create InstanceValidationBeanPostProcessor");
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.printf("PostProcessing bean %s\n", beanName);
        if (bean instanceof InstanceValidator validator) {
            System.out.printf("Validating instance of %s\n", beanName);
            validator.validateInstance();
        }
        return bean;
    }
}