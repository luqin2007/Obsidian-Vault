package com.example.mybank;

import com.example.mybank.services.MyService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Sample {

    @Autowired
    @Qualifier("service")
    private AService service;

    @Autowired
    @Qualifier("myservice")
    private Set<MyService> myServices;

    @Autowired
    public Sample(@Qualifier("ABean") Object aBean, BBean bBean) {
        System.out.println(aBean);
        System.out.println(bBean);
    }

    @PostConstruct
    public void onPostConstruct() {
        System.out.println(service);
        System.out.println(myServices);
    }
}
