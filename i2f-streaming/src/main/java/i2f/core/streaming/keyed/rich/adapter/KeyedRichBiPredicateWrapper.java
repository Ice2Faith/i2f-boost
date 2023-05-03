package i2f.core.streaming.keyed.rich.adapter;

import i2f.core.streaming.keyed.functional.KeyedBiPredicate;
import i2f.core.streaming.rich.RichStreamingWrapper;

/**
 * @author Ice2Faith
 * @date 2023/5/1 23:38
 * @desc
 */
public abstract class KeyedRichBiPredicateWrapper<K, T, U> extends RichStreamingWrapper implements KeyedBiPredicate<K, T, U> {
}
