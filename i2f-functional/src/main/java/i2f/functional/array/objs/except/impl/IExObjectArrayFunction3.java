package i2f.functional.array.objs.except.impl;

import i2f.functional.array.objs.except.IExObjectArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:02
 * @desc
 */
@FunctionalInterface
public interface IExObjectArrayFunction3<T,V1,V2,V3> extends IExObjectArrayFunction {
    T[] apply(V1 v1,V2 v2,V3 v3) throws Throwable;
}
