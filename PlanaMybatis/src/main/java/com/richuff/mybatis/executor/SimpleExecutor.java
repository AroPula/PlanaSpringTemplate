package com.richuff.mybatis.executor;

import com.richuff.mybatis.config.BoundSql;
import com.richuff.mybatis.config.Configuration;
import com.richuff.mybatis.exception.InvalidException;
import com.richuff.mybatis.type.MappedStatement;
import io.micrometer.common.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

public class SimpleExecutor implements Executor{
    /**
     * 执行sql的核心方法，JDBC
     * @param configuration 配置类对象
     * @param mappedStatement mappedStatement对象
     * @param args sql执行的参数
     * @return 查询的结果
     * @param <E> 泛型
     */
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... args) throws Exception{
        //获取数据库连接
        Connection connection = configuration.getDataSource().getConnection();
        //获取执行的sql语句
        String sql = mappedStatement.getSql();
        //解析sql
        try {
            BoundSql boundSql = getBoundSql(sql);
            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
            //设置参数
            String parameterType = mappedStatement.getParameterType();
            Class<?>  parameterClass = this.getClass(parameterType);
            //获取sql语句的参数集合
            List<String> parameterMappingList = boundSql.getParameterMappingList();
            if (parameterClass != null){
                if ("java.lang.Long".equals(parameterClass.getName()) ||
                        "java.lang.Integer".equals(parameterClass.getName())){
                    preparedStatement.setObject(1,args[0]);
                }else {
                    for (int i = 0; i < parameterMappingList.size(); i++) {
                        String content = parameterMappingList.get(i);
                        //获取参数的字段
                        Field field = parameterClass.getDeclaredField(content);
                        field.setAccessible(true);
                        //得到每个字段的类
                        Object data = field.get(paramsList.get(i));
                        //设置进preparedStatement
                        preparedStatement.setObject(i+1,data);
                    }
                }
            }


            //执行sql
            ResultSet resultSet = null;
            if (sql.split(" ")[0].equals("update") ||
                    sql.split(" ")[0].equals("delete") ||
                    sql.split(" ")[0].equals("insert")) {
                Integer result = preparedStatement.executeUpdate();
                List<Integer> resultList = new ArrayList<>();
                resultList.add(result);
                return (List<E>) resultList;
            }else{
                resultSet = preparedStatement.executeQuery();
                //获取返回值类型
                Class<?> resultClass = getClass(mappedStatement.getResultType());
                List<Object> list = new ArrayList<>();
                ResultSetMetaData metaData = resultSet.getMetaData();
                while (resultSet.next()){
                    //获取返回对象的实例
                    Object instance = resultClass.getConstructor().newInstance();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = resultSet.getObject(columnName);
                        //属性封装
                        //根据反射根据数据库表和实体类的属性和字段的对应的关系数据库
                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultClass);
                        //获取对应字段的set方法
                        Method writeMethod = propertyDescriptor.getWriteMethod();
                        //使用set将instance里的columnName字段设置为object
                        writeMethod.invoke(instance, value);
                    }
                    list.add(instance);
                }
                return (List<E>) list;
            }
        }catch (InvalidException e){
            System.out.println("e = " + e);
        }
        return List.of();
    }

    public Class<?> getClass(String className) throws ClassNotFoundException {
        if (className != null){
            return Class.forName(className);
        }
        return null;
    }

    private final Map<Integer,Integer> indexMap = new HashMap<>();
    private Integer findPosition = 0;
    private final List<String> paramsList = new ArrayList<>();

    /**
     * 解析sql  将#{}转为?
     * @param sql 需要解析的sql
     * @return BoundSql对象
     * @throws InvalidException sql格式错误
     */
    private BoundSql getBoundSql(String sql) throws InvalidException{
        this.parseSql(sql);
        Set<Map.Entry<Integer, Integer>> entries = indexMap.entrySet();
        for (Map.Entry<Integer,Integer> entry:entries){
            int begin = entry.getKey() + 2;
            int end = entry.getValue();
            paramsList.add(sql.substring(begin,end));
        }
        for (String s:paramsList){
            sql = sql.replace("#{"+s+"}","?");
        }
        return new BoundSql(sql,paramsList);
    }

    /**
     * 获取每个 #{ 和 } 的索引位置
     * @param sql sql语句
     * @throws InvalidException sql格式错误
     */
    private void parseSql(String sql) throws InvalidException {
        if (StringUtils.isBlank(sql)){
            return;
        }
        int start = sql.indexOf("#{",findPosition);
        if (start != -1){
            int end = sql.indexOf("}",findPosition);
            if (end == -1){
                throw new InvalidException("error sql");
            }else{
                indexMap.put(start,end);
                findPosition = end+1;
                parseSql(sql);
            }
        }
    }
}
