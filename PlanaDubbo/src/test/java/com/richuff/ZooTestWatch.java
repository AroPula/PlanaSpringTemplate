package com.richuff;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZooTestWatch {

    private CuratorFramework client;

    @Before
    public void Test(){
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(3000, 10);
        CuratorFramework nClient = CuratorFrameworkFactory.builder()
                .connectString("192.168.3.133:2181")
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(retry).build();
        client = nClient;
        nClient.start();
    }

//    @Test
//    public void testWatch() throws Exception {
////        CuratorCache cache = CuratorCache.build(client, "/father/son/grandson1",
////                CuratorCache.Options.SINGLE_NODE_CACHE);
////
//        CuratorCacheListener listener = CuratorCacheListener.builder()
//                .forPathChildrenCache("/father/son/grandson1", client, (client, event) -> {
//                    System.out.println(event);
//                })
//                .build();
//        final NodeCache nodeCache = new NodeCache(client,"/app");
//        nodeCache.getListenable().addListener(()->{
//            byte[] data = nodeCache.getCurrentData().getData();
//            System.out.println(new String(data));
//        });
//        nodeCache.start();
//        while (true){
//
//        }
//    }

    @Test
    public void PathChildrenCacheTest() throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client,"/app",true);
        pathChildrenCache.getListenable().addListener((client,event)->{
            PathChildrenCacheEvent.Type type = event.getType();
            if (type.equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)){
                byte[] data = event.getData().getData();
                System.out.println(new String(data));
            }
        });
        pathChildrenCache.start();
        while (true){

        }
    }

    @Test
    public void TreeCacheTest() throws Exception {
        TreeCache treeCache = new TreeCache(client,"/app");
        treeCache.getListenable().addListener((client,event)->{
            byte[] data = event.getData().getData();
            System.out.println(new String(data));
        });
        treeCache.start();
        while (true){

        }
    }

    @After
    public void testClose(){
        if (client != null){
            client.close();
        }
    }
}
