package com.example.shopping.service;

import com.example.shopping.api.UserService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "SHOPPING-USER-SERVICE",
        url = "${gateway.base-url}/userservice",
        fallbackFactory = UserFallbackFactory.class)
public interface UserRemoteClient extends UserService {
}
