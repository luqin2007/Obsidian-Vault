package com.example.shopping.api;

import com.example.shopping.dto.UserDto;
import com.example.shopping.entity.User;
import com.example.shopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserEndpointImpl implements UserEndpoint {

    @Autowired
    private UserService userService;
    @Value("${server.port}")
    private int port;

    @Override
    public List<UserDto> findAll(Pageable pageable) {
        return userService.getPage(pageable)
                .getContent()
                .stream()
                .map(user -> UserDto.fromUser(user, port))
                .toList();
    }

    @Override
    public UserDto detail(@PathVariable Long id) {
        return UserDto.fromUser(userService.load(id), port);
    }

    @Override
    public UserDto update(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userService.save(new UserDto(id, userDto.nickname(), userDto.avatar(), port));
        return user == null ? null : UserDto.fromUser(user, port);
    }

    @Override
    public boolean delete(@PathVariable Long id){
        this.userService.delete(id);
        return true;
    }
}
