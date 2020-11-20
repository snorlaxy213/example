package com.vino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RedisLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisLockApplication.class, args);
    }

    @Autowired
    DistributedLock distributedLock;

    @GetMapping("/redisLock")
    public String testRedisLock() {
        //设置个Key
        String key = "order_id";
        try {
            long expireTime = distributedLock.lock(DistributedLock.LOCK_PREFIX + key);
        } catch (Exception e) {
            System.out.println("获取锁异常");
        }
        return "success";
    }
}
