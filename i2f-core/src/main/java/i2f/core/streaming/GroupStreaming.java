package i2f.core.streaming;

import i2f.core.functional.common.IExecutor;
import i2f.core.functional.common.IFilter;
import i2f.core.functional.common.IMapper;
import i2f.core.functional.jvf.BiConsumer;
import i2f.core.functional.jvf.BiFunction;
import i2f.core.functional.jvf.Consumer;
import i2f.core.streaming.api.process.IProcessStreaming;
import i2f.core.streaming.api.sink.ISinkStreaming;
import i2f.core.tuple.impl.Tuple2;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Collector;

public interface GroupStreaming<K, E> {

    GroupStreaming<K, E> groupParallel();

    GroupStreaming<K, E> groupSequential();

    boolean isGroupParallel();

    GroupStreaming<K, E> groupParallelize(int cnt);

    Streaming<Tuple2<K, Collection<E>>> stream();

    Streaming<E> flat();

    <R> GroupStreaming<K, R> process(IProcessStreaming<R, E> processor);

    <R> Streaming<Tuple2<K, R>> sink(ISinkStreaming<R, E> processor);

    GroupStreaming<K, E> parallel();

    GroupStreaming<K, E> sequential();

    GroupStreaming<K, E> parallelize(int cnt);

    Streaming<Tuple2<K, Boolean>> isParallel();

    GroupStreaming<K, E> skip(int len);

    GroupStreaming<K, E> limit(int len);

    GroupStreaming<K, E> limit(int offset, int len);

    GroupStreaming<K, E> filter(IFilter<E> filter);

    GroupStreaming<K, E> andFilter(IFilter<E>... filters);

    GroupStreaming<K, E> orFilter(IFilter<E>... filters);

    GroupStreaming<K, E> afterAll(IFilter<E> filter);

    GroupStreaming<K, E> beforeAll(IFilter<E> filter);

    GroupStreaming<K, E> rangeAll(IFilter<E> beginFilter, IFilter<E> endFilter);

    GroupStreaming<K, E> peek(Consumer<E> consumer);

    GroupStreaming<K, E> log(IExecutor<Object[]> executor, Object... args);

    GroupStreaming<K, E> distinct();

    GroupStreaming<K, E> sort(Comparator<E> comparator);

    GroupStreaming<K, E> shuffle();

    <R> GroupStreaming<K, R> map(IMapper<R, E> mapper);

    <R> GroupStreaming<K, R> flatMap(BiConsumer<E, Collection<R>> mapper);

    <R> GroupStreaming<K, R> aggregate(BiConsumer<Iterator<E>, Collection<R>> mapper);

    Streaming<Tuple2<K, E>> reduce(BiFunction<E, E, E> reducer);

    Streaming<Tuple2<K, E[]>> array(Class<E[]> tarType);

    void each(IExecutor<E> executor);

    Streaming<Tuple2<K, E>> first();

    Streaming<Tuple2<K, E>> last();

    Streaming<Tuple2<K, E>> firstMatch(IFilter<E> filter);

    Streaming<Tuple2<K, E>> lastMatch(IFilter<E> filter);

    Streaming<Tuple2<K, Integer>> count();

    Streaming<Tuple2<K, E>> max(Comparator<E> comparator);

    Streaming<Tuple2<K, E>> min(Comparator<E> comparator);

    Streaming<Tuple2<K, Boolean>> anyMatch(IFilter<E> filter);

    Streaming<Tuple2<K, Boolean>> allMatch(IFilter<E> filter);

    Streaming<Tuple2<K, String>> stringify(String open, String separator, String close);

    Streaming<Tuple2<K, String>> stringify(String separator);

    <R, A> Streaming<Tuple2<K, R>> collect(Collector<E, A, R> collector);

}
