package i2f.core.streaming.rich.adapter;

import i2f.core.streaming.rich.RichStreamingWrapper;

import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2023/5/1 23:38
 * @desc
 */
public abstract class RichPredicateWrapper<T> extends RichStreamingWrapper implements Predicate<T> {
}
