package i2f.functional.consumer.except.impl;

import i2f.functional.consumer.except.IExConsumer;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IExConsumer5<V1, V2, V3, V4, V5> extends IExConsumer {
    void accept(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) throws Throwable;
}
