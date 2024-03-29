package i2f.functional.array.chars;

import i2f.functional.array.ICharArrayFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:41
 * @desc
 */
@FunctionalInterface
public interface ICharArrayFunction3<V1,V2,V3> extends ICharArrayFunction {
    char[] apply(V1 v1,V2 v2,V3 v3);
}
