package i2f.core.lang.functional.thr.consumer;

import i2f.core.lang.functional.thr.IThrConsumer;

/**
 * @author ltb
 * @date 2022/11/16 10:14
 * @desc
 */
@FunctionalInterface
public interface ThrCharConsumer extends IThrConsumer {
    void accept(char val) throws Throwable;
}