package com.richuff.mybatis.jdbc.session;

import com.richuff.mybatis.config.Configuration;
import com.richuff.mybatis.config.XmlConfigurationBuilder;

import java.io.InputStream;

/**
 * @implNote SqlSessionFactory的Builder
 * @author richu
 * @version 1.0
 */
public class SqlSessionFactoryBuilder {
    /**
     * 获取sqlSessionFactory
     * @param inputStream 传入的输入流，mybatis配置文件的流
     * @return sqlSessionFactory
     */
    public SqlSessionFactory build(InputStream inputStream){
        //创建xml解析对象
        Configuration configuration = new Configuration();
        XmlConfigurationBuilder builder = new XmlConfigurationBuilder(configuration);
        //解析mybatis的配置文件
        builder.parseMybatisConfiguration(inputStream);
        //创建sqlSessionFactory
        return new SqlSessionFactoryDefault(configuration);
    }

}
