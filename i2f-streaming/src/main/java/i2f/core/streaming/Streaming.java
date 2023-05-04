package i2f.core.streaming;

import i2f.core.iterator.impl.EnumerationIterator;
import i2f.core.streaming.impl.AbsStreaming;
import i2f.core.streaming.impl.NumberStreaming;
import i2f.core.streaming.impl.StreamingContext;
import i2f.core.streaming.iterator.LazyIterator;
import i2f.core.streaming.keyed.KeyedStreaming;
import i2f.core.streaming.process.StreamingProcessor;
import i2f.core.streaming.sink.StreamingSinker;
import i2f.core.tuple.impl.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2023/4/22 21:41
 * @desc
 */
public interface Streaming<E> {
    public enum Measure {
        FIRST, // 第一个，返回E
        LAST, // 最后一个，返回E
        COUNT,  // 总数，返回Integer
        NULL_COUNT, // 空值的数量，返回Integer
        NON_NULL_COUNT, // 非空值的数量，返回Integer
        DISTINCT_COUNT, // 去重的数量，返回Integer
        MOST, // 最多出现的元素，返回E
        LEAST, // 最少出现的元素，返回E
        MIN, // 最小元素，返回E
        MAX, // 最大元素，返回E
        SUM, // 求和，返回E
        AVG, // 求品均值，返回E
        MEDIAN // 求中位数，返回E
        ;
    }

    static <E> Streaming<E> source(Iterator<E> iterator) {
        return AbsStreaming.source(iterator);
    }

    static <E> Streaming<E> source(Iterable<E> iterable) {
        return AbsStreaming.source(new LazyIterator<>(() -> iterable.iterator()));
    }

    static <E> Streaming<E> source(Enumeration<E> enumeration) {
        return AbsStreaming.source(new LazyIterator<>(() -> new EnumerationIterator<>(enumeration)));
    }

    static <E> Streaming<E> source(E... arr) {
        return AbsStreaming.source(new LazyIterator<>(() -> new ArrayList<>(Arrays.asList(arr)).iterator()));
    }

    static <E> Streaming<E> source(Stream<E> stream) {
        return AbsStreaming.source(new LazyIterator<>(() -> stream.iterator()));
    }

    Streaming<E> parallel();

    Streaming<E> sequential();

    Streaming<E> setContext(StreamingContext context);

    //////////////////////////////////////////////////////////////////////////////////////

    <OUT> Streaming<OUT> process(StreamingProcessor<E, OUT> processor, Object... args);

    <OUT> OUT sink(StreamingSinker<E, OUT> sinker, Object... args);

    //////////////////////////////////////////////////////////////////////////////////////

    Streaming<E> filter(Predicate<E> filter, Object... args);

    Streaming<E> before(Predicate<E> filter);

    Streaming<E> after(Predicate<E> filter);

    Streaming<E> head(int count);

    Streaming<E> tail(int count);

    Streaming<E> resample(double rate);

    Streaming<E> between(Predicate<E> begin, Predicate<E> end);

    Streaming<E> skip(int count);

    Streaming<E> limit(int count);

    <R> Streaming<E> distinct(Function<E, R> keyer);

    Streaming<E> distinct();

    //////////////////////////////////////////////////////////////////////////////////////

    <OUT> Streaming<OUT> map(Function<E, OUT> mapper, Object... args);

    <OUT> Streaming<OUT> flatMap(BiConsumer<E, Consumer<OUT>> mapper, Object... args);

    //////////////////////////////////////////////////////////////////////////////////////

    Streaming<E> sort(Comparator<E> comparator, boolean anti, Object... args);

    Streaming<E> sort(boolean anti);

    Streaming<E> sort();

    Streaming<E> shuffle();

    Streaming<E> reverse();

    //////////////////////////////////////////////////////////////////////////////////////

    E reduce(BiFunction<E, E, E> reducer);

    <OUT> OUT aggregate(int computeSize,
                        Function<List<E>, OUT> computer,
                        BiFunction<OUT, OUT, OUT> accumulator);

    <OUT> OUT aggregate(Function<List<E>, OUT> computer,
                        BiFunction<OUT, OUT, OUT> accumulator);

    <OUT, ACC> OUT aggregate(int batchSize,
                             Supplier<ACC> creator,
                             BiFunction<E, ACC, ACC> accumulator,
                             BiFunction<ACC, ACC, ACC> merger,
                             Function<ACC, OUT> outputer);

    <OUT, ACC> OUT aggregate(Supplier<ACC> creator,
                             BiFunction<E, ACC, ACC> accumulator,
                             BiFunction<ACC, ACC, ACC> merger,
                             Function<ACC, OUT> outputer);

    E max(Comparator<E> comparator);

    E min(Comparator<E> comparator);

    int count();

    //////////////////////////////////////////////////////////////////////////////////////

    <K> KeyedStreaming<K, E> keyBy(Function<E, K> keyer);

    //////////////////////////////////////////////////////////////////////////////////////

    E first(Predicate<E> filter);

    E first();

    E last(Predicate<E> filter);

    E last();

    void each(Consumer<E> consumer);

    void each(Predicate<E> consumer);

    boolean anyMatch(Predicate<E> filter);

    boolean allMatch(Predicate<E> filter);

    <C extends Collection<E>> void batch(int batchSize, Supplier<C> supplier, Consumer<C> consumer);

    void batch(int batchSize, Consumer<Collection<E>> consumer);

    //////////////////////////////////////////////////////////////////////////////////////

