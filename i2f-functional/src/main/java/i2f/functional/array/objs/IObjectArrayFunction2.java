package i2f.functional.array.objs;

import i2f.functional.array.IObjectArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:02
 * @desc
 */
@FunctionalInterface
public interface IObjectArrayFunction2<T,V1,V2> extends IObjectArrayFunction {
    T[] apply(V1 v1,V2 v2);
}
