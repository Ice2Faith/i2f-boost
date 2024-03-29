package i2f.functional.consumer.except.impl;

import i2f.functional.consumer.except.IExConsumer;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2024/3/29 13:51
 * @desc
 */
@FunctionalInterface
public interface IExConsumer1<V1> extends IExConsumer, Serializable {
    void accept(V1 v1) throws Throwable;
}
