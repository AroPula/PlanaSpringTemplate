package com.richuff;

import com.richuff.anno.*;
import com.richuff.entity.BeanDefinition;
import com.richuff.config.BeanNameWare;
import com.richuff.config.BeanPostProcessor;
import com.richuff.config.InitializingBean;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class PlanaSpringContext {
    private final ConcurrentHashMap<String,Object> singletonMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,BeanDefinition> beanDefinitionHashMap = new ConcurrentHashMap<>();
    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public PlanaSpringContext(Class<?> configClass) {
        //扫描Component
        ScanComponent(configClass);
        //遍历beanDefinitionHashMap,如果为单例的话提前创建好放入单例Map内
        for (Map.Entry<String,BeanDefinition> entry: beanDefinitionHashMap.entrySet()){
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = beanDefinitionHashMap.get(beanName);
            //为单例
            if (beanDefinition.getScope().equals("singleton")){
                //创建bean
                Object bean = createBean(beanName,beanDefinition);
                //放入单例Map中
                singletonMap.put(beanName,bean);
            }
        }
    }

    private void ScanComponent(Class<?> configClass) {
        //获取有ComponentScan注解的类
        ComponentScan componentScan = configClass.getDeclaredAnnotation(ComponentScan.class);
        //获取注解的内容
        String  servicePath = componentScan.value();
        servicePath = servicePath.replace('.','/');
        //获取config类的类加载器
        ClassLoader loader = configClass.getClassLoader();
        //通过类加载器获取指定目录下的资源
        URL resource = loader.getResource(servicePath);
        if (resource != null){
            File dir = new File(resource.getFile());
            if(dir.isDirectory()){
                //扫描所有带Component的注解
                ScanFile(dir,loader);
            }else{
                throw new RuntimeException("this is not a file");
            }
        }else{
            throw new RuntimeException("no resource");
        }
    }

    public Object getBean(String beanName){
        if (beanDefinitionHashMap.containsKey(beanName)){
            BeanDefinition beanDefinition = beanDefinitionHashMap.get(beanName);
            String scope = beanDefinition.getScope();
            if (Objects.equals(scope,"singleton")){
                //为单例直接返回单例Map的bean
                return singletonMap.get(beanName);
            }else{
                //不为单例，创建一个bean
                return createBean(beanName,beanDefinition);
            }
        }else {
            throw new RuntimeException("no keys");
        }
    }

    public Object createBean(String beanName,BeanDefinition beanDefinition){
        Class<?> clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            //获取指定类中声明的所有字段（fields）
            for(Field field : clazz.getDeclaredFields()){
                //如果这个自段有Autowired注解
                if(field.isAnnotationPresent(Autowired.class)){
                    //根据字段的名字得到bean
                    Object bean = getBean(field.getName());
                    field.setAccessible(true);
                    //将instance的field设置为bean
                    field.set(instance,bean);
                }
            }
            //如果实现了BeanNameWare接口，就将BeanName返回
            if (instance instanceof BeanNameWare){
                ((BeanNameWare)instance).setBeanName(beanName);
            }
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            }
            //初始化
            if (instance instanceof InitializingBean){
                try{
                    ((InitializingBean)instance).afterPropertiesSet();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance,beanName);
            }
            return instance;
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException
                e) {
            e.printStackTrace();
        }
        return null;
    }

    public void ScanFile(File file,ClassLoader loader){
        File[] files = file.listFiles();
        if (files != null){
            for (File mfile: files){
                if (mfile.isDirectory()){
                    ScanFile(mfile,loader);
                }else{
                    String fileName = mfile.getAbsolutePath();
                    String className = fileName.substring(fileName.indexOf("com"),fileName.indexOf(".class"));
                    className = className.replace("\\",".");

                    try {
                        Class<?> clazz = loader.loadClass(className);
                        if (BeanPostProcessor.class.isAssignableFrom(clazz)){
                            BeanPostProcessor instance = (BeanPostProcessor)clazz.getDeclaredConstructor().newInstance();
                            beanPostProcessorList.add(instance);
                        }
                        //判断是不是Bean
                        if (clazz.isAnnotationPresent(Component.class)){
                            Component component = clazz.getDeclaredAnnotation(Component.class);
                            String beanName = component.value();
                            //设置 BeanDefinition
                            BeanDefinition beanDefinition = getBeanDefinition(clazz);
                            beanDefinitionHashMap.put(beanName,beanDefinition);
                        }
                    }catch (ClassNotFoundException |
                            InvocationTargetException |
                            IllegalAccessException |
                            InstantiationException |
                            NoSuchMethodException
                            e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static BeanDefinition getBeanDefinition(Class<?> clazz) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setClazz(clazz);
        //有scope注解则为scope的值，否则为单例模式
        if (clazz.isAnnotationPresent(Scope.class)){
            Scope scope = clazz.getDeclaredAnnotation(Scope.class);
            beanDefinition.setScope(scope.value());
        }else{
            beanDefinition.setScope("singleton");
        }
        return beanDefinition;
    }
}
