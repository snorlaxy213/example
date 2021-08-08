package com.cjh.websocket.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;

@Configuration
public class RedisConfig {

    @Resource
    private LettuceConnectionFactory lettuceConnectionFactory;

    /**
     * 需要手动注册RedisMessageListenerContainer加入IOC容器
     * @author lijt
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅了一个叫chat 的通道
        container.addMessageListener(new MessageListener(){
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String msg = new String(message.getBody());
                System.out.println(new String(pattern) + "主题发布：" + msg);
            }
        }, new PatternTopic("PRESCRIPTION"));
        return container;
    }

    /**
     * jackson的序列化方式(默认使用JDK的序列化方式)
     */
    private RedisSerializer<Object> valueSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
//         om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    /**
     * String的序列化方式
     */
    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    /**
     * 缓存管理器
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //设置Redis默认配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // key采用String的序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                // value序列化方式采用jackson
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(30 * 60));//缓存过期时间

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(lettuceConnectionFactory)
                .cacheDefaults(config);

        return builder.build();
    }

    @Bean(name = "cacheKeyGenerator")
    public KeyGenerator cacheKeyGenerator() {
        return (target, method, params) -> {
            StringBuffer sb = new StringBuffer();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        // key采用String的序列化方式
        redisTemplate.setKeySerializer(keySerializer());
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(keySerializer());
        // value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(valueSerializer());
        // hash的value序列化方式采用jackson
        redisTemplate.setValueSerializer(valueSerializer());

        return redisTemplate;
    }
}
