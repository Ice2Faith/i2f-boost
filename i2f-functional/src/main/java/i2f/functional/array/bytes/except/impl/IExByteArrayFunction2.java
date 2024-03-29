package i2f.functional.array.bytes.except.impl;

import i2f.functional.array.bytes.except.IExByteArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:40
 * @desc
 */
@FunctionalInterface
public interface IExByteArrayFunction2<V1,V2> extends IExByteArrayFunction {
    byte[] apply(V1 v1,V2 v2) throws Throwable;
}
