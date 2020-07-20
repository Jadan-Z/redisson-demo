package com.jadan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Author Jadan-Z
 * @Date 2019/11/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissonTests {
    private Logger log = LoggerFactory.getLogger(RedissonTests.class);

    private final static int EFFECTIVE_DURATION = 10;

    @Autowired
    private RedissonClient redissonClient;

    // 获取redisson配置信息
    @Test
    public void getConfig(){
        Config config = redissonClient.getConfig();
    }
    // 使用:分割，便于查看

    // 获取有序集合： redissonClient.getSortedSet(objectName)

    // 获取集合: redissonClient.getSet(objectName)

    // 获取列表： redissonClient.getList(objectName)

    // 获取队列: redissonClient.getQueue(objectName)

    // 获取双端队列： redissonClient.getDeque(objectName)

    // 获取锁： redissonClient.getLock(objectName)

    // 获取读取锁: redissonClient.getReadWriteLock(objectName)

    // 获取原子数： redissonClient.getAtomicLong(objectName)

    // 获取记数锁: redissonClient.getCountDownLatch(objectName)

    // 获取消息的Topic：redissonClient.getTopic(objectName)

    /**
     * 可重入锁：
     * 如果负责储存这个分布式锁的Redisson节点宕机以后，而且这个锁正好处于锁住的状态时，这个锁会出现锁死的状态。
     * 为了避免这种情况的发生，Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。
     * 默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
     */
    @Test
    public void getLock() {
        /* */
        RLock lock = redissonClient.getLock("anyLock");
        lock.lock();
        // ...
        lock.unlock();

        /*==============================================*/
        //另外Redisson还通过加锁的方法提供了leaseTime的参数来指定加锁的时间。超过这个时间后锁便自动解开了。
        // 加锁以后10秒钟自动解锁
        // 无需调用unlock方法手动解锁
        lock.lock(10, TimeUnit.SECONDS);

        /*==============================================*/
        // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
        boolean res = false;
        try {
            res = lock.tryLock(100, 10, TimeUnit.SECONDS);
            if (res) {
                try {
                    // ...
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // RLock对象完全符合Java的Lock规范。也就是说只有拥有锁的进程才能解锁，其他进程解锁则会抛出IllegalMonitorStateException错误。
        RLock lock1 = redissonClient.getLock("anyLock");
        lock1.lockAsync();
        lock1.lockAsync(10, TimeUnit.SECONDS);
        Future<Boolean> res1 = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);

    }

    @Test
    public void t1() {
        RLock lock = redissonClient.getLock("anyLock");
        if(!lock.isLocked()) {
            lock.lock(100, TimeUnit.SECONDS);
            log.info("加锁...");
        } else {
            lock.unlock();
            log.info("解锁...");
        }
    }

    @Test
    public void t2() {
        RLock lock = redissonClient.getLock("anyLock");
        log.info("尝试解锁");
        lock.unlock();
        log.info("解锁...");
    }

    @Test
    public void t3() {
        RLock lock = redissonClient.getLock("anyLock1");
        if(!lock.isLocked()) {
//            lock.lockAsync(100, TimeUnit.SECONDS);
            lock.lockAsync();
            log.info("异步加锁");
        } else {
            RFuture<Void> voidRFuture = lock.unlockAsync();
            Void now = voidRFuture.getNow();
            log.info("异步解锁...");
        }
    }
}





















