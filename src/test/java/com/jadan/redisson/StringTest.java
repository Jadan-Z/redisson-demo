package com.jadan.redisson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.IntegerCodec;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.TimeUnit;

/**
 * 字符串类型（String）：常用方法测试
 * @Author Jadan-Z
 * @Date 2019/12/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StringTest {
    private Logger log = LoggerFactory.getLogger(StringTest.class);

    @Autowired
    private RedissonClient redissonClient;

    private static final Integer EFFECTIVE_DURATION_SECONDS = 30;
    private static final Integer EFFECTIVE_DURATION_MINUTES = 1;

    // set: 赋值
    @Test
    public void set() {
        // 未设置过期时间
        // 1) String类型
        redissonClient.getBucket("com:jadan:test:string:name1").set("jadan1");  //  "jadan1"
        redissonClient.getBucket("com:jadan:test:string:name2", StringCodec.INSTANCE).set("jadan1");  // jadan1
        // 2） Integer类型
        redissonClient.getBucket("com:jadan:test:string:age1").set(15);   // 15
        redissonClient.getBucket("com:jadan:test:string:age2", IntegerCodec.INSTANCE).set(25); // 25

        // 设置过期时间 ttl
        redissonClient.getBucket("com:jadan:test:string:amount1").set(100, EFFECTIVE_DURATION_SECONDS, TimeUnit.SECONDS);  // 30s过期
        redissonClient.getBucket("com:jadan:test:string:amount2").set(200, EFFECTIVE_DURATION_MINUTES, TimeUnit.MINUTES);  // 1分钟过期
    }

    // get: 取值（怎么存怎么取，存时有加 StringCodec.INSTANCE,取也要加，否则为null。）
    @Test
    public void get() {
        redissonClient.getBucket("com:jadan:test:string:name1").set("jadan1");
        RBucket<Object> bucket1 = redissonClient.getBucket("com:jadan:test:string:name1");
        Object name1 = bucket1.get();
        log.info("name1: {}", name1.equals("jadan1"));

        // 注意：设值时有StringCodec类型，取值也必须有
        redissonClient.getBucket("com:jadan:test:string:name2", StringCodec.INSTANCE).set("jadan1");
        RBucket<Object> bucket2 = redissonClient.getBucket("com:jadan:test:string:name2", StringCodec.INSTANCE);
        Object name2 = bucket2.get();
        log.info("name2: {}", name2);
    }

    // getName: 获取该节点
    @Test
    public void getName() {
        RBucket<Object> bucket = redissonClient.getBucket("com:jadan:test:string:name1");
        String name = bucket.getName();
        log.info("该节点为：{}", name);
    }

    // delete: 删除节点。存在的节点方可删除并返回true，否则，false
    @Test
    public void delete() {
        RBucket<Object> bucket = redissonClient.getBucket("com:jadan:test:string:name3");
        boolean delete = bucket.delete();
        log.info("删除节点{}, {}", bucket.getName(), delete == true ? "success" : "fail");
    }

    // getAndSet： 获取值后再设值
    @Test
    public void getAndSet() {
        RBucket<Object> bucket = redissonClient.getBucket("com:jadan:test:string:name1");
        Object andSet = bucket.getAndSet("reload-set");
        log.info("旧值{}, 新值设值成功", andSet);
    }

    // trySet(V var1[, long var2, TimeUnit var4]): 键不存在时设值并返回true，否则，false
    @Test
    public void trySet() {
        RBucket<Object> bucket = redissonClient.getBucket("com:jadan:test:string:name3");
        boolean trySet = bucket.trySet("jadan-trySet");
        log.info("trySet: {}", trySet);
    }

}
