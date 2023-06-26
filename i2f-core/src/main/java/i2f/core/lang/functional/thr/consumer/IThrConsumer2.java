package i2f.core.lang.functional.thr.consumer;

import i2f.core.lang.functional.thr.IThrConsumer;

/**
 * @author ltb
 * @date 2022/11/16 10:14
 * @desc
 */
@FunctionalInterface
public interface IThrConsumer2<V1, V2> extends IThrConsumer {
    void accept(V1 v1, V2 v2) throws Throwable;
}
