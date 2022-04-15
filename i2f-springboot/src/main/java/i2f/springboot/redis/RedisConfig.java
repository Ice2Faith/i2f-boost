package i2f.springboot.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.text.SimpleDateFormat;

/**
 * @author ltb
 * @date 2022/4/12 14:05
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.redis.enable:true}")
@Slf4j
@Configuration
@ConfigurationProperties("i2f.springboot.config.redis")
@EnableRedisRepositories
public class RedisConfig {

    String dateFormat="yyyy-MM-dd HH:mm:ss SSS";

    @Bean
    public Jackson2JsonRedisSerializer getRedisSerializer(){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        log.info("Jackson2JsonRedisSerializer config done.");
        return jackson2JsonRedisSerializer;
    }
}
