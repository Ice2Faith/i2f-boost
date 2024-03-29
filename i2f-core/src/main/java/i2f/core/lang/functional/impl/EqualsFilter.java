package i2f.core.lang.functional.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.lang.functional.common.IFilter;

/**
 * @author ltb
 * @date 2022/3/25 15:20
 * @desc
 */
@Author("i2f")
public class EqualsFilter<T> implements IFilter<T> {
    private T cmp;

    public EqualsFilter(T cmp) {
        this.cmp = cmp;
    }

    @Override
    public boolean test(T val) {
        if (cmp == val) {
            return true;
        }
        if (cmp != null) {
            return cmp.equals(val);
        }
        return val.equals(cmp);
    }
}
