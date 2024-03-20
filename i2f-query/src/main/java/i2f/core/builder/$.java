package i2f.core.builder;


import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/3/20 14:03
 * @desc
 */
public class $<E, C, R, T extends $<E, C, R, T>> {
    protected C acc;
    protected Class<E> clazz;
    protected Function<$,C> initializer;
    protected BiFunction<C, E, C> accumulator;
    protected BiFunction<$,C, R> finalizer;
    protected Supplier<E> separator;
    protected boolean ignoreNull = true;

    public $(Class<E> clazz,
             Function<$,C> initializer,
             BiFunction<C, E, C> accumulator,
             BiFunction<$,C, R> finalizer) {
        this.clazz = clazz;
        this.initializer = initializer;
        this.accumulator = accumulator;
        this.finalizer = finalizer;
        this.acc = initializer.apply((T)this);
    }

    public static <E, C, R, T extends $<E, C, R, T>> $<E, C, R, T> $_(Class<E> clazz,
                                                                      Function<$,C> initializer,
                                                                      BiFunction<C, E, C> accumulator,
                                                                      BiFunction<$,C, R> finalizer) {
        return new $(clazz, initializer, accumulator, finalizer);
    }

    public T $(E elem) {
        if (this.ignoreNull && elem == null) {
            return (T) this;

        }
        if (this.separator != null) {
            E sep = this.separator.get();
            if (!this.ignoreNull || sep != null) {
                this.acc = this.accumulator.apply(this.acc, sep);
            }
        }
        this.acc = this.accumulator.apply(this.acc, elem);
        return (T) this;
    }

    public R $$() {
        return this.finalizer.apply((T)this,this.acc);
    }

    public T $sep(Supplier<E> separator) {
        this.separator = separator;
        return (T) this;
    }

    public T $sepNone() {
        this.separator = null;
        return (T) this;
    }

    public T ignoreNull() {
        this.ignoreNull = true;
        return (T) this;
    }

    public T keepNull() {
        this.ignoreNull = false;
        return (T) this;
    }

    public <V> T $if(V val, Predicate<V> condition,
                     Function<V, E> elemMapper) {
        if (condition != null && !condition.test(val)) {
            return (T) this;
        }
        E elem = elemMapper.apply(val);
        return $(elem);
    }

    public <U, C extends Iterable<U>> T $for(C val, Predicate<C> condition,
                                             E prefix,
                                             E separator,
                                             E suffix,
                                             BiFunction<U, Integer, E> elemMapper
    ) {
        return $for(val, condition,
                e -> prefix,
                e -> separator,
                e -> suffix,
                elemMapper);
    }

    public <U, C extends Iterable<U>> T $for(C val, Predicate<C> condition,
                                             Function<C, E> prefix,
                                             Function<C, E> separator,
                                             Function<C, E> suffix,
                                             BiFunction<U, Integer, E> elemMapper
    ) {
        return $for(val, condition,
                null,
                e -> e,
                prefix, separator, suffix,
                elemMapper);
    }

    public <V, U, C extends Iterable<U>> T $for(V val, Predicate<V> condition,
                                                Class<U> clazz,
                                                Function<V, C> iterableMapper,
                                                E prefix,
                                                E separator,
                                                E suffix,
                                                BiFunction<U, Integer, E> elemMapper
    ) {
        return $for(val, condition,
                clazz, iterableMapper,
                e -> prefix,
                e -> separator,
                e -> suffix,
                elemMapper);
    }

    public <V, U, C extends Iterable<U>> T $for(V val, Predicate<V> condition,
                                                Class<U> clazz,
                                                Function<V, C> iterableMapper,
                                                Function<V, E> prefix,
                                                Function<V, E> separator,
                                                Function<V, E> suffix,
                                                BiFunction<U, Integer, E> elemMapper
    ) {
        if (condition != null && !condition.test(val)) {
            return (T) this;
        }
        C iterable = iterableMapper.apply(val);
        int i = 0;
        boolean isFirst = true;
        for (U item : iterable) {
            E elem = elemMapper.apply(item, i);
            if (this.ignoreNull && elem == null) {
                continue;
            }
            if (isFirst) {
                if (prefix != null) {
                    E pre = prefix.apply(val);
                    if (!this.ignoreNull || pre != null) {
                        this.acc = this.accumulator.apply(this.acc, pre);
                    }
                }
            }
            if (!isFirst) {
                if (separator != null) {
                    E sep = separator.apply(val);
                    if (!this.ignoreNull || sep != null) {
                        this.acc = this.accumulator.apply(this.acc, sep);
                    }
                }
            }
            this.acc = this.accumulator.apply(this.acc, elem);
            isFirst = false;
            i++;
        }
        if (!isFirst) {
            if (suffix != null) {
                E suf = suffix.apply(val);
                if (!this.ignoreNull || suf != null) {
                    this.acc = this.accumulator.apply(this.acc, suf);
                }
            }
        }
        return (T) this;
    }
}
