package i2f.core.streaming.keyed;

import i2f.core.streaming.Streaming;
import i2f.core.streaming.data.KeyedData;
import i2f.core.streaming.impl.StreamingContext;
import i2f.core.streaming.keyed.functional.*;
import i2f.core.streaming.keyed.process.KeyedStreamingProcessor;
import i2f.core.streaming.keyed.sink.KeyedStreamingSinker;
import i2f.core.streaming.process.StreamingProcessor;
import i2f.core.streaming.sink.StreamingSinker;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2023/4/22 21:41
 * @desc
 */
public interface KeyedStreaming<K, E> {
    KeyedStreaming<K, E> parallel();

    KeyedStreaming<K, E> sequential();

    KeyedStreaming<K, E> setContext(StreamingContext context);

    //////////////////////////////////////////////////////////////////////////////////////

    <OUT> KeyedStreaming<K, OUT> process(KeyedStreamingProcessor<K, E, OUT> processor, Object... args);

    <OUT> Streaming<KeyedData<K, OUT>> sink(KeyedStreamingSinker<K, E, OUT> sinker, Object... args);

    <OUT> Streaming<OUT> merge(KeyedStreamingSinker<K, E, OUT> sinker, Object... args);


    <OUT> KeyedStreaming<K, OUT> process(StreamingProcessor<E, OUT> processor, Object... args);

    <OUT> Streaming<KeyedData<K, OUT>> sink(StreamingSinker<E, OUT> sinker, Object... args);

    <OUT> Streaming<OUT> merge(StreamingSinker<E, OUT> sinker, Object... args);

    Streaming<E> flat();

    //////////////////////////////////////////////////////////////////////////////////////

    KeyedStreaming<K, E> filter(KeyedPredicate<K, E> filter, Object... args);

    KeyedStreaming<K, E> before(KeyedPredicate<K, E> filter);

    KeyedStreaming<K, E> after(KeyedPredicate<K, E> filter);

    KeyedStreaming<K, E> between(KeyedPredicate<K, E> begin, KeyedPredicate<K, E> end);

    <R> KeyedStreaming<K, E> distinct(KeyedFunction<K, E, R> keyer);

    <OUT> KeyedStreaming<K, OUT> map(KeyedFunction<K, E, OUT> mapper, Object... args);

    <OUT> KeyedStreaming<K, OUT> flatMap(KeyedBiConsumer<K, E, Consumer<OUT>> mapper, Object... args);

    KeyedStreaming<K, E> sort(KeyedComparator<K, E> comparator, boolean anti, Object... args);

    Streaming<KeyedData<K, E>> reduce(KeyedBiFunction<K, E, E, E> reducer);


    <OUT> Streaming<KeyedData<K, OUT>> aggregate(int computeSize,
                                                 KeyedFunction<K, List<E>, OUT> computer,
                                                 KeyedBiFunction<K, OUT, OUT, OUT> accumulator);

    <OUT> Streaming<KeyedData<K, OUT>> aggregate(KeyedFunction<K, List<E>, OUT> computer,
                                                 KeyedBiFunction<K, OUT, OUT, OUT> accumulator);

    <OUT, ACC> Streaming<KeyedData<K, OUT>> aggregate(int batchSize,
                                                      KeyedSupplier<K, ACC> creator,
                                                      KeyedBiFunction<K, E, ACC, ACC> accumulator,
                                                      KeyedBiFunction<K, ACC, ACC, ACC> merger,
                                                      KeyedFunction<K, ACC, OUT> outputer);

    <OUT, ACC> Streaming<KeyedData<K, OUT>> aggregate(KeyedSupplier<K, ACC> creator,
                                                      KeyedBiFunction<K, E, ACC, ACC> accumulator,
                                                      KeyedBiFunction<K, ACC, ACC, ACC> merger,
                                                      KeyedFunction<K, ACC, OUT> outputer);

    Streaming<KeyedData<K, E>> max(KeyedComparator<K, E> comparator);

    Streaming<KeyedData<K, E>> min(KeyedComparator<K, E> comparator);

    Streaming<KeyedData<K, E>> first(KeyedPredicate<K, E> filter);

    Streaming<KeyedData<K, E>> last(KeyedPredicate<K, E> filter);

    void each(KeyedConsumer<K, E> consumer);

    void each(KeyedPredicate<K, E> consumer);

    Streaming<KeyedData<K, Boolean>> anyMatch(KeyedPredicate<K, E> filter);

    Streaming<KeyedData<K, Boolean>> allMatch(KeyedPredicate<K, E> filter);

    <C extends Collection<E>> void batch(int batchSize, KeyedSupplier<K, C> supplier, KeyedConsumer<K, C> consumer);

    void batch(int batchSize, KeyedConsumer<K, Collection<E>> consumer);

    KeyedStreaming<K, E> sync(KeyedConsumer<K, Collection<E>> consumer);

    KeyedStreaming<K, E> peek(KeyedConsumer<K, E> consumer, Object... args);

    KeyedStreaming<K, E> resample(double rate, KeyedConsumer<K, E> consumer);

    //////////////////////////////////////////////////////////////////////////////////////

    KeyedStreaming<K, E> filter(Predicate<E> filter, Object... args);

    KeyedStreaming<K, E> before(Predicate<E> filter);

    KeyedStreaming<K, E> after(Predicate<E> filter);

    KeyedStreaming<K, E> head(int count);

    KeyedStreaming<K, E> tail(int count);

