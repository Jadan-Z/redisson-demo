package com.jadan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redisson属性
 * @Author Jadan-Z
 * @Date 2019/11/13
 */
@Data
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {
    private String redisAddress;

    private String password;

    private String deployMode;

    private Integer database = 0;

    private Integer database2 = 2;
}
