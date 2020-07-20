package com.jadan.redisson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.MapOptions;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 散列类型（hash）： 常见方法测试
 * @Author Jadan-Z
 * @Date 2019/12/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HashTest {
    Logger log = LoggerFactory.getLogger(HashTest.class);

    @Autowired
    private RedissonClient redissonClient;

    private static final String NAMESPACE_PREFIX = "com:jadan:test:map:";

    // putAll ： 赋值
    @Test
    public void putAll () {
        RMap<Object, Object> map1 = redissonClient.getMap(NAMESPACE_PREFIX + "map1");
        Map<String, Object> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map1.putAll(map);

        // StringCodec.INSTANCE
        RMap<Object, Object> map2 = redissonClient.getMap(NAMESPACE_PREFIX + "map2", MapOptions.defaults());
        map2.putAll(map);

        // 使用MapOptions.defaults(), 在原来的map上追加数据  ---> 观察数据
        RMap<Object, Object> map3 = redissonClient.getMap(NAMESPACE_PREFIX + "map3", StringCodec.INSTANCE, MapOptions.defaults());
        map3.putAll(map);
    }

    /**
     * get： 获取指定键的值
     * readAllMap: 获取键下的map
     * readAllKeySet： 读取rMap下所有的键
     * getAll: 根据键获取Map
     */
    @Test
    public void get() {
        RMap<Object, Object> rMap = redissonClient.getMap(NAMESPACE_PREFIX + "map1");
        Object v1 = rMap.get("k1");
        Object v2 = rMap.get("k2");
        log.info("k1: {}, k2: {}", v1, v2);

        Map<Object, Object> objectObjectMap = rMap.readAllMap();

        Set<Object> allKey = rMap.readAllKeySet();
        for (Object obj:allKey) {
            log.info("{}", obj);
        }

        Map<Object, Object> allKeyAndValue = rMap.getAll(allKey);
        for(Object obj: allKeyAndValue.values()) {
            log.info("{}", obj);
        }
    }

    // remove: 移除 rMap下指定的键及值
    @Test
    public void remove() {
        RMap<Object, Object> rMap = redissonClient.getMap(NAMESPACE_PREFIX + "map2");
        // 删除字段并返回删除个数
        long count = rMap.fastRemove("k1", "k2");
        log.info("移除的个数：{}", count);

        // 删除字段并返回映射值
        Object removeValue = rMap.remove("k3");
        log.info("移除的值：{}", removeValue);
    }

    // delete: 删除节点。存在的节点方可删除并返回true，否则，false
    @Test
    public void delete() {
        RMap<Object, Object> rMap = redissonClient.getMap("map2");
        boolean delete = rMap.delete();
        log.info("移除节点{}：{}", rMap.getName(), delete == true? "success": "fail");
    }

    // isExists: 判断是否存在
    // isEmpty: 判断是否为空
    @Test
    public void isExists() {
        RMap<Object, Object> rMap = redissonClient.getMap(NAMESPACE_PREFIX + "map1");
        boolean exists = rMap.isExists();
        boolean empty = rMap.isEmpty();
    }

}
