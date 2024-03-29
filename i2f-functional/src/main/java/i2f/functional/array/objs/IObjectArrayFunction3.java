package i2f.functional.array.objs;

import i2f.functional.array.IObjectArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 15:02
 * @desc
 */
@FunctionalInterface
public interface IObjectArrayFunction3<T,V1,V2,V3> extends IObjectArrayFunction {
    T[] apply(V1 v1,V2 v2,V3 v3);
}
