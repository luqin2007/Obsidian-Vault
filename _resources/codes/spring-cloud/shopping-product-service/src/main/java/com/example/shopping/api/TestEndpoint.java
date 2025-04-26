package com.example.shopping.api;

import com.example.shopping.model.UserDto;
import com.example.shopping.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product-test")
public class TestEndpoint {

    @Value("${gateway.base-url}")
    private String baseUrl;

    @Autowired
    private UserService userService;

    @GetMapping("/update/{id}/{name}")
    public String updateUser(@PathVariable Long id, @PathVariable String name) throws JsonProcessingException {
        UserDto user = userService.detail(id);
        userService.update(id, new UserDto(id, name, user.avatar(), 0));
        return "redirect:" + baseUrl + "/userservice/users/" + id;
    }
}
