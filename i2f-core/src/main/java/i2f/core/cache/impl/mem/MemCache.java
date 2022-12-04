package i2f.core.cache.impl.mem;

import i2f.core.cache.impl.AbsExpireCache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/11/4 9:13
 * @desc
 */
public class MemCache<K, V> extends AbsExpireCache<K, V> {

    private volatile ConcurrentHashMap<K, V> _map = new ConcurrentHashMap<>();

    public MemCache() {

    }

    public MemCache(String threadNamePrefix) {
        super(threadNamePrefix);

    }

    @Override
    public void set(K key, V val) {
        this._map.put(key, val);
    }

    @Override
    public V get(K key) {
        return this._map.get(key);
    }

    @Override
    public boolean exists(K key) {
        return this._map.containsKey(key);
    }

    @Override
    public void remove(K key) {
        this._map.remove(key);
    }

    @Override
    public Set<K> keys() {
        return this._map.keySet();
    }

    @Override
    public void clean() {
        this._map.clear();
    }

}
