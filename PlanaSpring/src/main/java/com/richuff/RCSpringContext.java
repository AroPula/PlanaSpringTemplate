package com.richuff;

import com.richuff.entity.BeanDefinition;
import com.richuff.anno.Component;
import com.richuff.anno.ComponentScan;
import com.richuff.anno.Scope;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class RCSpringContext {
    private Class<?> configClass;

    private ConcurrentHashMap<String,Object> singletonMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionHashMap = new ConcurrentHashMap<>();

    public RCSpringContext(Class<?> configClass) {
        this.configClass = configClass;

        ScanComponent(configClass);

        for (Map.Entry<String,BeanDefinition> entry: beanDefinitionHashMap.entrySet()){
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = beanDefinitionHashMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")){
                Object bean = createBean(beanDefinition);
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
                return singletonMap.get(beanName);
            }else{
                return createBean(beanDefinition);
            }
        }else {
            throw new RuntimeException("no keys");
        }
    }

    public Object createBean(BeanDefinition beanDefinition){
        Class<?> clazz = beanDefinition.getClazz();
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
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
                        //判断是不是Bean
                        if (clazz.isAnnotationPresent(Component.class)){
                            Component component = clazz.getDeclaredAnnotation(Component.class);
                            String beanName = component.value();

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(clazz);
                            if (clazz.isAnnotationPresent(Scope.class)){
                                Scope scope = clazz.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scope.value());
                            }else{
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionHashMap.put(beanName,beanDefinition);
                        }
                    }catch (ClassNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
