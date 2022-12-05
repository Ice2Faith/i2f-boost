package i2f.core.functional.consumer;

import i2f.core.functional.IConsumer;

import java.util.function.BiConsumer;

/**
 * @author ltb
 * @date 2022/11/16 10:14
 * @desc
 */
@FunctionalInterface
public interface IConsumer2<V1, V2> extends IConsumer, BiConsumer<V1, V2> {
    void accept(V1 v1, V2 v2);
}