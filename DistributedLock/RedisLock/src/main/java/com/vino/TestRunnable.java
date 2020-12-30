package com.vino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestRunnable implements Runnable {

    @Autowired
    DistributedLock distributedLock;

    // 模拟库存
    private long goodsNum = 10;

    @Override
    public void run() {
        //设置个Key
        String key = "order_id";
        try {
            long expireTime = distributedLock.lock(DistributedLock.LOCK_PREFIX + key);
            // 当获得锁，减少库存，同时释放锁
            if (expireTime != 0) {
                goodsNum = goodsNum - 1;
                distributedLock.unlock(DistributedLock.LOCK_PREFIX + key, expireTime);
                System.out.println("goodsNum: " + goodsNum);
            }
        } catch (Exception e) {
            System.out.println("获取锁异常");
        }
    }
}