    KeyedStreaming<K, E> resample(double rate);

    KeyedStreaming<K, E> between(Predicate<E> begin, Predicate<E> end);

    KeyedStreaming<K, E> skip(int count);

    KeyedStreaming<K, E> limit(int count);

    <R> KeyedStreaming<K, E> distinct(Function<E, R> keyer);

    KeyedStreaming<K, E> distinct();

    //////////////////////////////////////////////////////////////////////////////////////

    <OUT> KeyedStreaming<K, OUT> map(Function<E, OUT> mapper, Object... args);

    <OUT> KeyedStreaming<K, OUT> flatMap(BiConsumer<E, Consumer<OUT>> mapper, Object... args);

    //////////////////////////////////////////////////////////////////////////////////////

    KeyedStreaming<K, E> sort(Comparator<E> comparator, boolean anti, Object... args);

    KeyedStreaming<K, E> sort(boolean anti);

    KeyedStreaming<K, E> sort();

    KeyedStreaming<K, E> shuffle();

    KeyedStreaming<K, E> reverse();

    //////////////////////////////////////////////////////////////////////////////////////

    Streaming<KeyedData<K, E>> reduce(BiFunction<E, E, E> reducer);


    <OUT> Streaming<KeyedData<K, OUT>> aggregate(int computeSize,
                                                 Function<List<E>, OUT> computer,
                                                 BiFunction<OUT, OUT, OUT> accumulator);

    <OUT> Streaming<KeyedData<K, OUT>> aggregate(Function<List<E>, OUT> computer,
                                                 BiFunction<OUT, OUT, OUT> accumulator);

    <OUT, ACC> Streaming<KeyedData<K, OUT>> aggregate(int batchSize,
                                                      Supplier<ACC> creator,
                                                      BiFunction<E, ACC, ACC> accumulator,
                                                      BiFunction<ACC, ACC, ACC> merger,
                                                      Function<ACC, OUT> outputer);

    <OUT, ACC> Streaming<KeyedData<K, OUT>> aggregate(Supplier<ACC> creator,
                                                      BiFunction<E, ACC, ACC> accumulator,
                                                      BiFunction<ACC, ACC, ACC> merger,
                                                      Function<ACC, OUT> outputer);


    Streaming<KeyedData<K, E>> max(Comparator<E> comparator);

    Streaming<KeyedData<K, E>> min(Comparator<E> comparator);

    Streaming<KeyedData<K, Integer>> count();

    //////////////////////////////////////////////////////////////////////////////////////

    Streaming<KeyedData<K, E>> first(Predicate<E> filter);

    Streaming<KeyedData<K, E>> first();

    Streaming<KeyedData<K, E>> last(Predicate<E> filter);

    Streaming<KeyedData<K, E>> last();

    void each(Consumer<E> consumer);

    void each(Predicate<E> consumer);

    Streaming<KeyedData<K, Boolean>> anyMatch(Predicate<E> filter);

    Streaming<KeyedData<K, Boolean>> allMatch(Predicate<E> filter);

    <C extends Collection<E>> void batch(int batchSize, Supplier<C> supplier, Consumer<C> consumer);

    void batch(int batchSize, Consumer<Collection<E>> consumer);

    //////////////////////////////////////////////////////////////////////////////////////

    KeyedStreaming<K, E> sync(Consumer<Collection<E>> consumer);

    KeyedStreaming<K, E> peek(Consumer<E> consumer, Object... args);

    KeyedStreaming<K, E> fork(Consumer<KeyedStreaming<K, E>> consumer);

    KeyedStreaming<K, E> join(KeyedStreaming<K, E> streaming);

    KeyedStreaming<K, E> merge(KeyedStreaming<K, E> streaming, Comparator<E> comparator);

    <OUT, CO> KeyedStreaming<K, OUT> connect(KeyedStreaming<K, CO> coStreaming, BiPredicate<E, CO> condition, BiFunction<E, CO, OUT> linker);

    <OUT, CO> KeyedStreaming<K, OUT> combine(KeyedStreaming<K, CO> coStreaming, BiFunction<E, CO, OUT> linker);

    KeyedStreaming<K, E> resample(double rate, Consumer<E> consumer);

    <R> KeyedStreaming<K, E> include(KeyedStreaming<K, E> streaming, Function<E, R> keyer);

    KeyedStreaming<K, E> include(KeyedStreaming<K, E> streaming);

    <R> KeyedStreaming<K, E> exclude(KeyedStreaming<K, E> streaming, Function<E, R> keyer);

    KeyedStreaming<K, E> exclude(KeyedStreaming<K, E> streaming);

    //////////////////////////////////////////////////////////////////////////////////////

    Streaming<KeyedData<K, Iterator<E>>> iterator();

    Streaming<KeyedData<K, Stream<E>>> stream();

    <C extends Collection<E>> Streaming<KeyedData<K, C>> collection(Supplier<C> supplier);

    Streaming<KeyedData<K, List<E>>> list();

    Streaming<KeyedData<K, Set<E>>> set();

    Streaming<KeyedData<K, E[]>> array(E[] arr);

    <R, A> Streaming<KeyedData<K, R>> collect(Collector<E, A, R> collector);

    Streaming<KeyedData<K, String>> stringify(Function<E, Object> mapper, Object open, Object separator, Object close);

    Streaming<KeyedData<K, Map<Streaming.Measure, Object>>> measures(Streaming.Measure... measures);
}
