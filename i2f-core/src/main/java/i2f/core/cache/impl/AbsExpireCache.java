package i2f.core.cache.impl;

import i2f.core.cache.ICache;
import i2f.core.thread.NamingThreadFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/11/4 9:13
 * @desc
 */
public abstract class AbsExpireCache<K, V> implements ICache<K, V> {

    private volatile String threadNamePrefix = null;
    private volatile ConcurrentHashMap<K, Long> _expire = new ConcurrentHashMap<>();
    private volatile ScheduledExecutorService poolExecutor;

    public AbsExpireCache() {
        this.init();
    }

    public AbsExpireCache(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;

        this.init();
    }

    private void init() {
        poolExecutor = Executors.newScheduledThreadPool(5, new NamingThreadFactory("cache", "expire" + (threadNamePrefix == null ? "" : "-" + threadNamePrefix)));
    }


    @Override
    public void set(K key, V val, long expireTime, TimeUnit expireUnit) {
        this.set(key, val);
        expire(key, expireTime, expireUnit);
    }

    @Override
    public void expire(K key, long expireTime, TimeUnit expireUnit) {
        long expireKey = System.currentTimeMillis() + expireUnit.toMillis(expireTime);
        this._expire.put(key, expireKey);
        poolExecutor.schedule(() -> {
            Long ttl = this._expire.get(key);
            if (ttl == expireKey) {
                this._expire.remove(key);
                this.remove(key);
            }
        }, expireTime, expireUnit);
    }


}
