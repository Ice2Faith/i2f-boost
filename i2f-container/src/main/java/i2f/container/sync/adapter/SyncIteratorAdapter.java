package i2f.container.sync.adapter;

import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/4/18 14:16
 * @desc
 */
public class SyncIteratorAdapter<E> implements Iterator<E> {
    private Iterator<E> iterator;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncIteratorAdapter(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    public SyncIteratorAdapter(Iterator<E> iterator, ReadWriteLock lock) {
        this.iterator = iterator;
        this.lock = lock;
    }

    @Override
    public boolean hasNext() {
        lock.readLock().lock();
        try {
            return iterator.hasNext();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E next() {
        lock.writeLock().lock();
        try {
            return iterator.next();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove() {
        lock.writeLock().lock();
        try {
            iterator.remove();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            iterator.forEachRemaining(action);
        } finally {
            lock.readLock().unlock();
        }
    }
}
