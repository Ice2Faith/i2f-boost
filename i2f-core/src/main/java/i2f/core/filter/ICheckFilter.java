package i2f.core.filter;

/**
 * @author ltb
 * @date 2022/4/27 8:50
 * @desc unify check filter
 * use for check an element whether exist
 * direct implements is BloomFilter
 * exist cloud check element had been marked before.
 */
public interface ICheckFilter<T> {
    void mark(T obj);
    boolean exists(T obj);
}
