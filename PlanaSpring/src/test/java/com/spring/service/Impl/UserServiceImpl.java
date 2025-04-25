package com.spring.service.Impl;

import com.richuff.anno.Autowired;
import com.richuff.anno.Component;
import com.richuff.service.UserService;
import com.richuff.service.OrderService;

@Component("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private OrderService orderService;

    @Override
    public void test() {
        String order = orderService.order();
        System.out.println(order);
    }
}
