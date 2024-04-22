package i2f.container.sync.adapter;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/4/18 14:34
 * @desc
 */
public class SyncSpliteratorAdapter<E> implements Spliterator<E> {
    private Spliterator<E> spliterator;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncSpliteratorAdapter(Spliterator<E> spliterator) {
        this.spliterator = spliterator;
    }

    public SyncSpliteratorAdapter(Spliterator<E> spliterator, ReadWriteLock lock) {
        this.spliterator = spliterator;
        this.lock = lock;
    }

    @Override
    public boolean tryAdvance(Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            return spliterator.tryAdvance(action);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Spliterator<E> trySplit() {
        lock.writeLock().lock();
        try {
            return spliterator.trySplit();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public long estimateSize() {
        lock.readLock().lock();
        try {
            return spliterator.estimateSize();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int characteristics() {
        lock.readLock().lock();
        try {
            return spliterator.characteristics();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            spliterator.forEachRemaining(action);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public long getExactSizeIfKnown() {
        lock.readLock().lock();
        try {
            return spliterator.getExactSizeIfKnown();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        lock.readLock().lock();
        try {
            return spliterator.hasCharacteristics(characteristics);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Comparator<? super E> getComparator() {
        lock.readLock().lock();
        try {
            return spliterator.getComparator();
        } finally {
            lock.readLock().unlock();
        }
    }
}
