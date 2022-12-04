package i2f.core.functional.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.data.Pair;
import i2f.core.functional.jvf.BiFunction;

/**
 * @author ltb
 * @date 2022/3/25 14:38
 * @desc
 */
@Author("i2f")
public class PairCombine<T, E> implements BiFunction<Pair<T, E>, T, E> {
    @Override
    public Pair<T, E> get(T fval, E sval) {
        return new Pair<>(fval, sval);
    }
}
