package i2f.core.container.collection;

import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;

public class SyncIterator<E> implements Iterator<E> {
    protected volatile Iterator<E> iterator;
    protected volatile ReadWriteLock lock;

    public SyncIterator(Iterator<E> iterator, ReadWriteLock lock) {
        this.iterator = iterator;
        this.lock = lock;
    }

    @Override
    public boolean hasNext() {
        lock.readLock().lock();
        try {
            return this.iterator.hasNext();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E next() {
        lock.readLock().lock();
        try {
            return this.iterator.next();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void remove() {
        lock.writeLock().lock();
        try {
            this.iterator.remove();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
