package com.vino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RedisLockApplication {

    @Autowired
    DistributedLock distributedLock;
    // 模拟库存
    private long goodsNum = 10;

    public static void main(String[] args) {
        SpringApplication.run(RedisLockApplication.class, args);
    }

    @GetMapping("/redisLock")
    public String testRedisLock() {
        //设置个Key
        String key = "ORDER_ID";
        try {
            long expireTime = distributedLock.tryLock(DistributedLock.LOCK_PREFIX + key);
            // 当获得锁，减少库存，同时释放锁
            if (expireTime != 0) {
                goodsNum = goodsNum - 1;
                distributedLock.unlock(DistributedLock.LOCK_PREFIX + key, expireTime);
                System.out.println("goodsNum: " + goodsNum);
                return "success";
            }
        } catch (Exception e) {
            System.out.println("获取锁异常");
        }
        return "false";
    }
}
