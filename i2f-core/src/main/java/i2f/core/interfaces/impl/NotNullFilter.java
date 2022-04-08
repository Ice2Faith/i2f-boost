package i2f.core.interfaces.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.interfaces.IFilter;

/**
 * @author ltb
 * @date 2022/3/25 14:07
 * @desc
 */
@Author("i2f")
public class NotNullFilter<T> implements IFilter<T> {
    @Override
    public boolean choice(T val) {
        return val!=null;
    }
}
