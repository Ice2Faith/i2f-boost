package i2f.extension.redis.inject.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.core.cache.ICache;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/11/4 9:13
 * @desc
 */
@Data
public class ObjectRedisCache<K, V> implements ICache<K, V> {

    private RedisTemplate<String, Object> redisTemplate;

    private ObjectMapper objectMapper;

    private Class<K> keyClass;

    private String prefix = null;

    public ObjectRedisCache(Class<K> keyClass, RedisTemplate<String, Object> redisTemplate) {
        this.keyClass = keyClass;
        this.redisTemplate = redisTemplate;
        init();
    }

    public ObjectRedisCache(Class<K> keyClass, RedisTemplate<String, Object> redisTemplate, String prefix) {
        this.keyClass = keyClass;
        this.redisTemplate = redisTemplate;
        this.prefix = prefix;
        init();
    }

    private void init() {
        // 设置序列化
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.setLocale(Locale.getDefault());
        objectMapper.setTimeZone(TimeZone.getDefault());
    }

    private String keysPatten() {
        if (prefix == null) {
            return "*";
        }
        return prefix + "*";
    }

    private String serializeKey(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage(), e);
        }
    }

    private Object deserializeKey(String str) {
        try {
            return objectMapper.readValue(str, keyClass);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage(), e);
        }
    }

    private String redisKey(K key) {
        if (prefix != null) {
            return prefix + serializeKey(key);
        }
        return serializeKey(key);
    }

    private K redisKeyDecode(String str) {
        String rk = str;
        if (prefix != null) {
            rk = str.substring(prefix.length());
        }
        return (K) deserializeKey(rk);
    }

    @Override
    public void set(K key, V val) {
        redisTemplate.opsForValue().set(redisKey(key), val);
    }

    @Override
    public void set(K key, V val, long expireTime, TimeUnit expireUnit) {
        redisTemplate.opsForValue().set(redisKey(key), val, expireTime, expireUnit);
    }

    @Override
    public void expire(K key, long expireTime, TimeUnit expireUnit) {
        redisTemplate.expire(redisKey(key), expireTime, expireUnit);
    }

    @Override
    public Long getExpire(K key, TimeUnit expireUnit) {
        return redisTemplate.getExpire(redisKey(key), expireUnit);
    }

    @Override
    public V get(K key) {
        return (V) redisTemplate.opsForValue().get(redisKey(key));
    }

    @Override
    public boolean exists(K key) {
        return redisTemplate.hasKey(redisKey(key));
    }

    @Override
    public void remove(K key) {
        redisTemplate.delete(redisKey(key));
    }

    @Override
    public Set<K> keys() {
        Set<String> set = redisTemplate.keys(keysPatten());
        Set<K> ret = new HashSet<>(set.size());
        for (String item : set) {
            ret.add(redisKeyDecode(item));
        }
        return ret;
    }

    public Set<String> redisKeys() {
        return redisTemplate.keys(keysPatten());
    }

    @Override
    public void clean() {
        Set<String> set = redisKeys();
        for (String item : set) {
            redisTemplate.delete(item);
        }
    }
}
