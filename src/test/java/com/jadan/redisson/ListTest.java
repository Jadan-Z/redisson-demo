package com.jadan.redisson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.api.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表类型（list）：常用方法测试
 * @Author Jadan-Z
 * @Date 2019/12/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ListTest {
    Logger log = LoggerFactory.getLogger(ListTest.class);

    @Autowired
    private RedissonClient redissonClient;

    private static final String PREFIX = "com:jadan:test:list:";

    /**
     * add: 添加一个元素
     * addAll： 添加List
     * addBefore： 在指定值之前设值
     * addAfter： 在指定值之后设值
     */
    @Test
    public void add() {
        RList<Object> rList = redissonClient.getList(PREFIX + "list1");
        rList.add("v1");
        // 指定索引设值
        rList.add(0, "v2");
        // 在那个值之前设值
        rList.addBefore("v2", "v3");
        // 在那个值之后设值
        rList.addAfter("v1", "v4");

//        RList<Object> rList1 = redissonClient.getList(PREFIX + "list2");
//        List<Integer> list = new ArrayList<>();
//        list.add(3);
//        list.add(1);
//        list.add(10);
//        list.add(5);
//        rList1.addAll(list);
    }

    /**
     * readAll: 读取列表中的所有的值
     * subList： 截取指定索引的列表值，包头不包尾
     * readSort: 获取指定的排列方式得到的列表
     */
    @Test
    public void read() {
        RList<Object> rList = redissonClient.getList(PREFIX + "list1");
        List<Object> list = rList.readAll();
        for (Object obj: list) {
            log.info("{}", obj);
        }

        RList<Object> objects = rList.subList(0, 2);
        List<Object> list1 = objects.readAll();
        for (Object obj: list1) {
            log.info("{}", obj);
        }

        RList<Object> rList1 = redissonClient.getList(PREFIX + "list2");
        List<Object> list2 = rList1.readSort(SortOrder.DESC);
        for (Object obj: list2) {
            log.info("{}", obj);
        }
    }

    // get： 指定索引获取列表值
    @Test
    public void get() {
        RList<Object> rList = redissonClient.getList(PREFIX + "list1");
        Object v = rList.get(0);
        log.info("索引为0的值：{}", v);
        List<Object> list = rList.get(1, 2, 3);
        for (Object obj: list) {
            log.info("{}", obj);
        }
    }

    /**
     * set(int index, Object v): 指定索引并赋新值，返回旧值
     * fastSet(int index, Object v): 指定索引并赋新值，无返回值
     */
    @Test
    public void set() {
        RList<Object> rList = redissonClient.getList(PREFIX + "list1");
        Object v33333 = rList.set(0, "v33333");
        rList.fastSet(3, "v4444444444");
        log.info("{}", v33333);
    }

    /**
     * 注意：指定索引删除，多次删除得注意，，列表值不断缩减，指定索引可能会删除不确定的值
     * fastRemove(int index): 指定索引删除
     * remove(int index): 指定索引删除，并返回之前的值
     * remove(Object v): 指定值删除，成功返回true，否则false
     * removeAll: 移除指定列表含有的值
     */
    @Test
    public void remove() {
        RList<Object> rList = redissonClient.getList(PREFIX + "list1");
        rList.fastRemove(3);
        Object remove = rList.remove(0);
        boolean v1 = rList.remove("v1");
        boolean v4 = rList.remove("v4");
        log.info("remove: {}", remove);
        log.info("v1: {}", v1);
        log.info("v4: {}", v4);
    }

    /**
     * isEmpty: 列表是否为空
     * isExists: 该节点是否存在
     */
    @Test
    public void is() {
        RList<Object> rList = redissonClient.getList(PREFIX + "list1");
        boolean empty = rList.isEmpty();
        boolean exists = rList.isExists();
        log.info("empty: {}", empty);
        log.info("exists: {}", exists);
    }

    // delete: 删除节点。存在的节点方可删除并返回true，否则，false
    @Test
    public void delete() {
        RList<Object> rList = redissonClient.getList(PREFIX + "list1");
        boolean delete = rList.delete();
        log.info("删除节点{}, {}", rList.getName(), delete == true? "success": "error");
    }

    /**
     * contains: 是否含有指定的值， 并返回布尔值
     * containsAll(Collection<?> c): 是否含有指定的集合， 并返回布尔值
     */
    @Test
    public void contains() {
        RList<Object> rList = redissonClient.getList(PREFIX + "list1");
        boolean containsBoolean = rList.contains("v1");
        log.info("containsBoolean: {}", containsBoolean);

        List<String> list = new ArrayList<>();
        list.add("v2");
        list.add("v3");
        boolean containsAllBoolean = rList.containsAll(list);
        log.info("containsAllBoolean: {}", containsAllBoolean);
    }
}
