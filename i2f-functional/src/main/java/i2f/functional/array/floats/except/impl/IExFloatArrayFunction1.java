package i2f.functional.array.floats.except.impl;

import i2f.functional.array.floats.except.IExFloatArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:45
 * @desc
 */
@FunctionalInterface
public interface IExFloatArrayFunction1<V1> extends IExFloatArrayFunction {
    float[] apply(V1 v1) throws Throwable;
}
