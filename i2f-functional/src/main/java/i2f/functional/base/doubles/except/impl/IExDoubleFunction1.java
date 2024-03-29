package i2f.functional.base.doubles.except.impl;

import i2f.functional.base.doubles.except.IExDoubleFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:27
 * @desc
 */
@FunctionalInterface
public interface IExDoubleFunction1<V1> extends IExDoubleFunction {
    double apply(V1 v1) throws Throwable;
}
