package i2f.core.lambda.functional.provider;

import i2f.core.lambda.functional.IFunction;

/**
 * @author ltb
 * @date 2022/11/9 12:04
 * @desc
 */
@FunctionalInterface
public interface IProvider1<R, V1> extends IFunction {
    R pick(V1 v1);
}
