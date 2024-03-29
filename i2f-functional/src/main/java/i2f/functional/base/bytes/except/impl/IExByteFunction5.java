package i2f.functional.base.bytes.except.impl;

import i2f.functional.base.bytes.except.IExByteFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:22
 * @desc
 */
@FunctionalInterface
public interface IExByteFunction5<V1, V2, V3, V4, V5> extends IExByteFunction {
    byte apply(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) throws Throwable;
}
