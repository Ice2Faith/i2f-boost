package i2f.core.streaming.keyed.rich.adapter;

import i2f.core.streaming.keyed.functional.KeyedFunction;
import i2f.core.streaming.rich.RichStreamingWrapper;

/**
 * @author Ice2Faith
 * @date 2023/5/1 23:38
 * @desc
 */
public abstract class KeyedRichFunctionWrapper<K, T, R> extends RichStreamingWrapper implements KeyedFunction<K, T, R> {
}
