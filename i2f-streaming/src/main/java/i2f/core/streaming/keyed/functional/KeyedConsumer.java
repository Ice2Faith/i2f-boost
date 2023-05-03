package i2f.core.streaming.keyed.functional;

/**
 * @author Ice2Faith
 * @date 2023/5/2 16:23
 * @desc
 */
@FunctionalInterface
public interface KeyedConsumer<K, T> {
    void accept(T elem, K key);
}
