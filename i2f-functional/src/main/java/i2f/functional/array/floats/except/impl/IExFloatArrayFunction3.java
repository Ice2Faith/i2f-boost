package i2f.functional.array.floats.except.impl;

import i2f.functional.array.floats.except.IExFloatArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:45
 * @desc
 */
@FunctionalInterface
public interface IExFloatArrayFunction3<V1,V2,V3> extends IExFloatArrayFunction {
    float[] apply(V1 v1,V2 v2,V3 v3) throws Throwable;
}
