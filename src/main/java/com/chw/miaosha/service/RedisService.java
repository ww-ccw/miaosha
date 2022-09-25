package com.chw.miaosha.service;

import org.springframework.stereotype.Service;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@Service
public interface RedisService {
    /**
     * 将数据写入到redis
     *
     * @param key   key
     * @param value value
     * @param <T>   value类型
     * @param time  过期时间
     * @return
     */
    public <T> boolean set(String key, T value, int time);
    
    /**
     * 通过key得到Value
     * @param key
     * @return
     */
    public String get(String key);
}
