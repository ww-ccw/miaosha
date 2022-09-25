package com.chw.miaosha.service.impl;

import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.util.JsonUtil;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@Service
public class RedisServiceImpl implements RedisService {
    
    @Resource
    JedisPool jedisPool;
    
    @Override
    public <T> boolean set(String key, T value, int time) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = JsonUtil.objectToJson(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            if (time <= 0) {
                jedis.set(key, str);
            } else {
                jedis.setex(key, time, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }
    
    @Override
    public String get(String key) {
        String value = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        }finally {
            returnToPool(jedis);
        }
        
        return value;
    }
    
    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
