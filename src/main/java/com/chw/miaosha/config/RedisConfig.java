package com.chw.miaosha.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author CHW
 * @Date 2022/9/21
 **/
@Component
@Data
public class RedisConfig {
    /**
     * redis种第几个库
     */
    @Value("${spring.redis.database}")
    private Integer dbNum;
    /**
     * ip
     */
    @Value("${spring.redis.host}")
    private String host;
    /**
     * 端口
     */
    @Value("${spring.redis.port}")
    private Integer port;
    
    /**
     * 密码
     */
    @Value("${spring.redis.password}")
    private String password;
    
    /**
     * 连接超时时间
     */
    @Value("${spring.redis.timeout}")
    private Integer timeout;
    
    /**
     * 最大连接数
     */
    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxActive;
    /**
     * 最多维持空闲连接
     */
    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdle;
    
    /**
     * 连接池出借连接的最长期限
     */
    @Value("${spring.redis.jedis.pool.max-wait}")
    private Long maxWaitMillis;
    
    /**
     * 数据池
     */
    @Value("${spring.redis.database}")
    private int database;
    
}
