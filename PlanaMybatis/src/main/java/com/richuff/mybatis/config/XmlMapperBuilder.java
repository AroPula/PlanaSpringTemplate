package com.richuff.mybatis.config;

import com.richuff.mybatis.type.MappedStatement;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用dom4j解析xml配置文件
 */
public class XmlMapperBuilder {
    /**
     * 配置数据封装对象
     */
    private Configuration configuration;

    /**
     * 有参构造
     * @param configuration 配置数据封装对象
     */
    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream){
        SAXReader saxReader = new SAXReader();
        try{
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();
            String NameSpace = rootElement.attribute("namespace").getValue();
            List<Element> elements = rootElement.elements();
            for (Element element:elements){
                MappedStatement mappedStatement = new MappedStatement();
                Attribute idAttribute = element.attribute("id");
                if (idAttribute == null){
                    throw new RuntimeException("id can't be null");
                }
                mappedStatement.setId(idAttribute.getValue());

                Attribute resultTypeAttribute = element.attribute("resultType");
                if (resultTypeAttribute != null){
                    mappedStatement.setResultType(resultTypeAttribute.getValue());
                }
                Attribute parameterTypeAttribute = element.attribute("parameterType");
                if (parameterTypeAttribute != null){
                    mappedStatement.setParameterType(parameterTypeAttribute.getValue());
                }
                String sql = element.getText();
                if (!sql.isBlank()){
                    sql = sql.replace("\n", "").trim();
                    mappedStatement.setSql(sql);
                }else{
                    throw new RuntimeException("sql is empty");
                }
                configuration.getMappedStatementMap().put(NameSpace+"."+idAttribute.getValue(),mappedStatement);
            }
        }catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
