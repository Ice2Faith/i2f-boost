package i2f.core.functional.consumer;

import i2f.core.functional.IConsumer;

/**
 * @author ltb
 * @date 2022/11/16 10:14
 * @desc
 */
@FunctionalInterface
public interface IConsumer4<V1, V2, V3, V4> extends IConsumer {
    void accept(V1 v1, V2 v2, V3 v3, V4 v4);
}
