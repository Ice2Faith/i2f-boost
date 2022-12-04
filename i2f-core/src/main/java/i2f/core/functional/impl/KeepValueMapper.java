package i2f.core.functional.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.functional.common.IMapper;

/**
 * @author ltb
 * @date 2022/3/25 15:03
 * @desc
 */
@Author("i2f")
public class KeepValueMapper<R, T> implements IMapper<R, T> {
    @Override
    public R get(T val) {
        return (R) val;
    }
}
