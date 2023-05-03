package i2f.core.streaming.impl;

import i2f.core.streaming.Streaming;
import i2f.core.streaming.comparator.DefaultComparator;
import i2f.core.streaming.iterator.LazyIterator;
import i2f.core.streaming.keyed.KeyedStreaming;
import i2f.core.streaming.keyed.impl.AbsKeyedStreaming;
import i2f.core.streaming.keyed.process.KeyedStreamingProcessor;
import i2f.core.streaming.keyed.sink.KeyedStreamingSinker;
import i2f.core.streaming.process.ProcessWrappers;
import i2f.core.streaming.process.StreamingProcessor;
import i2f.core.streaming.rich.RichStreamingWrapper;
import i2f.core.streaming.sink.SinkWrappers;
import i2f.core.streaming.sink.StreamingSinker;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2023/4/22 21:47
 * @desc
 */
public class AbsStreaming<E> implements Streaming<E> {
    protected Iterator<E> iterator;
    protected StreamingContext context;

    public AbsStreaming(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    public StreamingContext getContext() {
        return context;
    }

    @Override
    public AbsStreaming<E> setContext(StreamingContext context) {
        this.context = context;
        return this;
    }

    protected void inject(Object processor, Object... args) {
        if (processor instanceof RichStreamingWrapper) {
            RichStreamingWrapper rich = (RichStreamingWrapper) processor;
            rich.setContext(context);
            rich.setArgs(args);
        }
    }

    public static <E> AbsStreaming<E> source(Iterator<E> iterator) {
        StreamingContext context = new StreamingContext();
        context.setPool(StreamingContext.defaultPool);
        AbsStreaming<E> ret = new AbsStreaming<>(iterator)
                .setContext(context);
        return ret;
    }

    @Override
    public Streaming<E> parallel() {
        context.setParallel(true);
        return this;
    }

    @Override
    public Streaming<E> sequential() {
        context.setParallel(false);
        return this;
    }

    protected StreamingContext copyContext() {
        StreamingContext ret = new StreamingContext();
        ret.setPool(context.getPool());
        ret.setGlobalMap(context.getGlobalMap());
        ret.setParallel(context.isParallel());
        return ret;
    }

    public <K, OUT> Streaming<OUT> process(K key, KeyedStreamingProcessor<K, E, OUT> processor, Object... args) {
        inject(processor, args);
        return new AbsStreaming<>(new LazyIterator<>(() -> processor.process(key, iterator)))
                .setContext(copyContext());
    }

    public <K, OUT> OUT sink(K key, KeyedStreamingSinker<K, E, OUT> sinker, Object... args) {
        inject(sinker, args);
        OUT ret = sinker.sink(key, iterator);
        return ret;
    }

    @Override
    public <OUT> Streaming<OUT> process(StreamingProcessor<E, OUT> processor, Object... args) {
        inject(processor, args);
        return new AbsStreaming<>(new LazyIterator<>(() -> processor.process(iterator)))
                .setContext(copyContext());
    }

    @Override
    public <OUT> OUT sink(StreamingSinker<E, OUT> sinker, Object... args) {
        inject(sinker, args);
        OUT ret = sinker.sink(iterator);
        return ret;
    }

    @Override
    public Streaming<E> filter(Predicate<E> filter, Object... args) {
        inject(filter, args);
        return process(ProcessWrappers.filter(filter), args);
    }

    @Override
    public <OUT> Streaming<OUT> map(Function<E, OUT> mapper, Object... args) {
        inject(mapper, args);
        return process(ProcessWrappers.mapper(mapper), args);
    }

    @Override
    public <OUT> Streaming<OUT> flatMap(BiConsumer<E, Consumer<OUT>> mapper, Object... args) {
        inject(mapper, args);
        return process(ProcessWrappers.flatMap(mapper), args);
    }

    @Override
    public <K> KeyedStreaming<K, E> keyBy(Function<E, K> keyer) {
        inject(keyer);
        return AbsKeyedStreaming.source(keyer, iterator).setContext(copyContext());
    }

    @Override
    public Streaming<E> before(Predicate<E> filter) {
        inject(filter);
        return process(ProcessWrappers.before(filter));
    }

    @Override
    public Streaming<E> after(Predicate<E> filter) {
        inject(filter);
        return process(ProcessWrappers.after(filter));
    }

    @Override
    public Streaming<E> head(int count) {
        return process(ProcessWrappers.head(count));
    }

    @Override
    public Streaming<E> tail(int count) {
        return process(ProcessWrappers.tail(count));
    }

    @Override
    public Streaming<E> resample(double rate) {
        return process(ProcessWrappers.resample(rate));
    }

    @Override
    public Streaming<E> between(Predicate<E> begin, Predicate<E> end) {
        inject(begin);
        inject(end);
        return process(ProcessWrappers.between(begin, end));
    }

    @Override
    public Streaming<E> skip(int count) {
        return process(ProcessWrappers.skip(count));
    }

    @Override
    public Streaming<E> limit(int count) {
        return process(ProcessWrappers.limit(count));
    }

    @Override
    public <R> Streaming<E> distinct(Function<E, R> keyer) {
        inject(keyer);
        return process(ProcessWrappers.distinct(keyer));
    }

    @Override
    public Streaming<E> distinct() {
        return distinct((e) -> e);
    }

    @Override
    public Streaming<E> sort(Comparator<E> comparator, boolean anti, Object... args) {
        inject(comparator, args);
        return process(ProcessWrappers.sort(comparator, anti), args);
    }

    @Override
    public Streaming<E> sort(boolean anti) {
        return sort(new DefaultComparator<E>(), anti);
    }

    @Override
    public Streaming<E> sort() {
        return sort(false);
    }

    @Override
    public Streaming<E> shuffle() {
        return process(ProcessWrappers.shuffle());
    }

    @Override
    public Streaming<E> reverse() {
        return process(ProcessWrappers.reverse());
    }

    @Override
    public E reduce(BiFunction<E, E, E> reducer) {
        inject(reducer);
        return sink(SinkWrappers.reduce(reducer));
    }

    @Override
    public <OUT> OUT aggregate(int computeSize, Function<List<E>, OUT> computer, BiFunction<OUT, OUT, OUT> accumulator) {
        inject(computer);
        inject(accumulator);
        return sink(SinkWrappers.aggregate(computeSize, computer, accumulator));
    }

    @Override
    public <OUT> OUT aggregate(Function<List<E>, OUT> computer, BiFunction<OUT, OUT, OUT> accumulator) {
        return aggregate(256, computer, accumulator);
    }

    @Override
    public <OUT, ACC> OUT aggregate(int batchSize, Supplier<ACC> creator, BiFunction<E, ACC, ACC> accumulator, BiFunction<ACC, ACC, ACC> merger, Function<ACC, OUT> outputer) {
        inject(creator);
        inject(accumulator);
        inject(merger);
        inject(outputer);
        return sink(SinkWrappers.aggregate(batchSize, creator, accumulator, merger, outputer));
    }

    @Override
    public <OUT, ACC> OUT aggregate(Supplier<ACC> creator, BiFunction<E, ACC, ACC> accumulator, BiFunction<ACC, ACC, ACC> merger, Function<ACC, OUT> outputer) {
        return aggregate(1024, creator, accumulator, merger, outputer);
    }

    @Override
    public E max(Comparator<E> comparator) {
        inject(comparator);
        return sink(SinkWrappers.max(comparator));
    }

    @Override
    public E min(Comparator<E> comparator) {
        inject(comparator);
        return sink(SinkWrappers.min(comparator));
    }

    @Override
    public int count() {
        return sink(SinkWrappers.count());
    }

    @Override
    public E first(Predicate<E> filter) {
        inject(filter);
        return sink(SinkWrappers.first(filter));
    }

    @Override
    public E first() {
        return first((e) -> true);
    }

    @Override
    public E last(Predicate<E> filter) {
        inject(filter);
        return sink(SinkWrappers.last(filter));
    }

    @Override
    public E last() {
        return last((e) -> true);
    }

    @Override
    public void each(Consumer<E> consumer) {
        inject(consumer);
        sink(SinkWrappers.each(consumer));
    }

    @Override
    public void each(Predicate<E> consumer) {
        inject(consumer);
        sink(SinkWrappers.each(consumer));
    }

    @Override
    public boolean anyMatch(Predicate<E> filter) {
        inject(filter);
        return sink(SinkWrappers.anyMatch(filter));
    }

    @Override
    public boolean allMatch(Predicate<E> filter) {
        inject(filter);
        return sink(SinkWrappers.allMatch(filter));
    }

    @Override
    public <C extends Collection<E>> void batch(int batchSize, Supplier<C> supplier, Consumer<C> consumer) {
        inject(supplier);
        inject(consumer);
        sink(SinkWrappers.batch(batchSize, supplier, consumer));
    }

    @Override
    public void batch(int batchSize, Consumer<Collection<E>> consumer) {
        inject(consumer);
        batch(batchSize, LinkedList::new, consumer);
    }

    @Override
    public Streaming<E> sync(Consumer<Collection<E>> consumer) {
        inject(consumer);
        return process(ProcessWrappers.sync(consumer));
    }

    @Override
    public Streaming<E> peek(Consumer<E> consumer, Object... args) {
        inject(consumer, args);
        return process(ProcessWrappers.peek(consumer), args);
    }

    @Override
    public Streaming<E> fork(Consumer<Streaming<E>> consumer) {
        inject(consumer);
        return process(ProcessWrappers.fork(consumer));
    }

    @Override
    public Streaming<E> join(Streaming<E> streaming) {
        inject(streaming);
        return process(ProcessWrappers.join(streaming));
    }

    @Override
    public Streaming<E> merge(Streaming<E> streaming, Comparator<E> comparator) {
        inject(streaming);
        inject(comparator);
        return process(ProcessWrappers.merge(streaming, comparator));
    }

    @Override
    public <OUT, CO> Streaming<OUT> connect(Streaming<CO> coStreaming, BiPredicate<E, CO> condition, BiFunction<E, CO, OUT> linker) {
        inject(coStreaming);
        inject(condition);
        inject(linker);
        return process(ProcessWrappers.connect(coStreaming, condition, linker));
    }

    @Override
    public <OUT, CO> Streaming<OUT> combine(Streaming<CO> coStreaming, BiFunction<E, CO, OUT> linker) {
        inject(coStreaming);
        inject(linker);
        return process(ProcessWrappers.combine(coStreaming, linker));
    }

    @Override
    public Streaming<E> resample(double rate, Consumer<E> consumer) {
        inject(consumer);
        return process(ProcessWrappers.resample(rate, consumer));
    }

    @Override
    public <R> Streaming<E> include(Streaming<E> streaming, Function<E, R> keyer) {
        inject(streaming);
        inject(keyer);
        return process(ProcessWrappers.include(streaming, keyer));
    }

    @Override
    public Streaming<E> include(Streaming<E> streaming) {
        inject(streaming);
        return include(streaming, e -> e);
    }

    @Override
    public <R> Streaming<E> exclude(Streaming<E> streaming, Function<E, R> keyer) {
        inject(streaming);
        inject(keyer);
        return process(ProcessWrappers.exclude(streaming, keyer));
    }

    @Override
    public Streaming<E> exclude(Streaming<E> streaming) {
        inject(streaming);
        return exclude(streaming, e -> e);
    }

    @Override
    public Iterator<E> iterator() {
        return sink(SinkWrappers.iterator());
    }

    @Override
    public Stream<E> stream() {
        return sink(SinkWrappers.stream());
    }

    @Override
    public <C extends Collection<E>> C collection(C col) {
        return sink(SinkWrappers.collection(col));
    }

    @Override
    public List<E> list() {
        return collection(new LinkedList<>());
    }

    @Override
    public Set<E> set() {
        return collection(new LinkedHashSet<>());
    }

    @Override
    public E[] array(E[] arr) {
        return sink(SinkWrappers.array(arr));
    }

    @Override
    public <R, A> R collect(Collector<E, A, R> collector) {
        return sink(SinkWrappers.collect(collector));
    }

    @Override
    public String stringify(Function<E, Object> mapper, Object open, Object separator, Object close) {
        return sink(SinkWrappers.stringify(mapper, open, separator, close));
    }

    @Override
    public Map<Measure, Object> measures(Measure... measures) {
        return sink(SinkWrappers.measures(measures));
    }

    @Override
    public <R extends Number> NumberStreaming<R> number(Function<E, R> mapper) {
        return (AbsNumberStreaming<R>) AbsNumberStreaming.source(iterator, mapper).setContext(copyContext());
    }
}
