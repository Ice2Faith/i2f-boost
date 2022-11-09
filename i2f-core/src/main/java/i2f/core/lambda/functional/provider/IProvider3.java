package i2f.core.lambda.functional.provider;

import i2f.core.lambda.functional.IFunction;

/**
 * @author ltb
 * @date 2022/11/9 12:04
 * @desc
 */
@FunctionalInterface
public interface IProvider3<R, V1, V2, V3> extends IFunction {
    R pick(V1 v1, V2 v2, V3 v3);
}
