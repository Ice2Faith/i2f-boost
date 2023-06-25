package i2f.core.container.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SyncList<E> implements List<E> {
    private volatile List<E> list;
    private volatile ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncList(List<E> list) {
        this.list = list;
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return this.list.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return this.list.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.readLock().lock();
        try {
            return this.list.contains(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        lock.readLock().lock();
        try {
            return new SyncIterator<>(this.list.iterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Object[] toArray() {
        lock.readLock().lock();
        try {
            return this.list.toArray();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        lock.readLock().lock();
        try {
            return this.list.toArray(a);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean add(E e) {
        lock.writeLock().lock();
        try {
            return this.list.add(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.writeLock().lock();
        try {
            return this.list.remove(o);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.readLock().lock();
        try {
            return this.list.containsAll(c);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return this.list.addAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return this.list.addAll(index, c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return this.list.removeAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return this.list.retainAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            this.list.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E get(int index) {
        lock.readLock().lock();
        try {
            return this.list.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E set(int index, E element) {
        lock.writeLock().lock();
        try {
            return this.list.set(index, element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(int index, E element) {
        lock.writeLock().lock();
        try {
            this.list.add(index, element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E remove(int index) {
        lock.writeLock().lock();
        try {
            return this.list.remove(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        lock.readLock().lock();
        try {
            return this.list.indexOf(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        lock.readLock().lock();
        try {
            return this.list.lastIndexOf(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        lock.readLock().lock();
        try {
            return new SyncListIterator<>(this.list.listIterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        lock.readLock().lock();
        try {
            return new SyncListIterator<>(this.list.listIterator(index), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        lock.readLock().lock();
        try {
            return this.list.subList(fromIndex, toIndex);
        } finally {
            lock.readLock().unlock();
        }
    }
}
