package i2f.functional.array.bools;

import i2f.functional.array.IBoolArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:38
 * @desc
 */
@FunctionalInterface
public interface IBoolArrayFunction2<V1,V2> extends IBoolArrayFunction {
    boolean[] apply(V1 v1,V2 v2);
}
