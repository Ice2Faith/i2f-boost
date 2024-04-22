package i2f.container.sync.adapter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/4/18 17:32
 * @desc
 */
public class SyncSetAdapter<E> implements Set<E> {
    private Set<E> set;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncSetAdapter(Set<E> set) {
        this.set = set;
    }

    public SyncSetAdapter(Set<E> set, ReadWriteLock lock) {
        this.set = set;
        this.lock = lock;
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return set.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return set.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.readLock().lock();
        try {
            return set.contains(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        lock.readLock().lock();
        try {
            return new SyncIteratorAdapter<>(set.iterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Object[] toArray() {
        lock.readLock().lock();
        try {
            return set.toArray();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        lock.readLock().lock();
        try {
            return set.toArray(a);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean add(E e) {
        lock.writeLock().lock();
        try {
            return set.add(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.writeLock().lock();
        try {
            return set.remove(o);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.readLock().lock();
        try {
            return set.containsAll(c);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return set.addAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return set.retainAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return set.removeAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            set.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        lock.readLock().lock();
        try {
            return new SyncSpliteratorAdapter<>(set.spliterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        lock.writeLock().lock();
        try {
            return set.removeIf(filter);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Stream<E> stream() {
        lock.readLock().lock();
        try {
            return set.stream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Stream<E> parallelStream() {
        lock.readLock().lock();
        try {
            return set.parallelStream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            set.forEach(action);
        } finally {
            lock.readLock().unlock();
        }
    }
}
