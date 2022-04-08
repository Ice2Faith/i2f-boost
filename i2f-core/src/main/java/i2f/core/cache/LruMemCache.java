package i2f.core.cache;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ltb
 * @date 2022/4/2 17:46
 * @desc
 */
public class LruMemCache<T> implements ICache<T>{
    private ReadWriteLock lock=new ReentrantReadWriteLock();
    private LruLinkedHashMap<T,Object> cacheData;

    public LruMemCache(int capital) {
        cacheData=new LruLinkedHashMap<>(capital);
    }

    @Override
    public Object set(T key, Object val) {
        lock.writeLock().lock();
        try{
            return cacheData.put(key,val);
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean exists(T key) {
        lock.writeLock().lock();
        try{
            return cacheData.containsKey(key);
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Object get(T key) {
        lock.readLock().lock();
        try{
            return cacheData.get(key);
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Object set(T key, long expire, TimeUnit timeUnit, Object val) {
        lock.writeLock().lock();
        try{
            throw new UnsupportedOperationException("LruMemCache un-support set cache with expire!");
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Object expire(T key, long expire, TimeUnit timeUnit) {
        lock.writeLock().lock();
        try{
            throw new UnsupportedOperationException("LruMemCache un-support update data expire!");
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Object remove(T key) {
        lock.writeLock().lock();
        try{
            return cacheData.remove(key);
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Set<T> keys() {
        lock.readLock().lock();
        try{
            return cacheData.keySet();
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Object clean() {
        lock.writeLock().lock();
        try{
            int ret=cacheData.size();
            cacheData.clear();
            return ret;
        }finally {
            lock.writeLock().unlock();
        }
    }
}
