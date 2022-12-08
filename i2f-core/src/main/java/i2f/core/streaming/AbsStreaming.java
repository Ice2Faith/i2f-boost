package i2f.core.streaming;

import i2f.core.functional.common.IExecutor;
import i2f.core.functional.common.IFilter;
import i2f.core.functional.common.IMapper;
import i2f.core.functional.consumer.IConsumer2;
import i2f.core.functional.jvf.*;
import i2f.core.streaming.api.process.IProcessStreaming;
import i2f.core.streaming.api.process.ProcessStreaming;
import i2f.core.streaming.api.sink.ISinkStreaming;
import i2f.core.streaming.api.sink.SinkStreaming;
import i2f.core.streaming.base.KeyedAggregateStreaming;
import i2f.core.streaming.base.KeyedReduceStreaming;
import i2f.core.streaming.base.process.*;
import i2f.core.streaming.base.sink.*;
import i2f.core.thread.AtomicCountDownLatch;
import i2f.core.tuple.impl.Tuple2;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

/**
 * @author ltb
 * @date 2022/11/22 9:45
 * @desc
 */
public abstract class AbsStreaming<R, E> implements Streaming<E>, IProcessStreaming<R, E> {
    public AbsStreaming prev;
    public AbsStreaming next;
    public Boolean parallel;
    public Integer parallelize;

    @Override
    public boolean isParallel() {
        return parallel != null && parallel;
    }

    @Override
    public Streaming<E> parallel() {
        this.parallel = true;
        return this;
    }

    @Override
    public Streaming<E> sequential() {
        this.parallel = false;
        return this;
    }

    @Override
    public Streaming<E> parallelize(int cnt) {
        this.parallelize = cnt;
        return this;
    }

