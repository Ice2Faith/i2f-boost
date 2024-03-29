package i2f.functional.array.shorts.except.impl;

import i2f.functional.array.shorts.except.IExShortArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:56
 * @desc
 */
@FunctionalInterface
public interface IExShortArrayFunction3<V1,V2,V3> extends IExShortArrayFunction {
    short[] apply(V1 v1,V2 v2,V3 v3) throws Throwable;
}
