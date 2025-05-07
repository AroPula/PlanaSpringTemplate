package com.richuff.mapper;

import com.richuff.entity.User;

import java.util.List;

public interface UserMapper {
    List<User> list();

    User findUserById(Long id);

    Integer addUser(User user);

    Integer updateUser(User user);

    Integer deleteUser(Long id);
}