    public <OUT, IN> Iterator<OUT> parallelizeProcess(Iterator<IN> iterator, ExecutorService pool, IConsumer2<IN, Collection<OUT>> handler) {
        List<OUT> ret = Collections.synchronizedList(new LinkedList<>());
        AtomicCountDownLatch latch = new AtomicCountDownLatch();
        while (iterator.hasNext()) {
            IN item = iterator.next();
            if (pool != null) {
                latch.count();
                pool.submit(() -> {
                    handler.accept(item, ret);
                    latch.down();
                });
            } else {
                handler.accept(item, ret);
            }
        }
        if (pool != null) {
            System.out.println("parallel:" + this.getClass().getSimpleName());
            try {
                long tms = latch.await();
                System.out.println("parallel " + tms + "ms done:" + this.getClass().getSimpleName());
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

        }
        return ret.iterator();
    }

    @Override
    public <R> Streaming<R> process(IProcessStreaming<R, E> processor) {
        AbsStreaming<E, R> ret = (AbsStreaming<E, R>) new ProcessStreaming<E, R>(processor);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public <R> R sink(ISinkStreaming<R, E> sink) {
        AbsSinkStreaming<R, E, E> ret = new SinkStreaming<R, E>(sink);
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public Streaming<E> skip(int len) {
        AbsStreaming<E, E> ret = new LimitStreaming<E>(len, -1);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> limit(int len) {
        AbsStreaming<E, E> ret = new LimitStreaming<E>(-1, len);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> limit(int offset, int len) {
        AbsStreaming<E, E> ret = new LimitStreaming<E>(offset, len);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> filter(IFilter<E> filter) {
        AbsStreaming<E, E> ret = new FilterStreaming<E>(filter);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> andFilter(IFilter<E>... filters) {
        AbsStreaming<E, E> ret = new AndFilterStreaming<E>(filters);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> orFilter(IFilter<E>... filters) {
        AbsStreaming<E, E> ret = new OrFilterStreaming<E>(filters);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> afterAll(IFilter<E> filter) {
        AbsStreaming<E, E> ret = new AfterAllStreaming<>(filter);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> beforeAll(IFilter<E> filter) {
        AbsStreaming<E, E> ret = new BeforeAllStreaming<>(filter);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> rangeAll(IFilter<E> beginFilter, IFilter<E> endFilter) {
        AbsStreaming<E, E> ret = new RangeAllStreaming<>(beginFilter, endFilter);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> peek(Consumer<E> consumer) {
        AbsStreaming<E, E> ret = new PeekStreaming<>(consumer);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> log(IExecutor<Object[]> executor, Object... args) {
        AbsStreaming<E, E> ret = new LogStreaming<>(executor, args);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> union(Streaming<E> stream) {
        AbsStreaming<E, E> ret = new UnionStreaming<>(stream);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public <R, T> Streaming<R> join(Streaming<T> stream, BiPredicate<E, T> joiner, BiFunction<R, E, T> processor) {
        AbsStreaming<E, R> ret = (AbsStreaming<E, R>) new JoinStreaming<R, E, T>(stream, joiner, processor);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> include(Streaming<E> stream) {
        AbsStreaming<E, E> ret = new IncludeStreaming<>(stream);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> exclude(Streaming<E> stream) {
        AbsStreaming<E, E> ret = new ExcludeStreaming<>(stream);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> distinct() {
        AbsStreaming<E, E> ret = new DistinctStreaming<>();
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<E> shuffle() {
        return null;
    }

    @Override
    public Streaming<E> sort(Comparator<E> comparator) {
        AbsStreaming<E, E> ret = new SortStreaming<>(comparator);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public <R> Streaming<R> map(IMapper<R, E> mapper) {
        AbsStreaming<E, R> ret = (AbsStreaming<E, R>) new MapStreaming<E, R>(mapper);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public <R> Streaming<R> flatMap(BiConsumer<E, Collection<R>> mapper) {
        AbsStreaming<E, R> ret = (AbsStreaming<E, R>) new FlatMapStreaming<E, R>(mapper);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public <R> Streaming<R> aggregate(BiConsumer<Iterator<E>, Collection<R>> mapper) {
        AbsStreaming<E, R> ret = (AbsStreaming<E, R>) new AggregateStreaming<E, R>(mapper);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public E reduce(BiFunction<E, E, E> reducer) {
        AbsSinkStreaming<E, E, E> ret = new ReduceSinkStreaming<>(reducer);
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public <K> Streaming<Tuple2<K, E>> keyedReduce(BiSupplier<K, E> key, BiFunction<E, E, E> reducer) {
        AbsStreaming<K, Tuple2<K, E>> ret = (AbsStreaming<K, Tuple2<K, E>>) new KeyedReduceStreaming<K, E>(key, reducer);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public <K, RT> Streaming<Tuple2<K, RT>> keyedAggregate(BiSupplier<K, E> key, BiSupplier<RT, Iterator<E>> mapper) {
        AbsStreaming<E, Tuple2<K, RT>> ret = (AbsStreaming<E, Tuple2<K, RT>>) new KeyedAggregateStreaming<K, E, RT>(key, mapper);
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public Streaming<Tuple2<E, Integer>> countBy() {
        AbsStreaming<E, Tuple2<E, Integer>> ret = (AbsStreaming<E, Tuple2<E, Integer>>) new CountByStreaming<E>();
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    @Override
    public <R, C extends Collection<R>> C collect(C col) {
        AbsSinkStreaming<C, R, E> ret = new CollectSinkStreaming<>(col);
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public <K, V, MAP extends Map<K, V>> MAP collect(MAP map, BiSupplier<K, E> key, BiSupplier<V, E> val) {
        AbsSinkStreaming<MAP, E, E> ret = new CollectMapSinkStreaming<>(map, key, val);
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public void each(IExecutor<E> executor) {
        AbsSinkStreaming<Void, E, E> ret = new EachSinkStreaming<>(executor);
        ret.prev = this;
        this.next = ret;
        ret.sink();
    }

    @Override
    public E first() {
        AbsSinkStreaming<E, E, E> ret = new FirstSinkStreaming<E>();
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public E last() {
        AbsSinkStreaming<E, E, E> ret = new LastSinkStreaming<E>();
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public E firstMatch(IFilter<E> filter) {
        AbsSinkStreaming<E, E, E> ret = new FirstMatchSinkStreaming<E>(filter);
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public E lastMatch(IFilter<E> filter) {
        AbsSinkStreaming<E, E, E> ret = new LastMatchSinkStreaming<E>(filter);
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public Iterator<E> iterator() {
        AbsSinkStreaming<Iterator<E>, E, E> ret = new IteratorSinkStreaming<E>();
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public int count() {
        AbsSinkStreaming<Integer, E, E> ret = new CountSinkStreaming<E>();
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public E max(Comparator<E> comparator) {
        AbsSinkStreaming<E, E, E> ret = new MaxSinkStreaming<E>(comparator);
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public E min(Comparator<E> comparator) {
        AbsSinkStreaming<E, E, E> ret = new MinSinkStreaming<E>(comparator);
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public boolean anyMatch(IFilter<E> filter) {
        AbsSinkStreaming<Boolean, E, E> ret = new AnyMatchSinkStreaming<E>(filter);
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public boolean allMatch(IFilter<E> filter) {
        AbsSinkStreaming<Boolean, E, E> ret = new AllMatchSinkStreaming<E>(filter);
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }

    @Override
    public Stream<E> stream() {
        AbsSinkStreaming<Stream<E>, E, E> ret = new StreamSinkStreaming<E>();
        ret.prev = this;
        this.next = ret;
        return ret.sink();
    }
}
