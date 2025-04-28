package com.richuff.config;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}