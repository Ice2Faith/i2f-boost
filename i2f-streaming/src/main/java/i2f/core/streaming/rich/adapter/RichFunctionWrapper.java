package i2f.core.streaming.rich.adapter;

import i2f.core.streaming.rich.RichStreamingWrapper;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/5/1 23:38
 * @desc
 */
public abstract class RichFunctionWrapper<T, R> extends RichStreamingWrapper implements Function<T, R> {
}
