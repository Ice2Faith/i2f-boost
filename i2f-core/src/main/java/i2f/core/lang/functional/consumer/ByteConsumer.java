package i2f.core.lang.functional.consumer;

import i2f.core.lang.functional.IConsumer;

/**
 * @author ltb
 * @date 2022/11/16 10:14
 * @desc
 */
@FunctionalInterface
public interface ByteConsumer extends IConsumer {
    void accept(byte val);
}
