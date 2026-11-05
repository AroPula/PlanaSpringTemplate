package com.richuff;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ZooTest {

    private CuratorFramework client;

    @Before
    public void Test(){
        //失败一次休眠3秒，最多重试3次
//        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(3000, 10);
//
//         client =
//                CuratorFrameworkFactory.newClient("192.168.3.133:2181", 60 * 1000, 15 * 1000, retry);
//        //开启服务端
//        client.start();
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(3000, 10);
        CuratorFramework nClient = CuratorFrameworkFactory.builder()
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .connectString("192.168.3.133:2181")
                .retryPolicy(retry).build();
        client = nClient;
        nClient.start();
    }

    @Test
    public void testCreate() throws Exception {
        String path = client.create().withMode(CreateMode.CONTAINER).forPath("/app1");
        System.out.println(path);
    }

    @Test
    public void testGet() throws Exception {
        byte[] bytes = client.getData().forPath("/app1");
        System.out.println(new String(bytes));
    }

    @Test
    public void setData() throws Exception {
        //删除单个节点
        client.delete().forPath("/app1");
        //删除节点及以下所有子节点
        client.delete().deletingChildrenIfNeeded().forPath("/app1");
        //必须删除成功
        client.delete().guaranteed().forPath("/app1");
        //回调
        client.delete().guaranteed().inBackground((client,event)->{
            System.out.println("event = " + event);
        }).forPath("/app1");
    }

    @Test
    public void deleteData() throws Exception {
        Stat status = new Stat();
        client.getData().storingStatIn(status).forPath("/app1");

        int version = status.getAversion();
        client.setData().withVersion(version).forPath("/app1","test".getBytes());
    }

    @Test
    public void aTest(){
    }

    @After
    public void testClose(){
        if (client != null){
            client.close();
        }
    }
}
