package i2f.extension.redis.inject.template;

import i2f.core.cache.ICache;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/11/4 9:13
 * @desc
 */
@Data
public class RedisCache<V> implements ICache<String, V> {

    private RedisTemplate<String, Object> redisTemplate;

    private String prefix = null;

    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisCache(RedisTemplate<String, Object> redisTemplate, String prefix) {
        this.redisTemplate = redisTemplate;
        this.prefix = prefix;
    }

    private String keysPatten() {
        if (prefix == null) {
            return "*";
        }
        return prefix + "*";
    }

    private String redisKey(String key) {
        if (prefix != null) {
            return prefix + key;
        }
        return key;
    }

    @Override
    public void set(String key, V val) {
        redisTemplate.opsForValue().set(redisKey(key), val);
    }

    @Override
    public void set(String key, V val, long expireTime, TimeUnit expireUnit) {
        redisTemplate.opsForValue().set(redisKey(key), val, expireTime, expireUnit);
    }

    @Override
    public void expire(String key, long expireTime, TimeUnit expireUnit) {
        redisTemplate.expire(redisKey(key), expireTime, expireUnit);
    }

    @Override
    public V get(String key) {
        return (V) redisTemplate.opsForValue().get(redisKey(key));
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(redisKey(key));
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(redisKey(key));
    }

    @Override
    public Set<String> keys() {
        return redisTemplate.keys(keysPatten());
    }

    @Override
    public void clean() {
        Set<String> set = keys();
        for (String item : set) {
            redisTemplate.delete(item);
        }
    }
}
