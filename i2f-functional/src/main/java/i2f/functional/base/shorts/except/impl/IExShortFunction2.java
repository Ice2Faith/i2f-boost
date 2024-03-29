package i2f.functional.base.shorts.except.impl;

import i2f.functional.base.shorts.except.IExShortFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:32
 * @desc
 */
@FunctionalInterface
public interface IExShortFunction2<V1,V2> extends IExShortFunction {
    short apply(V1 v1,V2 v2) throws Throwable;
}
