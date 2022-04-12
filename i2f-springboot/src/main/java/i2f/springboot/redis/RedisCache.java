package i2f.springboot.redis;

import i2f.core.cache.ICache;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/12 14:06
 * @desc
 */
public class RedisCache implements ICache<String> {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisCache(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    @Override
    public Object set(String key, Object val) {
        redisTemplate.opsForValue().set(key,val);
        return true;
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Object get(String key) {
        Object ret=null;
        if(redisTemplate.hasKey(key)){
            ret=redisTemplate.opsForValue().get(key);
        }
        return ret;
    }

    @Override
    public Object set(String key, long expire, TimeUnit timeUnit, Object val) {
        set(key,val);
        expire(key,expire,timeUnit);
        return true;
    }

    @Override
    public Object expire(String key, long expire, TimeUnit timeUnit) {
        redisTemplate.expire(key, Duration.ofMillis(timeUnit.toMillis(expire)));
        return true;
    }

    @Override
    public Object remove(String key) {
        Object ret=null;
        if(redisTemplate.hasKey(key)){
            ret=redisTemplate.opsForValue().get(key);
            redisTemplate.delete(key);
        }
        return ret;
    }

    @Override
    public Set<String> keys() {
        Set<String> ret=null;
        ret=redisTemplate.keys("*");
        return ret;
    }

    @Override
    public Object clean() {
        throw new UnsupportedOperationException("redis template not support clear!");
    }
}
