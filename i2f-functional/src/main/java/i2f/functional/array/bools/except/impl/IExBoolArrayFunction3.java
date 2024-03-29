package i2f.functional.array.bools.except.impl;

import i2f.functional.array.bools.except.IExBoolArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:38
 * @desc
 */
@FunctionalInterface
public interface IExBoolArrayFunction3<V1, V2, V3> extends IExBoolArrayFunction {
    boolean[] apply(V1 v1, V2 v2, V3 v3) throws Throwable;
}
