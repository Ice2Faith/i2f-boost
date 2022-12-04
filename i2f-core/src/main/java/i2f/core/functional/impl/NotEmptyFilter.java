package i2f.core.functional.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.functional.common.IFilter;

/**
 * @author ltb
 * @date 2022/3/25 14:07
 * @desc
 */
@Author("i2f")
public class NotEmptyFilter<T> implements IFilter<T> {
    @Override
    public boolean test(T val) {
        return !(val == null || "".equals(val));
    }
}
