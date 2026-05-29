package com.travel.itinerary.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@Component
public class RedisUtil {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 设置键值对，带过期时间
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Redis set 操作失败: key={}", key, e);
        }
    }
    
    /**
     * 获取值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        try {
            return (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis get 操作失败: key={}", key, e);
            return null;
        }
    }
    
    /**
     * 删除键
     */
    public Boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis delete 操作失败: key={}", key, e);
            return false;
        }
    }
    
    /**
     * 判断键是否存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Redis hasKey 操作失败: key={}", key, e);
            return false;
        }
    }
    
    /**
     * 设置过期时间
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            return redisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            log.error("Redis expire 操作失败: key={}", key, e);
            return false;
        }
    }
}
