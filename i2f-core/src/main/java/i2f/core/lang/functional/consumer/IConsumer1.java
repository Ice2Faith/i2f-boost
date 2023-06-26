package i2f.core.lang.functional.consumer;

import i2f.core.lang.functional.IConsumer;

import java.util.function.Consumer;

/**
 * @author ltb
 * @date 2022/11/16 10:14
 * @desc
 */
@FunctionalInterface
public interface IConsumer1<V1> extends IConsumer, Consumer<V1> {

}
