package com.richuff.service.Impl;

import com.richuff.anno.Component;
import com.richuff.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component("planaPostProcessor")
public class PlanaPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("before");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Proxy.newProxyInstance(PlanaPostProcessor.class.getClassLoader(),bean.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("代理逻辑");
                return method.invoke(bean,args);
            }
        });

        return bean;
    }
}
