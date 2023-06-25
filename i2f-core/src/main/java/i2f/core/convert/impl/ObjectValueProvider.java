package i2f.core.convert.impl;

import i2f.core.convert.IValueProvider;
import i2f.core.reflection.reflect.ValueResolver;

/**
 * @author ltb
 * @date 2022/3/27 18:37
 * @desc
 */
public class ObjectValueProvider<T> implements IValueProvider<T,String> {
    @Override
    public Object get(T obj, String key) {
        return ValueResolver.get(obj,key);
    }
}
