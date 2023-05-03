package i2f.core.streaming.impl;

import i2f.core.streaming.Streaming;

/**
 * @author Ice2Faith
 * @date 2023/4/22 21:41
 * @desc
 */
public interface NumberStreaming<E extends Number> extends Streaming<E> {

    E sum();

    E avg();

}
