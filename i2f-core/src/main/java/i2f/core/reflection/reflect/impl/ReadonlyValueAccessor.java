package i2f.core.reflection.reflect.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.reflection.reflect.exception.ReadOnlyAccessException;
import i2f.core.reflection.reflect.interfaces.ValueAccessor;

/**
 * @author ltb
 * @date 2022/3/14 9:28
 * @desc
 */
@Author("i2f")
public class ReadonlyValueAccessor implements ValueAccessor {
    public Object obj;
    public ReadonlyValueAccessor(Object obj) {
        this.obj=obj;
    }

    @Override
    public Object get() {
        return obj;
    }

    @Override
    public void set(Object obj) {
        throw new ReadOnlyAccessException("object is readonly cannot be set.");
    }
}
