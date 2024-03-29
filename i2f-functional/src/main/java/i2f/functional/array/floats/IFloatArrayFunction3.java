package i2f.functional.array.floats;

import i2f.functional.array.IFloatArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:45
 * @desc
 */
@FunctionalInterface
public interface IFloatArrayFunction3<V1,V2,V3> extends IFloatArrayFunction {
    float[] apply(V1 v1,V2 v2,V3 v3);
}
