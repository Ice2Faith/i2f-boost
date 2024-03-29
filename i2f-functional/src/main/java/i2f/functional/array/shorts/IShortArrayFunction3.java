package i2f.functional.array.shorts;

import i2f.functional.array.IShortArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:56
 * @desc
 */
@FunctionalInterface
public interface IShortArrayFunction3<V1,V2,V3> extends IShortArrayFunction {
    short[] apply(V1 v1,V2 v2,V3 v3);
}
