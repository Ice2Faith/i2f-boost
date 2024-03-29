package i2f.functional.array.ints.except.impl;

import i2f.functional.array.ints.except.IExIntArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:48
 * @desc
 */
@FunctionalInterface
public interface IExIntArrayFunction1<V1> extends IExIntArrayFunction {
    int[] apply(V1 v1) throws Throwable;
}
