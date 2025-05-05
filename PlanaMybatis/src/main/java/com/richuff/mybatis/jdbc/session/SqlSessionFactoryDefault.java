package com.richuff.mybatis.jdbc.session;

import com.richuff.mybatis.config.Configuration;

public class SqlSessionFactoryDefault implements SqlSessionFactory{
    private Configuration configuration;

    public SqlSessionFactoryDefault(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new SqlSessionDefault(configuration);
    }
}