    Streaming<E> sync(Consumer<Collection<E>> consumer);

    Streaming<E> peek(Consumer<E> consumer, Object... args);

    Streaming<E> fork(Consumer<Streaming<E>>... consumers);

    Streaming<E> join(Streaming<E> streaming);

    Streaming<E> merge(Streaming<E> streaming, Comparator<E> comparator);

    <OUT, CO> Streaming<OUT> connect(Streaming<CO> coStreaming, BiPredicate<E, CO> condition, BiFunction<E, CO, OUT> linker);

    <OUT, CO> Streaming<OUT> combine(Streaming<CO> coStreaming, BiFunction<E, CO, OUT> linker);

    Streaming<E> resample(double rate, Consumer<E> consumer);

    <R> Streaming<E> include(Streaming<E> streaming, Function<E, R> keyer);

    Streaming<E> include(Streaming<E> streaming);

    <R> Streaming<E> exclude(Streaming<E> streaming, Function<E, R> keyer);

    Streaming<E> exclude(Streaming<E> streaming);

    //////////////////////////////////////////////////////////////////////////////////////

    Iterator<E> iterator();

    Stream<E> stream();

    <C extends Collection<E>> C collection(C col);

    List<E> list();

    Set<E> set();

    E[] array(E[] arr);

    <R, A> R collect(Collector<E, A, R> collector);

    String stringify(Function<E, Object> mapper, Object open, Object separator, Object close);

    <R extends Number> NumberStreaming<R> number(Function<E, R> mapper);

    Map<Measure, Object> measures(Measure... measures);

    //////////////////////////////////////////////////////////////////////////////////////

    Streaming<Map<Integer, Optional<Object>>> mapper(Function<E, ?>... mappers);

    <V1> Streaming<Tuple1<V1>> select(Function<E, V1> mapper1);

    <V1, V2> Streaming<Tuple2<V1, V2>> select(Function<E, V1> mapper1,
                                              Function<E, V2> mapper2
    );

    <V1, V2, V3> Streaming<Tuple3<V1, V2, V3>> select(Function<E, V1> mapper1,
                                                      Function<E, V2> mapper2,
                                                      Function<E, V3> mapper3
    );

    <V1, V2, V3, V4> Streaming<Tuple4<V1, V2, V3, V4>> select(Function<E, V1> mapper1,
                                                              Function<E, V2> mapper2,
                                                              Function<E, V3> mapper3,
                                                              Function<E, V4> mapper4
    );

    <V1, V2, V3, V4, V5> Streaming<Tuple5<V1, V2, V3, V4, V5>> select(Function<E, V1> mapper1,
                                                                      Function<E, V2> mapper2,
                                                                      Function<E, V3> mapper3,
                                                                      Function<E, V4> mapper4,
                                                                      Function<E, V5> mapper5
    );

    <V1, V2, V3, V4, V5, V6> Streaming<Tuple6<V1, V2, V3, V4, V5, V6>> select(Function<E, V1> mapper1,
                                                                              Function<E, V2> mapper2,
                                                                              Function<E, V3> mapper3,
                                                                              Function<E, V4> mapper4,
                                                                              Function<E, V5> mapper5,
                                                                              Function<E, V6> mapper6
    );

    <V1, V2, V3, V4, V5, V6, V7> Streaming<Tuple7<V1, V2, V3, V4, V5, V6, V7>> select(Function<E, V1> mapper1,
                                                                                      Function<E, V2> mapper2,
                                                                                      Function<E, V3> mapper3,
                                                                                      Function<E, V4> mapper4,
                                                                                      Function<E, V5> mapper5,
                                                                                      Function<E, V6> mapper6,
                                                                                      Function<E, V7> mapper7
    );

    <V1, V2, V3, V4, V5, V6, V7, V8> Streaming<Tuple8<V1, V2, V3, V4, V5, V6, V7, V8>> select(Function<E, V1> mapper1,
                                                                                              Function<E, V2> mapper2,
                                                                                              Function<E, V3> mapper3,
                                                                                              Function<E, V4> mapper4,
                                                                                              Function<E, V5> mapper5,
                                                                                              Function<E, V6> mapper6,
                                                                                              Function<E, V7> mapper7,
                                                                                              Function<E, V8> mapper8
    );

    <V1, V2, V3, V4, V5, V6, V7, V8, V9> Streaming<Tuple9<V1, V2, V3, V4, V5, V6, V7, V8, V9>> select(Function<E, V1> mapper1,
                                                                                                      Function<E, V2> mapper2,
                                                                                                      Function<E, V3> mapper3,
                                                                                                      Function<E, V4> mapper4,
                                                                                                      Function<E, V5> mapper5,
                                                                                                      Function<E, V6> mapper6,
                                                                                                      Function<E, V7> mapper7,
                                                                                                      Function<E, V8> mapper8,
                                                                                                      Function<E, V9> mapper9
    );

    <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> Streaming<Tuple10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10>> select(Function<E, V1> mapper1,
                                                                                                                 Function<E, V2> mapper2,
                                                                                                                 Function<E, V3> mapper3,
                                                                                                                 Function<E, V4> mapper4,
                                                                                                                 Function<E, V5> mapper5,
                                                                                                                 Function<E, V6> mapper6,
                                                                                                                 Function<E, V7> mapper7,
                                                                                                                 Function<E, V8> mapper8,
                                                                                                                 Function<E, V9> mapper9,
                                                                                                                 Function<E, V10> mapper10
    );
}
