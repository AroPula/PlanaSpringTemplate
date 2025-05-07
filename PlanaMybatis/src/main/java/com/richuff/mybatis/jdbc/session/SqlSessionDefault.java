package com.richuff.mybatis.jdbc.session;

import com.richuff.mybatis.config.Configuration;
import com.richuff.mybatis.executor.SimpleExecutor;
import com.richuff.mybatis.type.MappedStatement;

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
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statement);
        return simpleExecutor.query(configuration, mappedStatement, params);
    }

    @Override
    public <E> E selectOne(String statement, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statement);
        if (params[0].getClass() == Long.class){
            simpleExecutor.query(configuration, mappedStatement, params);
        } else if (params[0].getClass() == Integer.class) {
            simpleExecutor.query(configuration, mappedStatement, params);
        }
        List<Object> querys = selectList(statement, params);
        if (querys.size() == 1){
            return (E) querys.get(0);
        } else if (!querys.isEmpty()) {
            throw new RuntimeException("查询结果不唯一");
        }else {
            return null;
        }
    }

    @Override
    public <E> E insert(String statement, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statement);
        List<Object> list = simpleExecutor.query(configuration, mappedStatement, params);
        if (!list.isEmpty()){
            return (E) list.get(0);
        }else{
            return (E) "0";
        }
    }

    @Override
    public <E> E update(String statement, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statement);
        List<Object> list = simpleExecutor.query(configuration, mappedStatement, params);
        if (!list.isEmpty()){
            return (E) list.get(0);
        }else{
            return (E) "0";
        }
    }

    @Override
    public <E> E delete(String statement, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statement);
        List<Object> list = simpleExecutor.query(configuration, mappedStatement, params);
        if (!list.isEmpty()){
            return (E) list.get(0);
        }else{
            return (E) "0";
        }
    }

    /**
     * 获取mapper对象
     * @param clazz 接口类
     * @return mapper对象
     * @param <T> 泛型
     */
    @Override
    public <T> Object getMapper(Class<T> clazz) {
        ClassLoader cl = clazz.getClassLoader();
        return Proxy.newProxyInstance(cl, new Class[]{clazz}, (proxy, method, args) -> {
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
                return selectList(statementId, args);
            }else{
                return selectOne(statementId, args);
            }
        });
    }
}
