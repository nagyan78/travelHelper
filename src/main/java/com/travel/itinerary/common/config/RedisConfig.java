package com.travel.itinerary.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Configuration
public class RedisConfig {
    
    @Value("${spring.redis.host:localhost}")
    private String redisHost;
    
    @Value("${spring.redis.port:6379}")
    private int redisPort;
    
    @Value("${spring.redis.password:}")
    private String redisPassword;
    
    @Value("${spring.redis.database:0}")
    private int redisDatabase;
    
    /**
     * 配置 RedisTemplate
     * 使用 Jackson2JsonRedisSerializer 进行序列化
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 使用 String 序列化 key
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        
        // 使用 Jackson 序列化 value
        Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL
        );
        jsonSerializer.setObjectMapper(mapper);
        
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        
        template.afterPropertiesSet();
        
        return template;
    }
    
    /**
     * 配置 RedissonClient
     * 根据是否有密码动态配置
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        
        String address = "redis://" + redisHost + ":" + redisPort;
        
        if (redisPassword != null && !redisPassword.isEmpty()) {
            // 有密码的情况
            config.useSingleServer()
                .setAddress(address)
                .setPassword(redisPassword)
                .setDatabase(redisDatabase)
                .setConnectionPoolSize(10)
                .setConnectionMinimumIdleSize(5);
        } else {
            // 无密码的情况
            config.useSingleServer()
                .setAddress(address)
                .setDatabase(redisDatabase)
                .setConnectionPoolSize(10)
                .setConnectionMinimumIdleSize(5);
        }
        
        return Redisson.create(config);
    }
}