package com.example.shopping.service;

import com.example.shopping.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceFallback implements UserService {

    @Value("${server.port}")
    private int port;

    @Override
    public List<UserDto> findAll(Pageable pageable) {
        return List.of();
    }

    @Override
    public UserDto detail(Long id) {
        return new UserDto(id, "Unknown", "", port);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        return new UserDto(id, "Unknown", "", port);
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
