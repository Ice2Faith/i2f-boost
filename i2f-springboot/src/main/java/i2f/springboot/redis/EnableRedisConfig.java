package i2f.springboot.redis;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/3/27 13:28
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        RedisConfig.class,
        ConnectRedisTemplateConfig.class,
        LettuceRedisTemplateConfig.class,
        RedisCacheConfig.class,
})
public @interface EnableRedisConfig {

}

