package i2f.core.interfaces.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.data.Pair;
import i2f.core.interfaces.ICombine;

/**
 * @author ltb
 * @date 2022/3/25 14:38
 * @desc
 */
@Author("i2f")
public class PairCombine<T,E> implements ICombine<T,E, Pair<T,E>> {
    @Override
    public Pair<T, E> combine(T fval, E sval) {
        return new Pair<>(fval,sval);
    }
}
