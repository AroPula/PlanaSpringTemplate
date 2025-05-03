package com.richuff.mybatis.jdbc.session;

import java.util.List;

public interface SqlSession{
    /**
     * 批量查询
     * @param statement sql唯一标识
     * @param params 传入的sql参数
     * @return 返回对象
     * @param <E> 需要转换的对象
     * @throws Exception 方法执行的异常
     * @author richu
     */
    <E> List<E> selectList(String statement,Object... params) throws Exception;
    /**
     * 查询单个
     * @param statement sql唯一标识
     * @param params 传入的sql参数
     * @return 返回对象
     * @param <E> 需要转换的对象
     * @throws Exception 方法执行的异常
     * @author richu
     */
    <E> E selectOne(String statement,Object... params) throws Exception;

    /**
     * 新增
     * @param statement sql唯一标识
     * @param params 传入的sql参数
     * @return 返回对象
     * @param <E> 需要转换的对象
     * @throws Exception 方法执行的异常
     * @author richu
     */
    <E> E insert(String statement,Object... params) throws Exception;

    /**
     * 更新
     * @param statement sql唯一标识
     * @param params 传入的sql参数
     * @return 返回对象
     * @param <E> 需要转换的对象
     * @throws Exception 方法执行的异常
     * @author richu
     */
    <E> E update(String statement,Object... params) throws Exception;

    /**
     * 删除
     * @param statement sql唯一标识
     * @param params 传入的sql参数
     * @return 返回对象
     * @param <E> 需要转换的对象
     * @throws Exception 方法执行的异常
     * @author richu
     */
    <E> E delete(String statement,Object... params) throws  Exception;

    /**
     * 为mapper实现动态代理，实现接口类
     * @param clazz 接口类
     * @return 实现动态代理的mapper
     * @param <T> 泛型
     * @throws Exception 方法执行的异常
     * @author richu
     */
    <T> T getMapper(Class<T> clazz) throws Exception;
}
