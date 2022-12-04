package i2f.core.functional.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.functional.common.IMapper;

/**
 * @author ltb
 * @date 2022/3/25 14:12
 * @desc
 */
@Author("i2f")
public class StringMapper<T> implements IMapper<String, T> {
    private boolean null2Empty;

    public StringMapper() {
        this.null2Empty = false;
    }

    public StringMapper(boolean null2Empty) {
        this.null2Empty = null2Empty;
    }

    @Override
    public String get(T val) {
        if (val == null && null2Empty) {
            return "";
        }
        return String.valueOf(val);
    }
}
