package i2f.core.streaming;

import i2f.core.functional.common.IExecutor;
import i2f.core.functional.common.IFilter;
import i2f.core.functional.common.IMapper;
import i2f.core.functional.jvf.*;
import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.api.process.IProcessStreaming;
import i2f.core.streaming.api.sink.ISinkStreaming;
import i2f.core.thread.NamingForkJoinPool;
import i2f.core.tuple.Tuples;
import i2f.core.tuple.impl.Tuple2;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collector;

public class GroupStreamingImpl<K, E> implements GroupStreaming<K, E> {
    private Iterator<Tuple2<K, Streaming<E>>> streams;
    private GroupStreamingImpl<?, ?> prev;
    private GroupStreamingImpl<?, ?> next;
    private ExecutorService pool;
    private Boolean parallel;
    private Integer parallelize;

    public GroupStreamingImpl(Iterator<Tuple2<K, Streaming<E>>> streams) {
        this.streams = streams;
    }

    public static <K, E> GroupStreamingImpl<K, E> of(Streaming<Tuple2<K, Collection<E>>> stream) {
        Iterator<Tuple2<K, Streaming<E>>> iterator = new LazyIterator<Tuple2<K, Streaming<E>>>(() -> {
            LinkedList<Tuple2<K, Collection<E>>> datas = stream.collect(new LinkedList<Tuple2<K, Collection<E>>>());
            LinkedList<Tuple2<K, Streaming<E>>> streams = new LinkedList<>();
            for (Tuple2<K, Collection<E>> data : datas) {
                streams.add(Tuples.of(data.t1, Streaming.of(data.t2)));
            }
            return streams.iterator();
        });
        return new GroupStreamingImpl<>(iterator);
    }

    public static <K, E> GroupStreamingImpl<K, E> of(Streaming<E> streaming, BiSupplier<K, E> keySupplier) {
        Streaming<Tuple2<K, Collection<E>>> ret = streaming.keyBy(keySupplier);
        return of(ret);
    }

    public ExecutorService getPool() {
        if (!isGroupParallel()) {
            return null;
        }
        if (this.pool == null) {
            synchronized (this) {
                int parallelSize = Runtime.getRuntime().availableProcessors();
                if (this.parallelize != null) {
                    if (this.parallelize > 0) {
                        parallelSize = this.parallelize;
                    }
                }
                this.pool = NamingForkJoinPool.getPool(parallelSize, "streaming", "group");
            }
        }
        return this.pool;
    }

    @Override
    public GroupStreaming<K, E> groupParallel() {
        this.parallel = true;
        return this;
    }

    @Override
    public GroupStreaming<K, E> groupSequential() {
        this.parallel = false;
        return this;
    }

    @Override
    public boolean isGroupParallel() {
        return parallel != null && parallel;
    }

    @Override
    public GroupStreaming<K, E> groupParallelize(int cnt) {
        this.parallelize = cnt;
        return this;
    }

    public <R> GroupStreamingImpl<K, R> wrapperProcess(IMapper<Streaming<R>, Streaming<E>> mapper) {
        GroupStreamingImpl<K, R> ret = new GroupStreamingImpl<K, R>(null);
        Iterator<Tuple2<K, Streaming<R>>> nextIter = new LazyIterator<>(() -> {
            Iterator<Tuple2<K, Streaming<R>>> iter = AbsStreaming.parallelizeProcess(streams, getPool(), (stream, collect) -> {
                Streaming<R> next = mapper.get(stream.t2);
                collect.add(Tuples.of(stream.t1, next));
            });
            ret.pool = this.pool;
            ret.parallel = this.parallel;
            ret.parallelize = this.parallelize;
            return iter;
        });
        ret.streams = nextIter;
        ret.prev = this;
        this.next = ret;
        return ret;
    }

