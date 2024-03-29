package i2f.functional.array.shorts;

import i2f.functional.array.IShortArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:56
 * @desc
 */
@FunctionalInterface
public interface IShortArrayFunction5<V1,V2,V3,V4,V5> extends IShortArrayFunction {
    short[] apply(V1 v1,V2 v2,V3 v3,V4 v4,V5 v5);
}
