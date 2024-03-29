package i2f.functional.consumer.except.impl;

import i2f.functional.consumer.except.IExConsumer;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IExConsumer3<V1, V2, V3> extends IExConsumer {
    void accept(V1 v1, V2 v2, V3 v3) throws Exception;
}
