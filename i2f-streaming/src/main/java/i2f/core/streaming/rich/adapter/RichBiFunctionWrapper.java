package i2f.core.streaming.rich.adapter;

import i2f.core.streaming.rich.RichStreamingWrapper;

import java.util.function.BiFunction;

/**
 * @author Ice2Faith
 * @date 2023/5/1 23:38
 * @desc
 */
public abstract class RichBiFunctionWrapper<T, U, R> extends RichStreamingWrapper implements BiFunction<T, U, R> {
}
