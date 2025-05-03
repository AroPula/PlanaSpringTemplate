package com.richuff.mybatis.jdbc.session;

import com.richuff.mybatis.config.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

public class SqlSessionDefault implements SqlSession{
    public Configuration configuration;

    public SqlSessionDefault(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statement, Object... params) throws Exception {
        return List.of();
    }

    @Override
    public <E> E selectOne(String statement, Object... params) throws Exception {
        return null;
    }

    @Override
    public <E> E insert(String statement, Object... params) throws Exception {
        return null;
    }

    @Override
    public <E> E update(String statement, Object... params) throws Exception {
        return null;
    }

    @Override
    public <E> E delete(String statement, Object... params) throws Exception {
        return null;
    }

    /**
     * 获取mapper对象
     * @param clazz 接口类
     * @return mapper对象
     * @param <T> 泛型
     */
    @Override
    public <T> T getMapper(Class<T> clazz) {
        ClassLoader cl = clazz.getClassLoader();
        Object o = Proxy.newProxyInstance(cl, new Class[]{clazz}, (proxy, method, args) -> {
            //获取接口方法名
            String methodName = method.getName();
            //获取接口全类名
            String ClassName = method.getDeclaringClass().getName();
            //id
            String statementId = ClassName+"."+methodName;
            //获取返回的类型
            Type returnType = method.getGenericReturnType();
            if (ClassName.contains("insert")){
                return insert(statementId,args);
            } else if (ClassName.contains("update")) {
                return update(statementId,args);
            } else if (ClassName.contains("delete")) {
                return delete(statementId,args);
            }
            if (returnType instanceof ParameterizedType){
                List<Object> objects = selectList(statementId, args);
                return objects;
            }else{
                return selectOne(statementId, args);
            }
        });
        return (T)o;
    }
}
