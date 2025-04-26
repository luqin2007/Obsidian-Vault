package com.example.helloworldserviceprovider.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello 服务
 */
@RestController
@RequestMapping("/hello-provider")
public class HelloProviderEndpoint {

    @GetMapping("/{name}")
    public String hello(@PathVariable String name) {
        return "Hello, " + name + "!";
    }
}
