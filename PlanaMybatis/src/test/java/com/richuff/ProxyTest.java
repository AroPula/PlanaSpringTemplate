package com.richuff;

import com.richuff.dao.UserDao;
import com.richuff.mapper.UserMapper;
import org.junit.Test;

import java.lang.reflect.Proxy;


public class ProxyTest {
    @Test
    public void TestProxy(){
        UserDao instance = (UserDao) Proxy.newProxyInstance(UserDao.class.getClassLoader(), new Class[]{UserDao.class}, (proxy, method, args) -> {
            if (method.getName().equals("add")){
                int a = (int) args[0];
                int b = 20;
                return a + b;
            }
            return null;
        });
        int count = instance.add(1);
        System.out.println("count = " + count);
    }

    @Test
    public void TestProxyUser(){
        UserMapper instance = (UserMapper) Proxy.newProxyInstance(UserMapper.class.getClassLoader(), new Class[]{UserMapper.class}, (proxy, method, args) -> {
            if (method.getName().equals("add")){
                int a = (int) args[0];
                int b = 20;
                return a + b;
            }
            return null;
        });


    }
}
/*
            DataSource dataSource = configuration.getDataSource();
            Connection connection = dataSource.getConnection();
            MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
            PreparedStatement preparedStatement = connection.prepareStatement(mappedStatement.getSql());
            preparedStatement.setObject(1,mappedStatement.getParameterType());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                T t = clazz.getConstructor().newInstance();

            }
 */