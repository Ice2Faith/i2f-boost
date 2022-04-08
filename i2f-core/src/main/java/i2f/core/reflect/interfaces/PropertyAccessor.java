package i2f.core.reflect.interfaces;

import i2f.core.annotations.remark.Author;

import java.lang.reflect.Field;

/**
 * @author ltb
 * @date 2022/3/17 8:42
 * @desc
 */
@Author("i2f")
public interface PropertyAccessor extends ValueAccessor {
    void setInvokeObject(Object ivkObj);
    String getName();
    boolean writable();
    boolean readable();
    Class getType();
    Field getField();
}
