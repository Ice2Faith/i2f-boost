package i2f.springboot.redisson;

import i2f.springboot.redis.*;
import i2f.springboot.redisson.aop.RedissonLockAop;
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
        RedissonConfig.class,
        RedissonAtomic.class,
        RedissonLockProvider.class,
        RedissonLockAop.class
})
public @interface EnableRedissonConfig {

}

