package com.example.shopping.api;

import com.example.shopping.model.UserDto;
import com.example.shopping.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserEndpoint implements UserService {

    @Autowired
    private com.example.shopping.service.UserService userService;
    @Value("${server.port}")
    private int port;

    @Override
    public List<UserDto> findAll(Pageable pageable) {
        return userService.getPage(pageable)
                .getContent()
                .stream()
                .map(user -> user.toUserDto(port))
                .toList();
    }

    @Override
    public UserDto detail(@PathVariable Long id) {
        return userService.load(id).toUserDto(port);
    }

    @Override
    public UserDto update(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userService.save(new UserDto(id, userDto.nickname(), userDto.avatar(), port));
        return user == null ? null : user.toUserDto(port);
    }

    @Override
    public boolean delete(@PathVariable Long id){
        this.userService.delete(id);
        return true;
    }
}
