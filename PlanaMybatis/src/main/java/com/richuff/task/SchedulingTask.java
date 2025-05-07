package com.richuff.task;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
public class SchedulingTask implements SchedulingConfigurer {
    public static String cron;

    static {
        String path = "config.xml";
        String moduleName = "notify";
        SchedulingTask.getCron(path,moduleName);
    }

    private static void getCron(String path, String moduleName) {
        ClassLoader cl = SchedulingTask.class.getClassLoader();
        InputStream stream = cl.getResourceAsStream(path);
        if (null == stream){
            stream = cl.getResourceAsStream("/"+ path);
        }
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.elements();
            for (Element element:elements){
                if (element.getName().equals("cron")){
                    String name = element.attribute("name").getValue();
                    String value = element.attribute("value").getValue();
                    if (name.equals(moduleName)){
                        cron = value;
                        return;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(()->{
            log.info("cronString= " + cron);
        },(triggerContext)->{
            CronTrigger cronTrigger = new CronTrigger(cron);
            return cronTrigger.nextExecution(triggerContext);
        });
    }
}
