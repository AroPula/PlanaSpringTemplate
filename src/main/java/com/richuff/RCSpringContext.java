package com.richuff;

import com.richuff.anno.Component;
import com.richuff.anno.ComponentScan;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public class RCSpringContext {
    private Class<?> configClass;

    public RCSpringContext(Class<?> configClass) {
        this.configClass = configClass;
    }

    public Object getBean(String beanName){
        ComponentScan componentScan = configClass.getDeclaredAnnotation(ComponentScan.class);
        String  servicePath = componentScan.value();
        servicePath = servicePath.replace('.','/');
        ClassLoader loader = configClass.getClassLoader();
        URL resource = loader.getResource(servicePath);
        if (resource != null){
            File dir = new File(resource.getFile());
            if(dir.isDirectory()){
                try {
                    return AllFile(dir,loader,beanName);
                }catch (ClassNotFoundException e){
                    e.getException();
                }
            }else{
                throw new RuntimeException("this is not a file");
            }
        }else{
            throw new RuntimeException("no resource");
        }
        return null;
    }

    public Class<?> AllFile(File file,ClassLoader loader,String beanName) throws ClassNotFoundException{
        File[] files = file.listFiles();
        if (files != null){
            for (File mfile: files){
                if (mfile.isDirectory()){
                    AllFile(mfile,loader,beanName);
                }else{
                    String fileName = mfile.getAbsolutePath();
                    String className = fileName.substring(fileName.indexOf("com"),fileName.indexOf(".class"));
                    className = className.replace("\\",".");

                    Class<?> clazz = loader.loadClass(className);
                    if (clazz.isAnnotationPresent(Component.class)){
                        Component component = clazz.getDeclaredAnnotation(Component.class);
                        if (Objects.equals(component.value(), beanName)){
                            return clazz;
                        }
                    }
                }
            }
        }
        return null;
    }
}
