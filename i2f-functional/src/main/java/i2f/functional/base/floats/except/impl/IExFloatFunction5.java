package i2f.functional.base.floats.except.impl;

import i2f.functional.base.floats.except.IExFloatFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:28
 * @desc
 */
@FunctionalInterface
public interface IExFloatFunction5<V1,V2,V3,V4,V5> extends IExFloatFunction {
    float apply(V1 v1,V2 v2,V3 v3,V4 v4,V5 v5) throws Throwable;
}
