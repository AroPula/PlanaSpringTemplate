package com.richuff;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Ticket implements Runnable{
    private Integer number = 10;

    private InterProcessMutex lock;

    public Ticket(){
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(3000, 10);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(retry).build();
        client.start();
        lock = new InterProcessMutex(client,"/lock");
    }

    @Override
    public void run() {
        while (true){
            //加锁
            try {
                lock.acquire(3, TimeUnit.SECONDS);
                if (number > 0){
                    System.out.println("number = " + number);
                    number--;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }finally {
                //释放锁
                try {
                    lock.release();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
