package com.jadan.redisson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * 有序集合类型（sorted set）：
 * @Author Jadan-Z
 * @Date 2019/12/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SortSetTest {
    Logger log = LoggerFactory.getLogger(SortSetTest.class);

    @Autowired
    private RedissonClient redissonClient;

    private static final String PREFIX = "com:jadan:test:sortset:";

    /**
     * add: 添加一个元素
     * addAll： 添加set集合
     */
    @Test
    public void add () {
        RScoredSortedSet<Object> rScoredSortedSet = redissonClient.getScoredSortedSet(PREFIX + "sortset1");
        rScoredSortedSet.add(11, "v1");
        rScoredSortedSet.add(8, "v2");
        rScoredSortedSet.add(9, "v3");
        rScoredSortedSet.add(10, "v4");

        RScoredSortedSet<Object> rScoredSortedSet1 = redissonClient.getScoredSortedSet(PREFIX + "sortset2", LongCodec.INSTANCE);

        Map<Object, Double> map1 = new HashMap<>();
        map1.put("100001", 10001d);
        map1.put("100002", 10002d);
        map1.put("100003", 10003d);
        map1.put("100003", 10004d);
        rScoredSortedSet1.addAll(map1);
    }

    @Test
    public void xx() {
        RScoredSortedSet<Object> rScoredSortedSet = redissonClient.getScoredSortedSet(PREFIX + "sortset1");

    }

    // delete: 删除节点。存在的节点方可删除并返回true，否则，false
    @Test
    public void delete () {
        RScoredSortedSet<Object> rScoredSortedSet = redissonClient.getScoredSortedSet(PREFIX + "sortset1");
        boolean delete = rScoredSortedSet.delete();
        log.info("删除节点{}, {}", rScoredSortedSet.getName(), delete == true? "success": "error");
    }

}
