package com.example.shopping.repository;

import com.example.shopping.entity.User;

import java.util.List;

public interface UserRepositoryExtension {

    List<User> findTopUser(int count);
}
