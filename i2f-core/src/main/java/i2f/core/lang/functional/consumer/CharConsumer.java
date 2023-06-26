package i2f.core.lang.functional.consumer;

import i2f.core.lang.functional.IConsumer;

/**
 * @author ltb
 * @date 2022/11/16 10:14
 * @desc
 */
@FunctionalInterface
public interface CharConsumer extends IConsumer {
    void accept(char val);
}
