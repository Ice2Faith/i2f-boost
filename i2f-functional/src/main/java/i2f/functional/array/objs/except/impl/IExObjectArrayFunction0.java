package i2f.functional.array.objs.except.impl;

import i2f.functional.array.objs.except.IExObjectArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:02
 * @desc
 */
@FunctionalInterface
public interface IExObjectArrayFunction0<T> extends IExObjectArrayFunction {
    T[] apply() throws Throwable;
}
