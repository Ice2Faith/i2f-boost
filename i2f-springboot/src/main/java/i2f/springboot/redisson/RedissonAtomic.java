package i2f.springboot.redisson;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/5/29 9:11
 * @desc
 */
@Component
public class RedissonAtomic {
    private RedissonClient redissonClient;

    public RedissonAtomic(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public static final String REDIS_ATOMIC_PREFIX = "atomic:";

    public String groupKey(String key) {
        if (key.startsWith(REDIS_ATOMIC_PREFIX)) {
            return key;
        }
        return REDIS_ATOMIC_PREFIX + key;
    }

    public RAtomicLong getLong(String key) {
        return getLong(key, -1, null);
    }

    public RAtomicLong getLong(String key, long ttl, TimeUnit unit) {
        RAtomicLong ret = redissonClient.getAtomicLong(groupKey(key));
        if (ttl >= 0) {
            ret.expire(ttl, unit);
        }
        return ret;
    }

    public long nextId(String key) {
        return nextId(key, -1, null);
    }

    public long nextId(String key, long ttl, TimeUnit unit) {
        RAtomicLong num = getLong(key, ttl, unit);
        long ret = num.incrementAndGet();
        return ret;
    }
}
