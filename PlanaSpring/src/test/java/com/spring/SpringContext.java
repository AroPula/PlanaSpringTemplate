package com.spring;

import com.spring.config.AppConfig;
import com.spring.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContext {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.test();
    }
}
