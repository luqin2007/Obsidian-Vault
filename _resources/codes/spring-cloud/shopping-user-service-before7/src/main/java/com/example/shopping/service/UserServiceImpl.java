package com.example.shopping.service;

import com.example.shopping.dto.UserDto;
import com.example.shopping.entity.User;
import com.example.shopping.exception.UserNotFoundException;
import com.example.shopping.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> getPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User load(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional
    public User save(UserDto userDto) {
        User user = userRepository
                .findById(userDto.id())
                .orElseGet(User::new);
        user.setAvatar(userDto.avatar());
        user.setNickname(userDto.nickname());
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
