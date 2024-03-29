package i2f.functional.array.doubles;

import i2f.functional.array.IDoubleArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:43
 * @desc
 */
@FunctionalInterface
public interface IDoubleArrayFunction1<V1> extends IDoubleArrayFunction {
    double[] apply(V1 v1);
}
