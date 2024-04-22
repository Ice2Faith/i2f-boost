package i2f.container.sync.adapter;

import java.util.ListIterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/4/18 14:27
 * @desc
 */
public class SyncListIteratorAdapter<E> implements ListIterator<E> {
    private ListIterator<E> iterator;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncListIteratorAdapter(ListIterator<E> iterator) {
        this.iterator = iterator;
    }

    public SyncListIteratorAdapter(ListIterator<E> iterator, ReadWriteLock lock) {
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
    public boolean hasPrevious() {
        lock.readLock().lock();
        try {
            return iterator.hasPrevious();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E previous() {
        lock.writeLock().lock();
        try {
            return iterator.previous();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int nextIndex() {
        lock.readLock().lock();
        try {
            return iterator.nextIndex();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int previousIndex() {
        lock.readLock().lock();
        try {
            return iterator.previousIndex();
        } finally {
            lock.readLock().unlock();
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
    public void set(E e) {
        lock.writeLock().lock();
        try {
            iterator.set(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(E e) {
        lock.writeLock().lock();
        try {
            iterator.add(e);
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
