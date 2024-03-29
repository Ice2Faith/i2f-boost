package i2f.functional.base.longs.except.impl;

import i2f.functional.base.longs.except.IExLongFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:30
 * @desc
 */
@FunctionalInterface
public interface IExLongFunction2<V1, V2> extends IExLongFunction {
    long apply(V1 v1, V2 v2) throws Throwable;
}
