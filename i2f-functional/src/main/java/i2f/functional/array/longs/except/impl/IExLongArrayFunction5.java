package i2f.functional.array.longs.except.impl;

import i2f.functional.array.longs.except.IExLongArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:51
 * @desc
 */
@FunctionalInterface
public interface IExLongArrayFunction5<V1,V2,V3,V4,V5> extends IExLongArrayFunction {
    long[] apply(V1 v1,V2 v2,V3 v3,V4 v4,V5 v5) throws Throwable;
}
