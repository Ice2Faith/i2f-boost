package i2f.functional.array.shorts;

import i2f.functional.array.IShortArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:56
 * @desc
 */
@FunctionalInterface
public interface IShortArrayFunction1<V1> extends IShortArrayFunction {
    short[] apply(V1 v1);
}
