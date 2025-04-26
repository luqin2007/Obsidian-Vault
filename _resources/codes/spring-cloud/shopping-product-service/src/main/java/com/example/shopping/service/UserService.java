package com.example.shopping.service;

import com.example.shopping.model.UserDto;
import com.example.shopping.model.UserMq;
import com.example.shopping.repository.UserRedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements com.example.shopping.api.UserService {

    /**
     * 消息传递通道
     */
    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 用户服务降级
     */
    @Autowired
    private UserRemoteClient userRemoteClient;

    /**
     * 用户缓存数据库
     */
    @Autowired
    private UserRedisRepository userRedisRepository;

    @Override
    public List<UserDto> findAll(Pageable pageable) {
        return userRemoteClient.findAll(pageable);
    }

    @Override
    public UserDto detail(Long id) {
        UserDto user = userRedisRepository.findUser(id);
        if (user == null) {
            // 无缓存
            user = userRemoteClient.detail(id);
            userRedisRepository.saveUser(user);
        }
        return user;
    }

    @Override
    public UserDto update(Long id, UserDto userDto) throws JsonProcessingException {
        UserMq data = UserMq.updateUser(userDto.id(), userDto.nickname(), userDto.avatar());
        streamBridge.send("user", objectMapper.writeValueAsString(data));
        userRedisRepository.deleteUser(id);
        return userDto;
    }

    @Override
    public boolean delete(Long id) throws JsonProcessingException {
        UserMq data = UserMq.deleteUser(id);
        streamBridge.send("user", objectMapper.writeValueAsString(data));
        userRedisRepository.deleteUser(id);
        return true;
    }
}
