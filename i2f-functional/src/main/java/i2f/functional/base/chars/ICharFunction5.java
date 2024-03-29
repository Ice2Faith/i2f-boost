package i2f.functional.base.chars;

import i2f.functional.base.ICharFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:24
 * @desc
 */
@FunctionalInterface
public interface ICharFunction5<V1, V2, V3, V4, V5> extends ICharFunction {
    char apply(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5);
}
