package com.spring.service.Impl;

import com.richuff.anno.Autowired;
import com.richuff.anno.Component;
import com.richuff.service.UserService;
import com.richuff.service.OrderService;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Component("userService")
public class UserServiceImpl implements UserService, BeanPostProcessor {

    @Autowired
    private OrderService orderService;

    @Override
    public void test() {
        String order = orderService.order();
        System.out.println(order);
    }
}
