package i2f.core.hash;

/**
 * @author ltb
 * @date 2022/4/27 8:56
 * @desc
 */
public class ObjectHashcodeHashProvider<T> implements IHashProvider<T>{
    @Override
    public long hash(T obj) {
        return obj.hashCode();
    }
}
