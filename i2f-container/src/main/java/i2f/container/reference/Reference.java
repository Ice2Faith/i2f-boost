package i2f.container.reference;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2024/4/19 9:10
 * @desc 提供一个值的性质的包装
 * 以用于表示没有值nop,终止finish,值为null,有值的区分
 */
public class Reference<E> {
    public enum State {
        VALUE, // normal value
        NOP, // for filter nop/jump tag
        FINISH; // for iterable finish tag
    }

    private E value;
    private State state = State.VALUE;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public static <E> Reference<E> nop() {
        return new Reference<>(null, State.NOP);
    }

    public static <E> Reference<E> finish() {
        return new Reference<>(null, State.FINISH);
    }

    public static <E> Reference<E> empty() {
        return new Reference<>(null, State.VALUE);
    }

    public static <E> Reference<E> of(E value) {
        return new Reference<>(value, State.VALUE);
    }

    public Reference(E value) {
        this.value = value;
    }

    public Reference(E value, State state) {
        this.value = value;
        this.state = state;
    }

    public Reference(E value, State state, ReadWriteLock lock) {
        this.value = value;
        this.state = state;
        this.lock = lock;
    }

    public E get() {
        lock.readLock().lock();
        try {
            if (!isValue()) {
                throw new IllegalStateException("current state is [" + state + "], not readable.");
            }
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void set(E value) {
        lock.writeLock().lock();
        try {
            this.value = value;
            this.state = State.VALUE;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void of(Reference<E> ref) {
        lock.writeLock().lock();
        try {
            this.value = ref.value;
            this.state = ref.state;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isNop() {
        lock.readLock().lock();
        try {
            return state == State.NOP;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void toNop() {
        lock.writeLock().lock();
        try {
            value = null;
            state = State.NOP;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isFinish() {
        lock.readLock().lock();
        try {
            return state == State.FINISH;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void toFinish() {
        lock.writeLock().lock();
        try {
            value = null;
            state = State.FINISH;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void toEmpty() {
        lock.writeLock().lock();
        try {
            value = null;
            state = State.VALUE;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isValue() {
        lock.readLock().lock();
        try {
            return state == State.VALUE;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return isValue() && value == null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean hasValue() {
        lock.readLock().lock();
        try {
            return isValue() && value != null;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reference<?> reference = (Reference<?>) o;
        return Objects.equals(value, reference.value) &&
                state == reference.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, state);
    }

    @Override
    public String toString() {
        return "Reference{" +
                "value=" + value +
                ", state=" + state +
                '}';
    }
}
