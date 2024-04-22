package i2f.container.sync.adapter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/4/18 15:17
 * @desc
 */
public class SyncQueueAdapter<E> implements Queue<E> {
    private Queue<E> queue;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncQueueAdapter(Queue<E> queue) {
        this.queue = queue;
    }

    public SyncQueueAdapter(Queue<E> queue, ReadWriteLock lock) {
        this.queue = queue;
        this.lock = lock;
    }

    @Override
    public boolean add(E e) {
        lock.writeLock().lock();
        try {
            return queue.add(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean offer(E e) {
        lock.writeLock().lock();
        try {
            return queue.offer(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E remove() {
        lock.writeLock().lock();
        try {
            return queue.remove();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E poll() {
        lock.writeLock().lock();
        try {
            return queue.poll();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E element() {
        lock.readLock().lock();
        try {
            return queue.element();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E peek() {
        lock.readLock().lock();
        try {
            return queue.peek();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return queue.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.readLock().lock();
        try {
            return queue.contains(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        lock.readLock().lock();
        try {
            return new SyncIteratorAdapter<>(queue.iterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Object[] toArray() {
        lock.readLock().lock();
        try {
            return queue.toArray();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        lock.readLock().lock();
        try {
            return queue.toArray(a);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.writeLock().lock();
        try {
            return queue.remove(o);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.readLock().lock();
        try {
            return queue.containsAll(c);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return queue.addAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return queue.removeAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return queue.retainAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            queue.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        lock.readLock().lock();
        try {
            return queue.equals(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int hashCode() {
        lock.readLock().lock();
        try {
            return queue.hashCode();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        lock.writeLock().lock();
        try {
            return queue.removeIf(filter);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        lock.readLock().lock();
        try {
            return new SyncSpliteratorAdapter<>(queue.spliterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Stream<E> stream() {
        lock.readLock().lock();
        try {
            return queue.stream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Stream<E> parallelStream() {
        lock.readLock().lock();
        try {
            return queue.parallelStream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            queue.forEach(action);
        } finally {
            lock.readLock().unlock();
        }
    }
}
