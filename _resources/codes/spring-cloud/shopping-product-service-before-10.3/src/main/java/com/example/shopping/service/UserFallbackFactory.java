package com.example.shopping.service;

import com.example.shopping.model.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFallbackFactory implements FallbackFactory<UserRemoteClient> {

    @Override
    public UserRemoteClient create(Throwable cause) {
        return new Fallback();
    }

    private static class Fallback implements UserRemoteClient {

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
}
