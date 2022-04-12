package i2f.springboot.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author ltb
 * @date 2022/4/12 17:52
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.redis.cache.enable:true}")
@Configuration
@Slf4j
@ConfigurationProperties("i2f.springboot.config.redis.cache")
public class RedisCacheConfig {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Bean
    public RedisCache redisCache(){
        return new RedisCache(redisTemplate);
    }
}
