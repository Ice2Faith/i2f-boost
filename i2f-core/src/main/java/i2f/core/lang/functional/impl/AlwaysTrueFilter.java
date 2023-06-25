package i2f.core.lang.functional.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.lang.functional.common.IFilter;

/**
 * @author ltb
 * @date 2022/3/25 14:07
 * @desc
 */
@Author("i2f")
public class AlwaysTrueFilter<T> implements IFilter<T> {
    @Override
    public boolean test(T val) {
        return true;
    }
}
