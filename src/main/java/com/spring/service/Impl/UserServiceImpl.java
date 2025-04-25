package com.spring.service.Impl;

import com.spring.service.UserService;
import org.springframework.stereotype.Component;

@Component("userService")
public class UserServiceImpl implements UserService {

    @Override
    public void test() {
        System.out.println("test");
    }
}
