package i2f.core.streaming;

import i2f.core.delegate.batch.IBatchResolver;
import i2f.core.functional.common.IExecutor;
import i2f.core.functional.common.IFilter;
import i2f.core.functional.common.IMapper;
import i2f.core.functional.jvf.*;
import i2f.core.iterator.impl.ArrayIterator;
import i2f.core.iterator.impl.ArrayObjectIterator;
import i2f.core.iterator.impl.EnumerationIterator;
import i2f.core.streaming.api.process.IProcessStreaming;
import i2f.core.streaming.api.sink.ISinkStreaming;
import i2f.core.streaming.api.source.ISourceStreaming;
import i2f.core.streaming.api.source.SourceStreaming;
import i2f.core.streaming.base.source.SimpleSourceStreaming;
import i2f.core.tuple.impl.Tuple2;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author ltb
 * @date 2022/11/22 10:52
 * @desc
 */
public interface Streaming<E> {
    static <T> Streaming<T> stream(Iterator<T> iterator) {
        return new SimpleSourceStreaming<T>(iterator);
    }

    static <T> Streaming<T> stream(Iterable<T> iterable) {
        return new SimpleSourceStreaming<T>(iterable.iterator());
    }

    static <T> Streaming<T> stream(T... arr) {
        return new SimpleSourceStreaming<T>(new ArrayIterator<>(arr));
    }

    static <T> Streaming<T> stream(Object arr) {
        return new SimpleSourceStreaming<T>(new ArrayObjectIterator<>(arr));
    }

    static <T> Streaming<T> stream(Enumeration<T> enumeration) {
        return new SimpleSourceStreaming<T>(new EnumerationIterator<>(enumeration));
    }

    static <T> Streaming<T> stream(Stream<T> stream) {
        return new SimpleSourceStreaming<T>(stream.iterator());
    }

    static <T> Streaming<T> stream(ISourceStreaming<T> stream) {
        return new SourceStreaming<T>(stream);
    }

    <R> Streaming<R> process(IProcessStreaming<R, E> processor);

    <R> R sink(ISinkStreaming<R, E> processor);

    Streaming<E> parallel();

    Streaming<E> sequential();

    Streaming<E> parallelize(int cnt);

    boolean isParallel();

    Streaming<E> skip(int len);

    Streaming<E> limit(int len);

    Streaming<E> limit(int offset, int len);

    Streaming<E> filter(IFilter<E> filter);

    Streaming<E> andFilter(IFilter<E>... filters);

    Streaming<E> orFilter(IFilter<E>... filters);

    Streaming<E> afterAll(IFilter<E> filter);

    Streaming<E> beforeAll(IFilter<E> filter);

    Streaming<E> rangeAll(IFilter<E> beginFilter, IFilter<E> endFilter);

    Streaming<E> peek(Consumer<E> consumer);

    Streaming<E> log(IExecutor<Object[]> executor, Object... args);

    Streaming<E> union(Streaming<E> stream);

    <R, T> Streaming<R> join(Streaming<T> stream,
                             BiPredicate<E, T> joiner,
                             BiFunction<R, E, T> processor);

    Streaming<E> include(Streaming<E> stream);

    Streaming<E> exclude(Streaming<E> stream);

    Streaming<E> distinct();

    Streaming<E> sort(Comparator<E> comparator);

    Streaming<E> shuffle();

    <R> Streaming<R> map(IMapper<R, E> mapper);

    <R> Streaming<R> flatMap(BiConsumer<E, Collection<R>> mapper);

    <R> Streaming<R> aggregate(BiConsumer<Iterator<E>, Collection<R>> mapper);

    <K> Streaming<Tuple2<K, E>> keyedReduce(BiSupplier<K, E> key, BiFunction<E, E, E> reducer);

    <K, RT> Streaming<Tuple2<K, RT>> keyedAggregate(BiSupplier<K, E> key, BiSupplier<RT, Iterator<E>> mapper);

    <K> Streaming<Tuple2<K, Collection<E>>> keyBy(BiSupplier<K, E> key);

    Streaming<Tuple2<E, Integer>> countBy();

    E reduce(BiFunction<E, E, E> reducer);

    <R, C extends Collection<R>> C collect(C col);

    E[] array(Class<E[]> tarType);

    <K, V, MAP extends Map<K, V>> MAP collect(MAP map, BiSupplier<K, E> key, BiSupplier<V, E> val);

    void each(IExecutor<E> executor);

    <R> List<Tuple2<R, Exception>> batch(int batchCount, IBatchResolver<R, E> resolver, boolean throwEx);

    <R> List<Tuple2<R, Exception>> batch(int batchCount, IBatchResolver<R, E> resolver);

    E first();

    E last();

    E firstMatch(IFilter<E> filter);

    E lastMatch(IFilter<E> filter);

    Iterator<E> iterator();

    int count();

    E max(Comparator<E> comparator);

    E min(Comparator<E> comparator);

    boolean anyMatch(IFilter<E> filter);

    boolean allMatch(IFilter<E> filter);

    String stringify(String open, String separator, String close);

    default String stringify(String separator) {
        return stringify(null, separator, null);
    }

    <T extends Appendable> T stringify(T appender, String open, String separator, String close);

    default <T extends Appendable> T stringify(T appender, String separator) {
        return stringify(appender, null, separator, null);
    }

    Stream<E> stream();

}
