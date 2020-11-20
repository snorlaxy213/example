package com.vino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * redis实现的distributed lock ,锁占用时间不宜过长
 */
@Component
public class DistributedLock {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //锁名称前缀
    public static final String LOCK_PREFIX = "redis_lock";

    //单位毫秒
    private long lockTimeOut = 2000;
    private long perSleep;

    /**
     * 得不到锁立即返回，得到锁返回设置的超时时间
     *
     * @param key
     * @return
     */
    public long tryLock(String key) {

        //得到锁后设置的过期时间，未得到锁返回0
        long expireTime = System.currentTimeMillis() + lockTimeOut + 1;
        Boolean acquire = redisTemplate.opsForValue().setIfAbsent(key, expireTime);
        if (acquire) {
            //得到了锁返回
            return expireTime;
        } else {
            String curLockTimeStr = (String) redisTemplate.opsForValue().get(key);

            //判断是否过期
            if (StringUtils.isEmpty(curLockTimeStr)
                    || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
                expireTime = System.currentTimeMillis() + lockTimeOut + 1;

                curLockTimeStr = (String) redisTemplate.opsForValue().getAndSet(key, expireTime);
                //仍然过期,则得到锁
                if (StringUtils.isEmpty(curLockTimeStr)
                        || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
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
     * @param key
     * @return
     * @throws InterruptedException
     */
    public long lock(String key) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long sleep = (perSleep == 0 ? lockTimeOut / 10 : perSleep);
        //得到锁后设置的过期时间，未得到锁返回0
        long expireTime = 0;
        while (true) {
            expireTime = System.currentTimeMillis() + lockTimeOut + 1;
            if (redisTemplate.opsForValue().setIfAbsent(key, expireTime)) {
                //得到了锁返回
                return expireTime;
            } else {
                String curLockTimeStr = (String) redisTemplate.opsForValue().get(key);
                //判断是否过期
                if (StringUtils.isEmpty(curLockTimeStr)
                        || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
                    expireTime = System.currentTimeMillis() + lockTimeOut + 1;

                    curLockTimeStr = (String) redisTemplate.opsForValue().getAndSet(key, expireTime);
                    //仍然过期,则得到锁
                    if (StringUtils.isEmpty(curLockTimeStr)
                            || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
                        return expireTime;
                    } else {
                        Thread.sleep(sleep);
                    }
                } else {
                    Thread.sleep(sleep);
                }
            }
            if (lockTimeOut > 0
                    && ((System.currentTimeMillis() - startTime) >= lockTimeOut)) {
                expireTime = 0;
                return expireTime;
            }
        }
    }

    /**
     * 先判断自己运行时间是否超过了锁设置时间，是则不用解锁
     *
     * @param key
     * @param expireTime
     */
    public void unlock(String key, long expireTime) {
        if (System.currentTimeMillis() - expireTime > 0) {
            return;
        }
        String curLockTimeStr = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(curLockTimeStr) && Long.valueOf(curLockTimeStr) > System.currentTimeMillis()) {
            redisTemplate.delete(key);
        }
    }
}