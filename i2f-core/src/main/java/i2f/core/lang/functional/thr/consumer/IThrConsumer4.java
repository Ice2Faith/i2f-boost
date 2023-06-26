package i2f.core.lang.functional.thr.consumer;

import i2f.core.lang.functional.thr.IThrConsumer;

/**
 * @author ltb
 * @date 2022/11/16 10:14
 * @desc
 */
@FunctionalInterface
public interface IThrConsumer4<V1, V2, V3, V4> extends IThrConsumer {
    void accept(V1 v1, V2 v2, V3 v3, V4 v4) throws Throwable;
}
