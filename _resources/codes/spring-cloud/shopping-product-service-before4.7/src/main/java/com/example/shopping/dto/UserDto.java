package com.example.shopping.dto;

/**
 * 用户
 *
 * @param id       用户 id
 * @param nickname 昵称
 * @param avatar   头像
 * @param port     微服务端口，用于测试负载均衡
 */
public record UserDto(Long id, String nickname, String avatar, int port) {
}
