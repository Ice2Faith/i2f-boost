package i2f.functional.func.impl;

import i2f.functional.func.IFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:08
 * @desc
 */
@FunctionalInterface
public interface IFunction4<R, V1, V2, V3, V4> extends IFunction {
    R apply(V1 v1, V2 v2, V3 v3, V4 v4);
}
