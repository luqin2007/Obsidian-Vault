package com.example.shopping.service;

import com.example.shopping.model.UserDto;
import com.example.shopping.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    /**
     * 获取用户分页数据
     * @param pageable 分页参数
     * @return 分页数据
     */
    Page<User> getPage(Pageable pageable);

    /**
     * 加载用户信息
     * @param id 用户 id
     */
    User load(Long id);

    /**
     * 保存或更新用户
     */
    User save(UserDto userDto);

    /**
     * 删除用户
     * @param id 用户 id
     */
    void delete(Long id);
}
