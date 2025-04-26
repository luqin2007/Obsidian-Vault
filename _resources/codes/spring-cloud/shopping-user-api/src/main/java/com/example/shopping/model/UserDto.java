package com.example.shopping.model;

import java.io.Serializable;

/**
 * 用户
 */
public record UserDto(Long id, String nickname, String avatar, int port) implements Serializable {
}
