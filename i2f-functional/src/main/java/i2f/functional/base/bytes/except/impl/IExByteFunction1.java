package i2f.functional.base.bytes.except.impl;

import i2f.functional.base.bytes.except.IExByteFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:22
 * @desc
 */
@FunctionalInterface
public interface IExByteFunction1<V1> extends IExByteFunction {
    byte apply(V1 v1) throws Throwable;
}
