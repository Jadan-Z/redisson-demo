package com.jadan.demo.redisson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * 公平锁
 * @Author Jadan-Z
 * @Date 2019/11/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FairLockTests {
    private Logger log = LoggerFactory.getLogger(FairLockTests.class);
    @Autowired
    private RedissonClient redissonClient;

    // 它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，
    // 当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。
    @Test
    public void failLock(){
        RLock fairLock = redissonClient.getFairLock("exchangetask:" + "taskstatus:" + 136250);
        boolean locked = fairLock.isLocked();
        if (locked) {
            log.info("还锁着呢...");
        } else {
            // 加锁：默认30s过期
//            fairLock.lock();

            // 指定过期时间：10s
//            fairLock.lock(10, TimeUnit.SECONDS);

            try {
                // 尝试加锁，最多等待2s，加锁3s后过期
                boolean tryLock = fairLock.tryLock(2, 3, TimeUnit.SECONDS);
                if(!tryLock) {
                    log.info("尝试加锁失败");
                    return;
                }
                log.info("尝试加锁：{}", tryLock == true? "success": "error");
            } catch (InterruptedException e) {
                log.info("加锁中...");
            }
            log.info("加锁成功");
        }

//        locked = fairLock.isLocked();
//        log.info("锁的状态：{}", locked == true? "还锁着呢": "已解锁");
//        fairLock.unlock();
//        locked = fairLock.isLocked();
//        log.info("锁的状态：{}", locked == true? "还锁着呢": "已解锁");
    }
}
