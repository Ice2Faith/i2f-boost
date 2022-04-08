package i2f.core.reflect.interfaces;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2022/3/14 9:26
 * @desc
 */
@Author("i2f")
public interface ValueAccessor {
    Object get();
    void set(Object obj);
}
