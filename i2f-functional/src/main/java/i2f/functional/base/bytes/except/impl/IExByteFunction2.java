package i2f.functional.base.bytes.except.impl;

import i2f.functional.base.bytes.except.IExByteFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:22
 * @desc
 */
@FunctionalInterface
public interface IExByteFunction2<V1, V2> extends IExByteFunction {
    byte apply(V1 v1, V2 v2) throws Throwable;
}
