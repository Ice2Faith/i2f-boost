package i2f.functional.array.bools.except.impl;

import i2f.functional.array.bools.except.IExBoolArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:38
 * @desc
 */
@FunctionalInterface
public interface IExBoolArrayFunction5<V1,V2,V3,V4,V5> extends IExBoolArrayFunction {
    boolean[] apply(V1 v1,V2 v2,V3 v3,V4 v4,V5 v5) throws Throwable;
}
