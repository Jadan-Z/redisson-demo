package com.jadan.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.*;
import org.redisson.client.codec.IntegerCodec;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
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

    // 字符串类型赋值
    @Test
    public void setBucket() {
        // 无过期时间
        redissonClient.getBucket("com:jadan:test:name1").set("jadan1");  // "jadan1"
        redissonClient.getBucket("com:jadan:test:name2", StringCodec.INSTANCE).set("jadan2");  // jadan2

        // Integer类型
        redissonClient.getBucket("com:jadan:test:age", IntegerCodec.INSTANCE).set(2);

        // 设置过期时间
        redissonClient.getBucket("com:jadan:test:name3").set("jadan3", EFFECTIVE_DURATION, TimeUnit.SECONDS);  // "jadan3" 过期时间为10s
        redissonClient.getBucket("com:jadan:test:name4", StringCodec.INSTANCE).set("jadan4", EFFECTIVE_DURATION, TimeUnit.SECONDS); // jadan4 过期时间为10s
    }

    // 获取字符串对象： 怎么存怎么取，存+StringCodec.INSTANCE,取也要加，否则为null。
    @Test
    public void getBucket() {
        RBucket<Object> bucket1 = redissonClient.getBucket("com:jadan:test:name1");
        RBucket<Object> bucket2 = redissonClient.getBucket("com:jadan:test:name2", StringCodec.INSTANCE);
        RBucket<Object> bucket3 = redissonClient.getBucket("com:jadan:test:name3");
        RBucket<Object> bucket4 = redissonClient.getBucket("com:jadan:test:name4", StringCodec.INSTANCE);
        Object o1 = bucket1.get();
        Object o2 = bucket2.get();
        Object o3 = bucket3.get();
        Object o4 = bucket4.get();
        log.info("Str1: {}", o1);
        log.info("Str2: {}", o2);
        log.info("Str3: {}", o3);
        log.info("Str4: {}", o4);
    }

    // 删除字符串类型的节点: 根据键值移除
    @Test
    public void delBucket() {
        boolean delName1 = redissonClient.getBucket("com:jadan:test:name1").delete();
        log.info("delete: {}", delName1);
    }

    // 散列类型（hash）赋值
    @Test
    public void setMap() {
        RMap<Object, Object> map1 = redissonClient.getMap("map1");
        Map<String, Object> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map1.putAll(map);

        // StringCodec.INSTANCE
        RMap<Object, Object> map2 = redissonClient.getMap("map2", MapOptions.defaults());
        map2.putAll(map);

        // 使用MapOptions.defaults(), 在原来的map上追加数据  ---> 观察数据
        RMap<Object, Object> map3 = redissonClient.getMap("map3", StringCodec.INSTANCE, MapOptions.defaults());
        map3.putAll(map);
    }

    // 获取Map对象
    @Test
    public void getMap() {
        // 删除整个hash节点
        boolean map1Del = redissonClient.getMap("map1").delete();
        boolean map2Del = redissonClient.getMap("map2").delete();
        boolean map3Del = redissonClient.getMap("map3").delete();

        // 删除字段并返回删除个数
        long map1 = redissonClient.getMap("map1").fastRemove("k1");
        // 删除字段并返回映射值
        Object remove = redissonClient.getMap("map1").remove("k2");

        log.info("map1: {}", map1);
    }

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





















