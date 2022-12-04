package i2f.core.cache;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface ICache<K, V> {
    void set(K key, V val);

    void set(K key, V val, long expireTime, TimeUnit expireUnit);

    void expire(K key, long expireTime, TimeUnit expireUnit);

    V get(K key);

    boolean exists(K key);

    void remove(K key);

    Set<K> keys();

    void clean();
}
