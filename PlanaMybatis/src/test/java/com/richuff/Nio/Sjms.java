package com.richuff.Nio;

import com.richuff.entity.MyObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class Sjms {
    @Test
    public void TestBuilder() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        MyObject cmn = MyObject.Builder().setName("cmn").setAge(100).setEmail("123@qq.com").setPhone("123").build();
        System.out.println("cmn = " + cmn);
        Constructor<MyObject> constructor = MyObject.class.getConstructor();
        MyObject myObject = constructor.newInstance();
        System.out.println("myObject = " + myObject);
    }
}
