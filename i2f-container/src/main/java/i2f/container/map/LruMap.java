package i2f.container.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/4/19 15:25
 * @desc
 * 基于LInkedHashMap的LRU实现
 * 这不是线程安全的
 */
public class LruMap<K, V> extends LinkedHashMap<K, V> {

    protected int maxSize;

    public LruMap(int initialCapacity, float loadFactor, int maxSize) {
        super(initialCapacity, loadFactor);
        this.maxSize = maxSize;
    }

    public LruMap(int initialCapacity, int maxSize) {
        super(initialCapacity);
        this.maxSize = maxSize;
    }

    public LruMap(int maxSize) {
        this.maxSize = maxSize;
    }

    public LruMap(Map<? extends K, ? extends V> m, int maxSize) {
        super(m);
        this.maxSize = maxSize;
    }

    public LruMap(int initialCapacity, float loadFactor, boolean accessOrder, int maxSize) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
