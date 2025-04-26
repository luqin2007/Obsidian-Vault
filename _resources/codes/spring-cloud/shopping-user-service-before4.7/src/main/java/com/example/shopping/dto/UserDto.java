package com.example.shopping.dto;

import com.example.shopping.entity.User;

/**
 * 用户
 */
public record UserDto(Long id, String nickname, String avatar, int port) {
    public static UserDto fromUser(User user, int port) {
        return new UserDto(user.getId(), user.getNickname(), user.getAvatar(), port);
    }
}
