package i2f.functional.array.floats;

import i2f.functional.array.IFloatArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:45
 * @desc
 */
@FunctionalInterface
public interface IFloatArrayFunction2<V1,V2> extends IFloatArrayFunction {
    float[] apply(V1 v1,V2 v2);
}
