package i2f.core.streaming.keyed.rich.adapter;

import i2f.core.streaming.keyed.functional.KeyedPredicate;
import i2f.core.streaming.rich.RichStreamingWrapper;

/**
 * @author Ice2Faith
 * @date 2023/5/1 23:38
 * @desc
 */
public abstract class KeyedRichPredicateWrapper<K, T> extends RichStreamingWrapper implements KeyedPredicate<K, T> {
}
