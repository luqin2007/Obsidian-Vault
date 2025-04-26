package com.example.shopping.entity;

import com.example.shopping.dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tbUser")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 用户 id
    @Id
    @GeneratedValue
    private Long id;
    // 昵称
    private String nickname;
    // 头像
    private String avatar;

    public UserDto toUserDto(int port) {
        return new UserDto(id, nickname, avatar, port);
    }
}
