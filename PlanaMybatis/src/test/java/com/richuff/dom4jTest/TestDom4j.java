package com.richuff.dom4jTest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestDom4j {
    @Test
    public void Dom4jTest()  {
        SAXReader reader = new SAXReader();
        File file = new File("D:\\Mygit\\PlanaSpringTemplate\\PlanaMybatis\\src\\test\\resources\\Book.xml");
        try {
            Document read = reader.read(file);
            Element rootElement = read.getRootElement();
            List<Element> elements = rootElement.elements();
            for(Element element:elements){
                System.out.println("element.attribute(\"id\") = " + element.attribute("id").getValue());
                List<Element> childElements = element.elements();
                for (Element childElement:childElements){
                    System.out.print("Name() = " + childElement.getName() + "\t");
                    System.out.println("Text() = " + childElement.getText() + "\t");
                }
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
