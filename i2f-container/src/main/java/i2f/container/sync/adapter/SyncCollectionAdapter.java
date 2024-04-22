package i2f.container.sync.adapter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/4/18 14:40
 * @desc
 */
public class SyncCollectionAdapter<E> implements Collection<E> {
    private Collection<E> collection;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncCollectionAdapter(Collection<E> collection) {
        this.collection = collection;
    }

    public SyncCollectionAdapter(Collection<E> collection, ReadWriteLock lock) {
        this.collection = collection;
        this.lock = lock;
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return collection.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return collection.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.readLock().lock();
        try {
            return collection.contains(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        lock.readLock().lock();
        try {
            return new SyncIteratorAdapter<>(collection.iterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Object[] toArray() {
        lock.readLock().lock();
        try {
            return collection.toArray();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        lock.readLock().lock();
        try {
            return collection.toArray(a);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean add(E e) {
        lock.writeLock().lock();
        try {
            return collection.add(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.writeLock().lock();
        try {
            return collection.remove(o);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.readLock().lock();
        try {
            return collection.containsAll(c);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return collection.addAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return collection.removeAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return collection.retainAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            collection.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        lock.writeLock().lock();
        try {
            return collection.removeIf(filter);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        lock.readLock().lock();
        try {
            return new SyncSpliteratorAdapter<>(collection.spliterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Stream<E> stream() {
        lock.readLock().lock();
        try {
            return collection.stream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Stream<E> parallelStream() {
        lock.readLock().lock();
        try {
            return collection.parallelStream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            collection.forEach(action);
        } finally {
            lock.readLock().unlock();
        }
    }
}
