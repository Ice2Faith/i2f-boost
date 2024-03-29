package i2f.functional.array.ints;

import i2f.functional.array.IIntArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:48
 * @desc
 */
@FunctionalInterface
public interface IIntArrayFunction3<V1,V2,V3> extends IIntArrayFunction {
    int[] apply(V1 v1,V2 v2,V3 v3);
}
