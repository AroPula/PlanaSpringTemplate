package com.richuff;

import com.richuff.config.AppConfig;
import com.richuff.service.UserService;

public class MainTest {
    public static void main(String[] args) {
        RCSpringContext rcSpringContext  = new RCSpringContext(AppConfig.class);
        UserService userService = (UserService)rcSpringContext.getBean("userService");
        userService.test();
    }
}
