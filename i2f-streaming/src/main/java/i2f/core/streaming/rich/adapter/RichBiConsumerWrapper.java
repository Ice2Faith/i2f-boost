package i2f.core.streaming.rich.adapter;

import i2f.core.streaming.rich.RichStreamingWrapper;

import java.util.function.BiConsumer;

/**
 * @author Ice2Faith
 * @date 2023/5/1 23:38
 * @desc
 */
public abstract class RichBiConsumerWrapper<T, U> extends RichStreamingWrapper implements BiConsumer<T, U> {
}
