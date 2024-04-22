package i2f.container.map;

import i2f.container.sync.adapter.SyncLinkedHashMapAdapter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2024/4/19 15:39
 * @desc
 * 对LruMap的线程安全的包装
 */
public class SyncLruMap<K, V> extends SyncLinkedHashMapAdapter<K, V> {
    private SyncLruMap(LinkedHashMap<K, V> map) {
        super(map);
    }

    private SyncLruMap(LinkedHashMap<K, V> map, ReadWriteLock lock) {
        super(map, lock);
    }


    public SyncLruMap(int initialCapacity, float loadFactor, int maxSize) {
        this(new LruMap<>(initialCapacity, loadFactor, maxSize));
    }

    public SyncLruMap(int initialCapacity, int maxSize) {
        this(new LruMap<>(initialCapacity, maxSize));
    }

    public SyncLruMap(int maxSize) {
        this(new LruMap<>(maxSize));
    }

    public SyncLruMap(Map<? extends K, ? extends V> m, int maxSize) {
        this(new LruMap<>(m, maxSize));
    }

    public SyncLruMap(int initialCapacity, float loadFactor, boolean accessOrder, int maxSize) {
        this(new LruMap<>(initialCapacity, loadFactor, accessOrder, maxSize));
    }

}
