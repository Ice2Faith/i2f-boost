package i2f.functional.array.bytes;

import i2f.functional.array.IByteArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:40
 * @desc
 */
@FunctionalInterface
public interface IByteArrayFunction1<V1> extends IByteArrayFunction {
    byte[] apply(V1 v1);
}
