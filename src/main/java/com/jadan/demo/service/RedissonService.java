package com.jadan.demo.service;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author Jadan-Z
 * @Date 2019/11/13
 */
@Service
public class RedissonService {

    @Autowired
    private RedissonClient redissonClient;


}
