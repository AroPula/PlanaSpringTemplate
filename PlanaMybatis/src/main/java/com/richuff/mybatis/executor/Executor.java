package com.richuff.mybatis.executor;

import com.richuff.mybatis.config.Configuration;
import com.richuff.mybatis.type.MappedStatement;

import java.util.List;

public interface Executor {
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement,Object... args) throws Exception;
}
