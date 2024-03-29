package i2f.functional.array.chars.except.impl;

import i2f.functional.array.chars.except.IExCharArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:41
 * @desc
 */
@FunctionalInterface
public interface IExCharArrayFunction1<V1> extends IExCharArrayFunction {
    char[] apply(V1 v1) throws Throwable;
}
