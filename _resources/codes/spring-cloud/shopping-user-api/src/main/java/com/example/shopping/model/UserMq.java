package com.example.shopping.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class UserMq implements Serializable {

    public static final String USER_UPDATED = "USER_UPDATED";
    public static final String USER_DELETED = "USER_DELETED";

    @Serial
    private static final long serialVersionUID = 0L;

    private String type;
    private Long id;
    private String name;
    private String avatar;

    public UserMq(String type, Long id, String name, String avatar) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public UserMq() {
    }

    public static UserMq updateUser(Long id, String name, String avatar) {
        return new UserMq(USER_UPDATED, id, name, avatar);
    }

    public static UserMq deleteUser(Long id) {
        return new UserMq(USER_DELETED, id, "", "");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMq userMq = (UserMq) o;
        return Objects.equals(type, userMq.type) && Objects.equals(id, userMq.id) && Objects.equals(name, userMq.name) && Objects.equals(avatar, userMq.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, name, avatar);
    }

    @Override
    public String toString() {
        return "UserMq{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
