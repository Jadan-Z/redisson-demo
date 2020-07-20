package com.jadan.redisson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author Jadan-Z
 * @Date 2019/12/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SetTest {
    Logger log = LoggerFactory.getLogger(SetTest.PREFIX);

    @Autowired
    private RedissonClient redissonClient;

    private static final String PREFIX = "com:jadan:test:set:";

    /**
     * add: 添加一个元素
     * addAll： 添加set集合
     */
    @Test
    public void add() {
        RSortedSet<Object> rSortedSet = redissonClient.getSortedSet(PREFIX + "set1");
        rSortedSet.add("sort-value-1");
        rSortedSet.add("sort-value-2");
        rSortedSet.add("sort-value-1");
        rSortedSet.add("sort-value-4");

        RSortedSet<Object> rSortedSet1 = redissonClient.getSortedSet(PREFIX + "set2");
        Set<Object> set = new HashSet<>();
        set.add("sort-value2-1");
        set.add("sort-value2-1");
        set.add("sort-value2-2");
        set.add("sort-value2-2");
        rSortedSet1.addAll(set);
    }


    /**
     * first： 获取第一个元素
     * last： 获取最后一个元素
     * size: 获取元素数量
     */
    @Test
    public void first() {
        RSortedSet<Object> rSortedSet = redissonClient.getSortedSet(PREFIX + "set1");
        Object first = rSortedSet.first();
        log.info("first: {}", first);

        Object last = rSortedSet.last();
        log.info("last: {}", last);

        int size = rSortedSet.size();
        log.info("size: {}", size);

    }

    // delete: 删除节点。存在的节点方可删除并返回true，否则，false
    @Test
    public void delete() {
        RSortedSet<Object> rSortedSet = redissonClient.getSortedSet(PREFIX + "set1");
        boolean delete = rSortedSet.delete();
        log.info("删除节点{}: {}", rSortedSet.getName(), delete == true? "success": "error");

        RSortedSet<Object> rSortedSet1 = redissonClient.getSortedSet(PREFIX + "set2");
        boolean delete1 = rSortedSet1.delete();
        log.info("删除节点{}: {}", rSortedSet1.getName(), delete1 == true? "success": "error");
    }

    @Test
    public void xx() {
        RSortedSet<Object> rSortedSet = redissonClient.getSortedSet(PREFIX + "set1");
        boolean touch = rSortedSet.touch();
        log.info("{}", touch);
    }
}
