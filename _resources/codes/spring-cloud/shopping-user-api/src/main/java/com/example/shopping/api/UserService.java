package com.example.shopping.api;

import com.example.shopping.model.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserService {

    @ApiOperation(value = "获取用户分页", notes = "获取用户分页数据", httpMethod = "GET", tags = "用户管理相关Api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码，从 0 开始", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页记录数量，默认 20", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序，格式:property,property(,ASC|DESC)", dataType = "String", paramType = "query")
    })
    @GetMapping("/users")
    List<UserDto> findAll(Pageable pageable);

    @ApiOperation(value = "获取用户详细信息", notes = "获取用户详细信息", httpMethod = "GET", tags = "用户管理相关Api")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "用户 id", dataType = "int", paramType = "path")
    )
    @GetMapping("/users/{id}")
    UserDto detail(@PathVariable Long id);

    @ApiOperation(value = "更新用户详情数据", notes = "更新用户详情数据", httpMethod = "POST", tags = "用户管理相关Api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的主键", dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "userDto", value = "用户详情数据", dataType = "UserDto", paramType = "body"),
    })
    @PostMapping("/users/{id}")
    UserDto update(@PathVariable Long id, @RequestBody UserDto userDto) throws JsonProcessingException;

    @ApiOperation(value = "删除指定用户", notes = "删除指定用户", httpMethod = "DELETE", tags = "用户管理相关Api")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "所要删除用户的主键", dataType = "int", paramType = "path")
    )
    @DeleteMapping("/users/{id}")
    boolean delete(@PathVariable Long id) throws JsonProcessingException;
}
