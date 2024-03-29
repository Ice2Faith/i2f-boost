package i2f.functional.array.chars.except.impl;

import i2f.functional.array.chars.except.IExCharArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:41
 * @desc
 */
@FunctionalInterface
public interface IExCharArrayFunction2<V1,V2> extends IExCharArrayFunction {
    char[] apply(V1 v1,V2 v2) throws Throwable;
}
