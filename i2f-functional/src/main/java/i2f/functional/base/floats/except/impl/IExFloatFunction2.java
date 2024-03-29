package i2f.functional.base.floats.except.impl;

import i2f.functional.base.floats.except.IExFloatFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:28
 * @desc
 */
@FunctionalInterface
public interface IExFloatFunction2<V1,V2> extends IExFloatFunction {
    float apply(V1 v1,V2 v2) throws Throwable;
}
