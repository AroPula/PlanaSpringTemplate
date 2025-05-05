package com.richuff.mapper;

import com.richuff.entity.User;

import java.util.List;

public interface UserMapper {
    List<User> list();

    User findUserById(Long id);

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(Integer id);
}
