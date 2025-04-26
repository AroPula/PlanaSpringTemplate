package com.richuff.service.Impl;

import com.richuff.anno.Autowired;
import com.richuff.config.BeanNameWare;
import com.richuff.anno.Component;
import com.richuff.anno.Scope;
import com.richuff.service.OrderService;
import com.richuff.service.UserService;

@Component("userService")
@Scope("prototype")
public class UserServiceImpl implements UserService, BeanNameWare {

    @Autowired
    private OrderService orderService;

    private String BeanName;

    @Override
    public void setBeanName(String name) {
        BeanName = name;
    }

    @Override
    public void test() {
        System.out.println(orderService.order());
        System.out.println(BeanName);
    }

}
