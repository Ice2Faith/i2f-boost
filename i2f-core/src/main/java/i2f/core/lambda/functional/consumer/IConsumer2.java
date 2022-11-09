package i2f.core.lambda.functional.consumer;

import i2f.core.lambda.functional.IFunction;

/**
 * @author ltb
 * @date 2022/11/9 12:00
 * @desc
 */
@FunctionalInterface
public interface IConsumer2<V1, V2> extends IFunction {
    void accept(V1 v1, V2 v2);
}
