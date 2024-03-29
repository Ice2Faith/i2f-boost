package i2f.functional.array.doubles;

import i2f.functional.array.IDoubleArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:43
 * @desc
 */
@FunctionalInterface
public interface IDoubleArrayFunction5<V1,V2,V3,V4,V5> extends IDoubleArrayFunction {
    double[] apply(V1 v1,V2 v2,V3 v3,V4 v4,V5 v5);
}
