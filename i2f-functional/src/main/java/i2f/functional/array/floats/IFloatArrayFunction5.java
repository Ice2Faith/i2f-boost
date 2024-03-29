package i2f.functional.array.floats;

import i2f.functional.array.IFloatArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:45
 * @desc
 */
@FunctionalInterface
public interface IFloatArrayFunction5<V1,V2,V3,V4,V5> extends IFloatArrayFunction {
    float[] apply(V1 v1,V2 v2,V3 v3,V4 v4,V5 v5);
}
