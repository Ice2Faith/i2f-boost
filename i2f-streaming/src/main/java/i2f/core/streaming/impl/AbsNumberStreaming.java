package i2f.core.streaming.impl;

import i2f.core.streaming.iterator.LazyIterator;
import i2f.core.streaming.sink.SinkWrappers;
import i2f.core.streaming.sink.StreamingSinkWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/4/22 21:47
 * @desc
 */
public class AbsNumberStreaming<E extends Number> extends AbsStreaming<E> implements NumberStreaming<E> {
    public AbsNumberStreaming(Iterator<E> iterator) {
        super(iterator);
    }

    public static <E, R extends Number> AbsNumberStreaming<R> source(Iterator<E> iterator, Function<E, R> mapper) {
        LazyIterator<R> iter = new LazyIterator<>(() -> {
            return new Iterator<R>() {
                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public R next() {
                    return mapper.apply(iterator.next());
                }
            };
        });

        return new AbsNumberStreaming<>(iter);
    }

    @Override
    public E sum() {
        return sink(new StreamingSinkWrapper<E, E>() {
            @Override
            public E sink(Iterator<E> iterator) {
                E eval = null;
                BigDecimal ret = new BigDecimal("0");
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (val != null) {
                        eval = val;
                        ret = ret.add(new BigDecimal(String.valueOf(val)));
                    }
                }
                return SinkWrappers.castBigDecimal2Number(eval, ret);
            }
        });
    }

    @Override
    public E avg() {
        return sink(new StreamingSinkWrapper<E, E>() {
            @Override
            public E sink(Iterator<E> iterator) {
                E eval = null;
                BigDecimal ret = new BigDecimal("0");
                int cnt = 0;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (val != null) {
                        eval = val;
                        ret = ret.add(new BigDecimal(String.valueOf(val)));
                        cnt++;
                    }
                }
                ret = ret.divide(new BigDecimal(cnt), 10, RoundingMode.HALF_UP);
                return SinkWrappers.castBigDecimal2Number(eval, ret);
            }
        });
    }


}
