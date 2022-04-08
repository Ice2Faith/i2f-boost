package i2f.core.convert;

/**
 * @author ltb
 * @date 2022/3/27 18:17
 * @desc
 */
public interface IKeyProvider<T,E> {
    T getKey(E obj);
}
