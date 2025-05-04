package com.richuff;

import com.richuff.dao.UserDao;
import com.richuff.mapper.UserMapper;
import com.richuff.mybatis.io.Resource;
import com.richuff.mybatis.jdbc.session.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

@Slf4j
public class MybatisTest {
    UserMapper userMapper;

    @Before
    public void init()  {
        String resourceName = "mybatis-config.xml";
        InputStream resource = Resource.getResourceAsStream(resourceName);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resource);
        SqlSessionDefault sqlSession = (SqlSessionDefault) sqlSessionFactory.openSession();
        UserDao userDao = (UserDao) sqlSession.getMapper(UserDao.class);
        int count = userDao.add(10);
        System.out.println("count = " + count);
//        System.out.println("sqlSession = " + sqlSession);
//        System.out.println("sqlSession.configuration.getMappedStatementMap() = " + sqlSession.configuration.getMappedStatementMap());
//        String resourceName = "mapper/UserMapper.xml";
//        Configuration configuration = new Configuration();
//        XmlConfigurationBuilder builder = new XmlConfigurationBuilder(configuration);
//        String resource = "mybatis-config.xml";
//        InputStream resourceStream = Resource.getResourceAsStream(resource);
//        configuration = builder.parseConfiguration(resourceStream);
//        log.info(configuration.getMappedStatementMap().toString());
    }

    @Test
    public void TestMybatis(){

        //加载配置文件
        //List<User> userList = userMapper.list();
        //System.out.println("userList.toString() = " + userList.toString());
    }
}
