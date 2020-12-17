package com.vino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * redis实现的distributed lock ,锁占用时间不宜过长
 *
 * 实现方式：使用 Redis SETNX。
 * 只在键 key 不存在的情况下， 将键 key 的值设置为 value 。
 * 若键 key 已经存在， 则 SETNX 命令不做任何动作。
 * SETNX 是『SET if Not exists』(如果不存在，则 SET)的简写。
 */
@Component
public class DistributedLock {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //锁名称前缀
    public static final String LOCK_PREFIX = "REDIS_LOCK_";
    //单位毫秒
    private final long lockTimeOut = 200;

    private long perSleep;

    /**
     * 得不到锁立即返回，得到锁返回设置的超时时间
     *
     * @param key 主键
     * @return 过期时间
     *
     */
    public long tryLock(String key) {

        //得到锁后设置的过期时间，未得到锁返回0
        long expireTime = System.currentTimeMillis() + lockTimeOut + 1;
        boolean acquire = redisTemplate.opsForValue().setIfAbsent(key, expireTime);
        if (acquire) {
            //得到了锁返回
            return expireTime;
        } else {
            Long curLockTime = (Long) redisTemplate.opsForValue().get(key);

            //判断是否过期
            if (StringUtils.isEmpty(curLockTime) || System.currentTimeMillis() > curLockTime) {
                expireTime = System.currentTimeMillis() + lockTimeOut + 1;
                curLockTime = (Long) redisTemplate.opsForValue().getAndSet(key, expireTime);
                //仍然过期,则得到锁
                if (StringUtils.isEmpty(curLockTime) || System.currentTimeMillis() > curLockTime) {
                    return expireTime;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
    }

    /**
     * 得到锁返回设置的超时时间，得不到锁等待
     *
     * @param key 主键
     * @return 过期时间
     *
     */
    public long lock(String key) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long sleep = (perSleep == 0 ? lockTimeOut / 10 : perSleep);
        //得到锁后设置的过期时间，未得到锁返回0
        long expireTime = 0;
        while (true) {
            expireTime = System.currentTimeMillis() + lockTimeOut + 1;
            boolean acquire = redisTemplate.opsForValue().setIfAbsent(key, expireTime);
            if (acquire) {
                //得到了锁返回
                return expireTime;
            } else {
                Long curLockTime = (Long) redisTemplate.opsForValue().get(key);
                //判断是否过期
                if (StringUtils.isEmpty(curLockTime) || System.currentTimeMillis() > curLockTime) {
                    expireTime = System.currentTimeMillis() + lockTimeOut + 1;
                    curLockTime = (Long) redisTemplate.opsForValue().getAndSet(key, expireTime);
                    //仍然过期,则得到锁
                    if (StringUtils.isEmpty(curLockTime) || System.currentTimeMillis() > curLockTime) {
                        return expireTime;
                    } else {
                        Thread.sleep(sleep);
                    }
                } else {
                    Thread.sleep(sleep);
                }
            }
            if ((System.currentTimeMillis() - startTime) >= lockTimeOut) {
                expireTime = 0;
                return expireTime;
            }
        }
    }

    /**
     * 先判断自己运行时间是否超过了锁设置时间，是则不用解锁
     *
     * @param key redis key
     * @param expireTime 超时时间
     */
    public void unlock(String key, long expireTime) {
        if (System.currentTimeMillis() - expireTime > 0) {
            return;
        }
        Long curLockTime = (Long) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(curLockTime) && curLockTime > System.currentTimeMillis()) {
            redisTemplate.delete(key);
        }
    }
}