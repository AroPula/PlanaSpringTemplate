package com.richuff;

import com.richuff.dao.UserDao;
import com.richuff.entity.User;
import com.richuff.mapper.UserMapper;
import com.richuff.mybatis.io.Resource;
import com.richuff.mybatis.jdbc.session.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

@Slf4j
public class MybatisTest {
    UserMapper userMapper;

    @Before
    public void init()  {
        String resourceName = "mybatis-config.xml";
        InputStream resource = Resource.getResourceAsStream(resourceName);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resource);
        SqlSessionDefault sqlSession = (SqlSessionDefault) sqlSessionFactory.openSession();
        userMapper = (UserMapper) sqlSession.getMapper(UserMapper.class);

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
        List<User> users= userMapper.list();
        System.out.println("users = " + users);
        //加载配置文件
        //List<User> userList = userMapper.list();
        //System.out.println("userList.toString() = " + userList.toString());
    }

    @Test
    public void testGet(){
        Long id = 3L;
        User user = userMapper.findUserById(id);
        System.out.println("user = " + user);
    }

    @Test
    public void testDelete(){
        Long id = 3L;
        Integer count = userMapper.deleteUser(id);
        System.out.println("count = " + count);
    }

    @Test
    public void testUpdate(){
        Integer integer = Integer.valueOf("100");
        User user = User.builder().id(666L).name("cnm").age(integer).build();
        Integer count = userMapper.updateUser(user);
        System.out.println("count = " + count);
    }

    @Test
    public void testInsert(){
        Integer integer = Integer.valueOf("100");
        User user = User.builder().id(3L).name("cnm").age(integer).build();
        Integer count = userMapper.addUser(user);
        System.out.println("count = " + count);
    }
}
