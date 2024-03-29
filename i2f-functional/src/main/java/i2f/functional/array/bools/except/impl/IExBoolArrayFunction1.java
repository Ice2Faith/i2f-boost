package i2f.functional.array.bools.except.impl;

import i2f.functional.array.bools.except.IExBoolArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:38
 * @desc
 */
@FunctionalInterface
public interface IExBoolArrayFunction1<V1> extends IExBoolArrayFunction {
    boolean[] apply(V1 v1) throws Throwable;
}
