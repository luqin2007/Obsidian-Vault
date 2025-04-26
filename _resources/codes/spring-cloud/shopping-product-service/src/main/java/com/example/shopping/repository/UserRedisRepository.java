package com.example.shopping.repository;

import com.example.shopping.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户 Redis 缓存表
 */
@Repository
public class UserRedisRepository {

    private static final String USER_KEY = "user:";

    private RedisTemplate<String, UserDto> userRedisTemplate;
    private ValueOperations<String, UserDto> operations;

    @Autowired
    private void setUserRedisTemplate(RedisTemplate<String, UserDto> userRedisTemplate) {
        this.userRedisTemplate = userRedisTemplate;
        this.operations = userRedisTemplate.opsForValue();
    }

    /**
     * 保存用户
     */
    public void saveUser(UserDto userDto) {
        operations.set(buildKey(userDto.id()), userDto);
    }

    /**
     * 查找用户
     */
    public UserDto findUser(Long uid) {
        return Optional.of(buildKey(uid))
                .filter(userRedisTemplate::hasKey)
                .map(operations::get)
                .orElse(null);
    }

    /**
     * 删除用户
     */
    public void deleteUser(Long uid) {
        userRedisTemplate.delete(buildKey(uid));
    }

    private String buildKey(Long uid) {
        return USER_KEY + uid;
    }
}