    public <R> Streaming<Tuple2<K, R>> wrapperSink(IMapper<R, Streaming<E>> mapper) {
        Iterator<Tuple2<K, R>> nextIter = new LazyIterator<>(() -> {
            return AbsStreaming.parallelizeProcess(streams, getPool(), (stream, collect) -> {
                R next = mapper.get(stream.t2);
                collect.add(Tuples.of(stream.t1, next));
            });
        });
        return Streaming.of(nextIter);
    }

    public void wrapperExecute(IExecutor<Streaming<E>> executor) {
        AbsStreaming.parallelizeProcess(streams, getPool(), (stream, collect) -> {
            executor.accept(stream.t2);
        });
    }


    @Override
    public Streaming<Tuple2<K, Collection<E>>> streaming() {
        Iterator<Tuple2<K, Collection<E>>> iter = new LazyIterator<>(() -> {
            List<Tuple2<K, Collection<E>>> ret = new LinkedList<>();
            while (streams.hasNext()) {
                Tuple2<K, Streaming<E>> stream = streams.next();
                LinkedList<E> list = stream.t2.collect(new LinkedList<E>());
                ret.add(Tuples.of(stream.t1, list));
            }
            return ret.iterator();
        });
        return Streaming.of(iter);
    }

    @Override
    public Streaming<E> flat() {
        Iterator<E> iter = new LazyIterator<>(() -> {
            List<E> ret = new LinkedList<>();
            while (streams.hasNext()) {
                Tuple2<K, Streaming<E>> stream = streams.next();
                LinkedList<E> list = stream.t2.collect(new LinkedList<E>());
                ret.addAll(list);
            }
            return ret.iterator();
        });
        return Streaming.of(iter);
    }

    @Override
    public <R> GroupStreamingImpl<K, R> process(IProcessStreaming<R, E> processor) {
        return wrapperProcess((stream) -> stream.process(processor));
    }

    @Override
    public <R> Streaming<Tuple2<K, R>> sink(ISinkStreaming<R, E> processor) {
        return wrapperSink((stream) -> stream.sink(processor));
    }

    @Override
    public GroupStreamingImpl<K, E> parallel() {
        return wrapperProcess((stream) -> stream.parallel());
    }

    @Override
    public GroupStreamingImpl<K, E> sequential() {
        return wrapperProcess((stream) -> stream.sequential());
    }

    @Override
    public GroupStreamingImpl<K, E> parallelize(int cnt) {
        return wrapperProcess((stream) -> stream.parallelize(cnt));
    }

    @Override
    public Streaming<Tuple2<K, Boolean>> isParallel() {
        return wrapperSink((stream) -> stream.isParallel());
    }

    @Override
    public GroupStreamingImpl<K, E> skip(int len) {
        return wrapperProcess((stream) -> stream.skip(len));
    }

    @Override
    public GroupStreamingImpl<K, E> limit(int len) {
        return wrapperProcess((stream) -> stream.limit(len));
    }

    @Override
    public GroupStreamingImpl<K, E> limit(int offset, int len) {
        return wrapperProcess((stream) -> stream.limit(offset, len));
    }

    @Override
    public GroupStreamingImpl<K, E> filter(IFilter<E> filter) {
        return wrapperProcess((stream) -> stream.filter(filter));
    }

    @Override
    public GroupStreamingImpl<K, E> andFilter(IFilter<E>... filters) {
        return wrapperProcess((stream) -> stream.andFilter(filters));
    }

    @Override
    public GroupStreamingImpl<K, E> orFilter(IFilter<E>... filters) {
        return wrapperProcess((stream) -> stream.orFilter(filters));
    }

    @Override
    public GroupStreamingImpl<K, E> afterAll(IFilter<E> filter) {
        return wrapperProcess((stream) -> stream.afterAll(filter));
    }

    @Override
    public GroupStreamingImpl<K, E> beforeAll(IFilter<E> filter) {
        return wrapperProcess((stream) -> stream.beforeAll(filter));
    }

