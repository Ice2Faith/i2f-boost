package i2f.functional.base.chars.except.impl;

import i2f.functional.base.chars.except.IExCharFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:24
 * @desc
 */
@FunctionalInterface
public interface IExCharFunction1<V1> extends IExCharFunction {
    char apply(V1 v1) throws Throwable;
}
