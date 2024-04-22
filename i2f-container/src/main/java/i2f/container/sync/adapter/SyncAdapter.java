package i2f.container.sync.adapter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/18 17:41
 * @desc
 */
public class SyncAdapter<T> {
    private T target;
    private Lock lock = new ReentrantLock();

    public SyncAdapter(T target) {
        this.target = target;
    }

    public SyncAdapter(T target, Lock lock) {
        this.target = target;
        this.lock = lock;
    }

    public <R> R map(Function<T, R> wrapper) {
        lock.lock();
        try {
            return wrapper.apply(target);
        } finally {
            lock.unlock();
        }
    }

    public <V, R> R biMap(BiFunction<T, V, R> wrapper, V val) {
        lock.lock();
        try {
            return wrapper.apply(target, val);
        } finally {
            lock.unlock();
        }
    }

    public void accept(Consumer<T> consumer) {
        lock.lock();
        try {
            consumer.accept(target);
        } finally {
            lock.unlock();
        }
    }

    public <V> void accept(BiConsumer<T, V> consumer, V val) {
        lock.lock();
        try {
            consumer.accept(target, val);
        } finally {
            lock.unlock();
        }
    }
}
