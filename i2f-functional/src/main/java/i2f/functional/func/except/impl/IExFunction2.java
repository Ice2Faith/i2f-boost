package i2f.functional.func.except.impl;

import i2f.functional.func.except.IExFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:08
 * @desc
 */
@FunctionalInterface
public interface IExFunction2<R, V1, V2> extends IExFunction {
    R apply(V1 v1, V2 v2) throws Throwable;
}
