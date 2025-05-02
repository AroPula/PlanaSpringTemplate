package com.richuff.mybatis.io;

import java.io.InputStream;

public class Resource {
    /**
     * 根据配置文件路径，加载为字节流形式，并保存到内存中
     * @param path 配置文件路径
     * @return 字节流
     */
    public static InputStream getResourceAsStream(String path){
        ClassLoader cl = Resource.class.getClassLoader();
        InputStream stream = cl.getResourceAsStream(path);
        if (null == stream){
            stream = cl.getResourceAsStream("/"+path);
        }
        return stream;
    }
}
