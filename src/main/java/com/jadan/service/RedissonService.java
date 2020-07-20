package com.jadan.service;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Jadan-Z
 * @Date 2019/11/13
 */
@Service
public class RedissonService {

    @Autowired
    private RedissonClient redissonClient;
}
