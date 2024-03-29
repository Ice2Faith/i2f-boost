package i2f.functional.array.longs;

import i2f.functional.array.ILongArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:51
 * @desc
 */
@FunctionalInterface
public interface ILongArrayFunction1<V1> extends ILongArrayFunction {
    long[] apply(V1 v1);
}
