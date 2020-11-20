package com.vino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestRunnable implements Runnable {

    @Autowired
    DistributedLock distributedLock;

    @Override
    public void run() {
        //设置个Key
        String key = "order_id";
        try {
            long expireTime = distributedLock.lock(DistributedLock.LOCK_PREFIX + key);
        } catch (Exception e) {
            System.out.println("获取锁异常");
        }
    }
}