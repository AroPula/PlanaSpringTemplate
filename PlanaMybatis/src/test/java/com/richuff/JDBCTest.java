package com.richuff;

import com.richuff.entity.User;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCTest {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    List<User> userList = new ArrayList<>();

    @Test
    public void TestJdbc(){
        try{
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //设置数据库连接地址和用户和密码
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/richu1?useSSL=false&serverTimezone=UTC","root","password");
            preparedStatement = connection.prepareStatement("select * from user");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                userList.add(User.builder().id(id).name(name).age(age).build());
            }
            System.out.println("userList.toString() = " + userList.toString());
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        } finally {
            try {
                if (connection != null){
                    connection.close();
                }
                if (preparedStatement != null){
                    preparedStatement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void TestJdbcUpdate(){
        try{
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //设置数据库连接地址和用户和密码
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/richu1?useSSL=false&serverTimezone=UTC","root","password");
            preparedStatement = connection.prepareStatement("insert into user(id,name,age) values(?,?,?)");
            preparedStatement.setObject(1,666);
            preparedStatement.setObject(2,"jerry");
            preparedStatement.setObject(3,66);
            int count = preparedStatement.executeUpdate();
            System.out.println("count = " + count);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        } finally {
            try {
                if (connection != null){
                    connection.close();
                }
                if (preparedStatement != null){
                    preparedStatement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
