package i2f.core.lambda.functional.provider;

import i2f.core.lambda.functional.IFunction;

/**
 * @author ltb
 * @date 2022/11/9 12:04
 * @desc
 */
@FunctionalInterface
public interface IProvider7<R, V1, V2, V3, V4, V5, V6, V7> extends IFunction {
    R pick(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7);
}
