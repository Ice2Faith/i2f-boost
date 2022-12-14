package i2f.springboot.redisson;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class RedissonLockProvider {

    private RedissonClient redissonClient;

    public RedissonLockProvider(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public RLock getLock(String lockName) {
        return redissonClient.getLock(lockName);
    }

    public RReadWriteLock getReadWriteLock(String lockName) {
        return redissonClient.getReadWriteLock(lockName);
    }

    public RLock getLock(Class<?> clazz) {
        return getLock(clazz.getName());
    }

    public RReadWriteLock getReadWriteLock(Class<?> clazz) {
        return getReadWriteLock(clazz.getName());
    }

    public RLock getLock(Class<?> clazz, String subKey) {
        return getLock(clazz.getName() + ":" + subKey);
    }

    public RReadWriteLock getReadWriteLock(Class<?> clazz, String subKey) {
        return getReadWriteLock(clazz.getName() + ":" + subKey);
    }
}
