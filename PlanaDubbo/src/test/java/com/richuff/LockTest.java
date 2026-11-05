package com.richuff;

import org.junit.Test;

import java.time.LocalDateTime;


public class LockTest {
    @Test
    public void TestBuy() throws InterruptedException {
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();

        Thread t1 = new Thread(ticket1,"t1");
        Thread t2 = new Thread(ticket2,"t2");

        t1.start();
        t2.start();

        Thread.sleep(100000);
    }

    @Test
    public void TestDate(){
        Long dayTime = 60 * 60 * 24L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusDays(dayTime * 15);

        LocalDateTime time1 = now.plusDays(13 * dayTime);
        if (time1.isAfter(deadline)){
            System.out.println("提前:"+deadline.minusSeconds(7 * dayTime));
        }else{
            System.out.println("超时");
        }
    }
}
