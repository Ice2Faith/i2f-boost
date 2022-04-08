package i2f.core.interfaces.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.interfaces.IMap;

/**
 * @author ltb
 * @date 2022/3/25 15:03
 * @desc
 */
@Author("i2f")
public class KeepValueMapper<T,E> implements IMap<T,E> {
    @Override
    public E map(T val) {
        return (E)val;
    }
}
