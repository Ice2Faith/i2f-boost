package i2f.core.convert;

/**
 * @author ltb
 * @date 2022/3/27 18:24
 * @desc
 */
public interface IValueProvider<T,K> {
    Object get(T obj,K key);
}
