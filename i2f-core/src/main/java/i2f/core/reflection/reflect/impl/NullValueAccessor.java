package i2f.core.reflection.reflect.impl;


import i2f.core.annotations.remark.Author;
import i2f.core.reflection.reflect.exception.NullAccessException;
import i2f.core.reflection.reflect.interfaces.ValueAccessor;

/**
 * @author ltb
 * @date 2022/3/14 9:28
 * @desc
 */
@Author("i2f")
public class NullValueAccessor implements ValueAccessor {
    @Override
    public Object get() {
        return null;
    }

    @Override
    public void set(Object obj) {
        throw new NullAccessException("object is null cannot be set.");
    }
}
