package i2f.core.streaming.keyed.functional;

/**
 * @author Ice2Faith
 * @date 2023/5/2 16:23
 * @desc
 */
@FunctionalInterface
public interface KeyedComparator<K, T> {
    int compare(T o1, T o2, K key);
}
