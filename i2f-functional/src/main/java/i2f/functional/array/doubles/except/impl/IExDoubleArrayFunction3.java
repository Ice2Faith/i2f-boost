package i2f.functional.array.doubles.except.impl;

import i2f.functional.array.doubles.except.IExDoubleArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:43
 * @desc
 */
@FunctionalInterface
public interface IExDoubleArrayFunction3<V1,V2,V3> extends IExDoubleArrayFunction {
    double[] apply(V1 v1,V2 v2,V3 v3) throws Throwable;
}
