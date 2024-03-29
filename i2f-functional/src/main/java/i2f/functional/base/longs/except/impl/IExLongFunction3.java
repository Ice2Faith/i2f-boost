package i2f.functional.base.longs.except.impl;

import i2f.functional.base.longs.except.IExLongFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:30
 * @desc
 */
@FunctionalInterface
public interface IExLongFunction3<V1, V2, V3> extends IExLongFunction {
    long apply(V1 v1, V2 v2, V3 v3) throws Throwable;
}
