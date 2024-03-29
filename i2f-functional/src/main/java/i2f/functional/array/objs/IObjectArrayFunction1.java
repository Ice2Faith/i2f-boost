package i2f.functional.array.objs;

import i2f.functional.array.IObjectArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:02
 * @desc
 */
@FunctionalInterface
public interface IObjectArrayFunction1<T,V1> extends IObjectArrayFunction {
    T[] apply(V1 v1);
}
