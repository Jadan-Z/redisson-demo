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
 * @Author Jadan-Z
 * @Date 2019/11/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReentrantLockTests {
    Logger log = LoggerFactory.getLogger(ReentrantLockTests.class);

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void t1() throws InterruptedException {
        // 获取节点的锁
        RLock lock = redissonClient.getLock("com:redisson:test1");
        // 获取锁的状态
        boolean locked = lock.isLocked();
        if (!locked) {
            // 直接加锁：默认时间 30s
//            lock.lock();

            // 尝试加锁
//            boolean tryLock = lock.tryLock();
            // 最后等待2s，3s过期
//            boolean tryLock = lock.tryLock(2, 30, TimeUnit.SECONDS);
//            log.info("尝试加锁：{}", tryLock == true? "成功": "error");
            // 加锁100s自动过期
            lock.lock(100, TimeUnit.SECONDS);
            log.info("加锁成功....");
        } else {
            log.info("还锁着呢....");
        }
        locked = lock.isLocked();
        log.info("锁的状态：{}", locked == true? "锁住了": "已解锁");
        lock.unlock();
        log.info("手动解锁....");
        locked = lock.isLocked();
        log.info("锁的状态：{}", locked == true? "锁住了": "已解锁");
    }


}
