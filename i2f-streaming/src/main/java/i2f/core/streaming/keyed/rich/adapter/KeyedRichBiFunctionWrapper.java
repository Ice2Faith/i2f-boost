package i2f.core.streaming.keyed.rich.adapter;

import i2f.core.streaming.keyed.functional.KeyedBiFunction;
import i2f.core.streaming.rich.RichStreamingWrapper;

/**
 * @author Ice2Faith
 * @date 2023/5/1 23:38
 * @desc
 */
public abstract class KeyedRichBiFunctionWrapper<K, T, U, R> extends RichStreamingWrapper implements KeyedBiFunction<K, T, U, R> {
}
