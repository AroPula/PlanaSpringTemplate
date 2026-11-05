package com.richuff.Nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
public class NioTest {
    @Test
    public void mainTest() throws IOException {
        Path path = Paths.get("D:\\Mygit\\PlanaSpringTemplate\\PlanaMybatis\\src\\test\\java\\com\\richuff\\Nio\\test.txt");
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)){
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (channel.read(buffer) > 0){
                buffer.flip();
                while (buffer.hasRemaining()){
                    System.out.println("char:"+buffer.get());
                }
                buffer.clear();
            }
        }
    }

    private final ConcurrentHashMap<Integer,Integer> hashMap = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    public void innerMethod(){
        lock.lock();
        try {
            System.out.println("inner");
        }finally {
            lock.unlock();
        }
    }

    public void outerMethod(){
        lock.lock();
        try {
            System.out.println("outer");
            innerMethod();
        }finally {
            lock.unlock();
        }
    }

    @Test
    public void TestLock(){
        outerMethod();
    }

    private final ExecutorService service = Executors.newFixedThreadPool(16);
    private final ConcurrentLinkedDeque<Object> linkedDeque = new ConcurrentLinkedDeque<>();

    @Test
    public void TestSingle() throws Exception{
        for (int i = 0; i < 10; i++) {
            service.submit(()->{
                SingleTonClass singleTonClass = SingleTonClass.getInstance();
                linkedDeque.add(singleTonClass);
            });
        }
        linkedDeque.forEach(System.out::println);
    }

    private class CreateInstance implements Runnable{
        @Override
        public void run() {
            SingleTonClass singleTonClass = SingleTonClass.getInstance();
            SingleTonClass singleTonClass1 = SingleTonClass.getInstance();
            System.out.println(singleTonClass1.equals(singleTonClass));
            System.out.println(singleTonClass);
            System.out.println(singleTonClass1);
        }
    }

    @Test
    public void TestSingle1() throws Exception{
        for (int i = 0; i < 10; i++) {
            service.submit(()->{
                SingleTonClass singleTonClass = SingleTonClass.getInstance1();
                linkedDeque.add(singleTonClass);
            });
        }
        Thread.sleep(3000);
        linkedDeque.forEach(System.out::println);
    }

    private Integer count = 0;

    @Test
    public void TestCount() throws Exception{
        long startTime =  System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
                service.submit(()->{
                    synchronized (SingleTonClass.class){
                        count++;
                    }
                });
        }
        Thread.sleep(3000);
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(count);
    }

    private final AtomicInteger aCount = new AtomicInteger(0);

    @Test
    public void TestACount() throws Exception{
        long startTime =  System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            service.submit(()->{
                aCount.addAndGet(1);
            });
        }
        System.out.println(System.currentTimeMillis() - startTime);
        Thread.sleep(3000);
        System.out.println(aCount);
    }

    private final HashMap<Integer,HashKey> map = new HashMap<>();
    @Test
    public void TestMap(){
        int h = 0;
        int code = 0;
        for (int i = 0; i < 10; i++) {
            HashKey hashKey = new HashKey();
            System.out.println(code = (h = hashKey.hashCode()) ^ (h >>> 16));
            System.out.println((map.size() -1) & code);
            map.put(i,hashKey);
            if (i == 9){
                System.out.println((map.size() - 1) & code);
            }
        }
    }

    private final HashSet<Integer> set = new HashSet<>();
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Test
    public void TestSet() throws InterruptedException{
        for (int i = 0; i < 10; i++) {
            executorService.submit(()->{
                int h;
                HashKey hashKey = new HashKey();
                int code = (set.size() - 1) & ((h = hashKey.hashCode()) ^ (h >>> 16));
                set.add(code);
            });
        }
        Thread.sleep(3000);
        Arrays.stream(set.toArray()).forEach(System.out::println);
    }

    private final List<String> array = new ArrayList<>(Arrays.asList("1","2","3","4"));

    @Test
    public void TestArray(){
        List<String> newArray = array.stream().filter(s-> !s.equals("2")).toList();
        System.out.println(newArray.toString());
    }

    private static class HashKey{
        public HashKey() {
        }
    }

    private class C implements A,B{
        @Override
        public void f() {
            System.out.println("test");
        }
    }
    ExecutorService MyexecutorService = new ThreadPoolExecutor(
            4, 8,
            10000, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

}

interface A{
    void f();
}

interface B{
    void f();
}