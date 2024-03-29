package i2f.functional.base.shorts;

import i2f.functional.base.IShortFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:32
 * @desc
 */
@FunctionalInterface
public interface IShortFunction2<V1,V2> extends IShortFunction {
    short apply(V1 v1,V2 v2);
}
