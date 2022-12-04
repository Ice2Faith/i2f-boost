package i2f.core.cache.impl.mem;

import i2f.core.cache.LruLinkedHashMap;
import i2f.core.cache.impl.AbsExpireCache;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ltb
 * @date 2022/11/4 9:13
 * @desc
 */
public class LruMemCache<K, V> extends AbsExpireCache<K, V> {

    private int capital = 200;

    private volatile LinkedHashMap<K, V> _map = null;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public LruMemCache(int capital) {
        this.init();
        this.capital = capital;
    }

    public LruMemCache(int capital, String threadNamePrefix) {
        super(threadNamePrefix);

        this.init();
        this.capital = capital;
    }

    private void init() {
        this._map = new LruLinkedHashMap<>(this.capital);
    }

    @Override
    public void set(K key, V val) {
        lock.writeLock().lock();
        try {
            this._map.put(key, val);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V get(K key) {
        lock.readLock().lock();
        try {
            return this._map.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean exists(K key) {
        lock.readLock().lock();
        try {
            return this._map.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void remove(K key) {
        lock.writeLock().lock();
        try {
            this._map.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Set<K> keys() {
        lock.readLock().lock();
        try {
            return this._map.keySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clean() {
        lock.writeLock().lock();
        try {
            this._map.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

}
