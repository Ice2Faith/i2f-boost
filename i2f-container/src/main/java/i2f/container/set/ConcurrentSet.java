package i2f.container.set;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/4/19 15:41
 * @desc
 * 内部使用ConcurrentHashMap实现的并发Set
 * 部分方法的使用有可能不是线程安全的
 * 使用时注意
 */
public class ConcurrentSet<E> implements Set<E> {
    private ConcurrentHashMap<E, Boolean> map = new ConcurrentHashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public ConcurrentSet() {

    }

    public ConcurrentSet(int initialCapacity, float loadFactor) {
        map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    public ConcurrentSet(int initialCapacity) {
        map = new ConcurrentHashMap<>(initialCapacity);
    }

    public ConcurrentSet(Set<? extends E> m) {
        map = new ConcurrentHashMap<>(Math.max(m.size(), 32));
        addAll(m);
    }

    public ConcurrentSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
        map = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }


    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public Object[] toArray() {
        return map.keySet().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return map.keySet().toArray(a);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, true);
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return map.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            Map<E, Boolean> tmp = new LinkedHashMap<>();
            for (E e : c) {
                tmp.put(e, true);
            }
            map.putAll(tmp);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            Set<E> tmp = new LinkedHashSet<>(map.keySet());
            tmp.retainAll(c);
            map.clear();
            return addAll(tmp);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            for (Object o : c) {
                map.remove(o);
            }
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Spliterator<E> spliterator() {
        return map.keySet().spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        lock.writeLock().lock();
        try {
            Set<E> tmp = new LinkedHashSet<>(map.keySet());
            boolean ret = false;
            for (E e : tmp) {
                if (filter.test(e)) {
                    map.remove(e);
                    ret = true;
                }
            }
            return ret;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Stream<E> stream() {
        return map.keySet().stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return map.keySet().parallelStream();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        map.keySet().forEach(action);
    }
}
