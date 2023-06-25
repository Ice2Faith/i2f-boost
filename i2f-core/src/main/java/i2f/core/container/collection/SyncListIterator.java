package i2f.core.container.collection;

import java.util.ListIterator;
import java.util.concurrent.locks.ReadWriteLock;

public class SyncListIterator<E> extends SyncIterator<E> implements ListIterator<E> {

    public SyncListIterator(ListIterator<E> iterator, ReadWriteLock lock) {
        super(iterator, lock);
    }

    protected ListIterator<E> iterator() {
        lock.readLock().lock();
        try {
            return (ListIterator<E>) this.iterator;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean hasPrevious() {
        lock.readLock().lock();
        try {
            return this.iterator().hasPrevious();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E previous() {
        lock.readLock().lock();
        try {
            return this.iterator().previous();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int nextIndex() {
        lock.readLock().lock();
        try {
            return this.iterator().nextIndex();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int previousIndex() {
        lock.readLock().lock();
        try {
            return this.iterator().previousIndex();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void set(E e) {
        lock.writeLock().lock();
        try {
            this.iterator().set(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(E e) {
        lock.writeLock().lock();
        try {
            this.iterator().add(e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
