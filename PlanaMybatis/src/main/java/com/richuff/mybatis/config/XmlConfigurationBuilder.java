package com.richuff.mybatis.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.richuff.mybatis.io.Resource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XmlConfigurationBuilder {
    private Configuration configuration;

    public XmlConfigurationBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析配置文件
     * @return 配置文件对象
     */
    public Configuration parseMybatisConfiguration(InputStream inputStream){
        SAXReader reader = new SAXReader();
        try{
            Document document = reader.read(inputStream);
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.elements();
            Properties properties = new Properties();
            for (Element element:elements){
                if (element.getName().equals("dataSource")){
                    List<Element> childElements = element.elements();
                    for (Element childElement:childElements){
                        String name = childElement.attribute("name").getValue();
                        String value = childElement.attribute("value").getValue();
                        properties.setProperty(name,value);
                    }
                    //初始化一个数据库连接池
                    ComboPooledDataSource dataSource = new ComboPooledDataSource();
                    dataSource.setDriverClass(properties.getProperty("driveClass"));
                    dataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
                    dataSource.setUser(properties.getProperty("username"));
                    dataSource.setPassword(properties.getProperty("password"));
                    //设置数据库数据源
                    configuration.setDataSource(dataSource);
                }
                if (element.getName().equals("mapper")){
                    String resource = element.attribute("resource").getValue();
                    InputStream reinputStream = Resource.getResourceAsStream(resource);
                    //读取XXXmapper.xml文件
                    XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
                    xmlMapperBuilder.parse(reinputStream);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return configuration;
    }
}
