package i2f.container.sync.adapter;

import java.util.Enumeration;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2024/4/18 17:15
 * @desc
 */
public class SyncEnumerationAdapter<E> implements Enumeration<E> {
    private Enumeration<E> target;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncEnumerationAdapter(Enumeration<E> target) {
        this.target = target;
    }

    public SyncEnumerationAdapter(Enumeration<E> target, ReadWriteLock lock) {
        this.target = target;
        this.lock = lock;
    }

    @Override
    public boolean hasMoreElements() {
        lock.readLock().lock();
        try {
            return target.hasMoreElements();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E nextElement() {
        lock.writeLock().lock();
        try {
            return target.nextElement();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
