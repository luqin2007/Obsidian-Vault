package com.example.helloworldserviceconsumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 声明 Hello 服务的客户端
@FeignClient(value = "HELLOWORLD-SERVICE", fallbackFactory = HelloServiceFallback.class)
public interface HelloService {

    @GetMapping("/hello-provider/{name}")
    String hello(@PathVariable String name);
}
