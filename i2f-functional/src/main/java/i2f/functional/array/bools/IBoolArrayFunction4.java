package i2f.functional.array.bools;

import i2f.functional.array.IBoolArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:38
 * @desc
 */
@FunctionalInterface
public interface IBoolArrayFunction4<V1,V2,V3,V4> extends IBoolArrayFunction {
    boolean[] apply(V1 v1,V2 v2,V3 v3,V4 v4);
}
