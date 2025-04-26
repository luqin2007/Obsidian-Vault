package com.example.helloworldserviceconsumer.service;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloServiceFallback implements FallbackFactory<HelloService> {

    @Override
    public HelloService create(Throwable cause) {
        // 回退服务对象
        return new HelloService() {

            @Override
            public String hello(String name) {
                return "Hello " + name + ", I'm fallback!";
            }
        };
    }
}