    @Override
    public GroupStreamingImpl<K, E> rangeAll(IFilter<E> beginFilter, IFilter<E> endFilter) {
        return wrapperProcess((stream) -> stream.rangeAll(beginFilter, endFilter));
    }

    @Override
    public GroupStreamingImpl<K, E> peek(Consumer<E> consumer) {
        return wrapperProcess((stream) -> stream.peek(consumer));
    }

    @Override
    public GroupStreamingImpl<K, E> log(IExecutor<Object[]> executor, Object... args) {
        return wrapperProcess((stream) -> stream.log(executor, args));
    }

    @Override
    public GroupStreamingImpl<K, E> distinct() {
        return wrapperProcess((stream) -> stream.distinct());

    }

    @Override
    public GroupStreamingImpl<K, E> sort(Comparator<E> comparator) {
        return wrapperProcess((stream) -> stream.sort(comparator));
    }

    @Override
    public GroupStreamingImpl<K, E> shuffle() {
        return wrapperProcess((stream) -> stream.shuffle());
    }

    @Override
    public <R> GroupStreamingImpl<K, R> map(IMapper<R, E> mapper) {
        return wrapperProcess((stream) -> stream.map(mapper));
    }

    @Override
    public <R> GroupStreamingImpl<K, R> flatMap(BiConsumer<E, Collection<R>> mapper) {
        return wrapperProcess((stream) -> stream.flatMap(mapper));
    }

    @Override
    public <R> GroupStreamingImpl<K, R> aggregate(BiConsumer<Iterator<E>, Collection<R>> mapper) {
        return wrapperProcess((stream) -> stream.aggregate(mapper));
    }

    @Override
    public Streaming<Tuple2<K, E>> reduce(BiFunction<E, E, E> reducer) {
        return wrapperSink((stream) -> stream.reduce(reducer));
    }

    @Override
    public Streaming<Tuple2<K, E[]>> array(Class<E[]> tarType) {
        return wrapperSink((stream) -> stream.array(tarType));
    }

    @Override
    public void each(IExecutor<E> executor) {
        wrapperExecute((stream) -> stream.each(executor));
    }

    @Override
    public Streaming<Tuple2<K, E>> first() {
        return wrapperSink(stream -> stream.first());
    }

    @Override
    public Streaming<Tuple2<K, E>> last() {
        return wrapperSink(stream -> stream.last());
    }

    @Override
    public Streaming<Tuple2<K, E>> firstMatch(IFilter<E> filter) {
        return wrapperSink(stream -> stream.firstMatch(filter));
    }

    @Override
    public Streaming<Tuple2<K, E>> lastMatch(IFilter<E> filter) {
        return wrapperSink(stream -> stream.lastMatch(filter));
    }

    @Override
    public Streaming<Tuple2<K, Integer>> count() {
        return wrapperSink(stream -> stream.count());
    }

    @Override
    public Streaming<Tuple2<K, E>> max(Comparator<E> comparator) {
        return wrapperSink(stream -> stream.max(comparator));
    }

    @Override
    public Streaming<Tuple2<K, E>> min(Comparator<E> comparator) {
        return wrapperSink(stream -> stream.min(comparator));
    }

    @Override
    public Streaming<Tuple2<K, Boolean>> anyMatch(IFilter<E> filter) {
        return wrapperSink(stream -> stream.anyMatch(filter));
    }

    @Override
    public Streaming<Tuple2<K, Boolean>> allMatch(IFilter<E> filter) {
        return wrapperSink(stream -> stream.allMatch(filter));
    }

    @Override
    public Streaming<Tuple2<K, String>> stringify(String open, String separator, String close) {
        return wrapperSink(stream -> stream.stringify(open, separator, close));
    }

    @Override
    public Streaming<Tuple2<K, String>> stringify(String separator) {
        return wrapperSink(stream -> stream.stringify(separator));
    }

    @Override
    public <R, A> Streaming<Tuple2<K, R>> collect(Collector<E, A, R> collector) {
        return wrapperSink(stream -> stream.collect(collector));
    }

}
