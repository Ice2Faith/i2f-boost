package i2f.functional.array.shorts.except.impl;

import i2f.functional.array.shorts.except.IExShortArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:56
 * @desc
 */
@FunctionalInterface
public interface IExShortArrayFunction2<V1,V2> extends IExShortArrayFunction {
    short[] apply(V1 v1,V2 v2) throws Throwable;
}
