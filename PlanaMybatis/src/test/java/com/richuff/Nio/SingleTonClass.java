package com.richuff.Nio;

public class SingleTonClass {
    private volatile static SingleTonClass singleTonClass;
    public SingleTonClass() {
    }
    public static SingleTonClass getInstance(){
        if (singleTonClass == null){
            synchronized (SingleTonClass.class){
                if (singleTonClass == null){
                    singleTonClass = new SingleTonClass();
                }
            }
        }
        return singleTonClass;
    }

    public static SingleTonClass getInstance1(){
        return InnerClass.singleTonClass;
    }

    private static class InnerClass{
        private static final SingleTonClass singleTonClass = new SingleTonClass();
    }
}
