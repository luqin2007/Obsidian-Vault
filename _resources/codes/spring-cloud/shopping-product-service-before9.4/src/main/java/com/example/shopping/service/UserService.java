package com.example.shopping.service;

import com.example.shopping.dto.UserDto;
import com.example.shopping.repository.UserRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements com.example.shopping.api.UserService {

    /**
     * 用户微服务客户端
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
    public UserDto update(Long id, UserDto userDto) {
        UserDto update = userRemoteClient.update(id, userDto);
        userRedisRepository.saveUser(update);
        return update;
    }

    @Override
    public boolean delete(Long id) {
        boolean deleted = userRemoteClient.delete(id);
        if (deleted) {
            userRedisRepository.deleteUser(id);
        }
        return deleted;
    }
}
