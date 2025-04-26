package com.example.shopping.service;

import com.example.shopping.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("SHOPPING-USER-SERVICE")
public interface UserService {

    @GetMapping("/users")
    List<UserDto> findAll(Pageable pageable);

    @GetMapping("/users/{id}")
    UserDto detail(@PathVariable Long id);

    @PostMapping("/users/{id}")
    UserDto update(@PathVariable Long id, @RequestBody UserDto userDto);

    @DeleteMapping("/users/{id}")
    boolean delete(@PathVariable Long id);
}
