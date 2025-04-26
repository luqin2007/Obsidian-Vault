package com.example.shopping.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "SHOPPING-USER-SERVICE",
//      url = "http://localhost:8285/userservice",
        url = "${gateway.base-url}/userservice",
        fallback = UserServiceFallback.class)
public interface UserService extends com.example.shopping.api.UserService {
}
