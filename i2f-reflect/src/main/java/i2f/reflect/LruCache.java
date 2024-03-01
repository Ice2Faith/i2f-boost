package i2f.reflect;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2024/2/29 9:14
 * @desc
 */
public class LruCache<K,V> extends LinkedHashMap<K,V> {

    protected int maxCount= Integer.MAX_VALUE;

    public LruCache(int initialCapacity, float loadFactor, int maxCount) {
        super(initialCapacity, loadFactor);
        this.maxCount = maxCount;
    }

    public LruCache(int initialCapacity, int maxCount) {
        super(initialCapacity);
        this.maxCount = maxCount;
    }

    public LruCache(int maxCount) {
        super(maxCount);
        this.maxCount = maxCount;
    }

    public LruCache(Map<? extends K, ? extends V> m, int maxCount) {
        super(m);
        this.maxCount = maxCount;
    }

    public LruCache(int initialCapacity, float loadFactor, boolean accessOrder, int maxCount) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxCount = maxCount;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size()>maxCount;
    }

}
