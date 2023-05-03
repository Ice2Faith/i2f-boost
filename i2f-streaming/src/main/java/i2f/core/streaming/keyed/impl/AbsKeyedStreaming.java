package i2f.core.streaming.keyed.impl;

import i2f.core.streaming.Streaming;
import i2f.core.streaming.comparator.DefaultComparator;
import i2f.core.streaming.data.KeyedData;
import i2f.core.streaming.impl.AbsStreaming;
import i2f.core.streaming.impl.StreamingContext;
import i2f.core.streaming.iterator.LazyIterator;
import i2f.core.streaming.iterator.MergeIterator;
import i2f.core.streaming.keyed.KeyedStreaming;
import i2f.core.streaming.keyed.functional.*;
import i2f.core.streaming.keyed.process.KeyedProcessWrappers;
import i2f.core.streaming.keyed.process.KeyedStreamingProcessor;
import i2f.core.streaming.keyed.sink.KeyedSinkWrappers;
import i2f.core.streaming.keyed.sink.KeyedStreamingSinker;
import i2f.core.streaming.parallel.AtomicCountDownLatch;
import i2f.core.streaming.process.ProcessWrappers;
import i2f.core.streaming.process.StreamingProcessor;
import i2f.core.streaming.rich.RichStreamingWrapper;
import i2f.core.streaming.sink.SinkWrappers;
import i2f.core.streaming.sink.StreamingSinkWrapper;
import i2f.core.streaming.sink.StreamingSinker;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;


/**
 * @author Ice2Faith
 * @date 2023/4/22 21:47
 * @desc
 */
public class AbsKeyedStreaming<K, E> implements KeyedStreaming<K, E> {
    protected StreamingContext context;
    protected Iterator<KeyedData<K, Streaming<E>>> keyIterator;

    public AbsKeyedStreaming(Iterator<KeyedData<K, Streaming<E>>> keyIterator) {
        this.keyIterator = keyIterator;
    }

    public static <K, E> AbsKeyedStreaming<K, E> source(Function<E, K> keyer, Iterator<E> iterator) {
        StreamingContext context = new StreamingContext();
        context.setPool(StreamingContext.defaultPool);
        Iterator<KeyedData<K, Streaming<E>>> keyIterator = new LazyIterator<>(() -> {
            ConcurrentHashMap<K, List<E>> keyMap = new ConcurrentHashMap<>();
            List<E> nullList = new LinkedList<>();
            boolean hasNull = false;
            while (iterator.hasNext()) {
                E val = iterator.next();
                K key = keyer.apply(val);
                if (key == null) {
                    nullList.add(val);
                    hasNull = true;
                } else {
                    if (!keyMap.containsKey(key)) {
                        keyMap.put(key, new LinkedList<>());
                    }
                    keyMap.get(key).add(val);
                }
            }

            LinkedList<KeyedData<K, Streaming<E>>> keys = new LinkedList<>();
            for (K key : keyMap.keySet()) {
                List<E> list = keyMap.get(key);
                keys.add(new KeyedData<>(key, AbsStreaming.source(list.iterator())));
            }
            if (hasNull) {
                keys.add(new KeyedData<>(null, AbsStreaming.source(nullList.iterator())));
            }
            return keys.iterator();
        });
        AbsKeyedStreaming<K, E> ret = new AbsKeyedStreaming<>(keyIterator);
        ret.setContext(context);
        return ret;
    }


    public StreamingContext getContext() {
        return context;
    }

    @Override
    public KeyedStreaming<K, E> setContext(StreamingContext context) {
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


    @Override
    public KeyedStreaming<K, E> parallel() {
        context.setParallel(true);
        return this;
    }

    @Override
    public KeyedStreaming<K, E> sequential() {
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

    @Override
    public <OUT> KeyedStreaming<K, OUT> process(KeyedStreamingProcessor<K, E, OUT> processor, Object... args) {
        inject(processor, args);
        Iterator<KeyedData<K, Streaming<OUT>>> ret = new LazyIterator<>(() -> {
            List<KeyedData<K, Streaming<OUT>>> list = new CopyOnWriteArrayList<>();
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                latch.count();
                if (getContext().isParallel()) {
                    getContext().getPool().submit(() -> {
                        try {
                            Streaming<OUT> out = ((AbsStreaming<E>) curr.val).process(curr.key, processor, args);
                            list.add(new KeyedData<>(curr.key, out));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            latch.down();
                        }
                    });
                } else {
                    Streaming<OUT> out = ((AbsStreaming<E>) curr.val).process(curr.key, processor, args);
                    list.add(new KeyedData<>(curr.key, out));
                    latch.down();
                }
            }
            latch.finish();
            try {
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list.iterator();
        });
        return new AbsKeyedStreaming<K, OUT>(ret).setContext(copyContext());
    }

    @Override
    public <OUT> Streaming<KeyedData<K, OUT>> sink(KeyedStreamingSinker<K, E, OUT> sinker, Object... args) {
        inject(sinker, args);
        Iterator<KeyedData<K, OUT>> iterator = new LazyIterator<>(() -> {
            List<KeyedData<K, OUT>> list = new CopyOnWriteArrayList<>();
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                latch.count();
                if (getContext().isParallel()) {
                    getContext().getPool().submit(() -> {
                        try {
                            OUT out = ((AbsStreaming<E>) curr.val).sink(curr.key, sinker, args);
                            list.add(new KeyedData<>(curr.key, out));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            latch.down();
                        }
                    });
                } else {
                    OUT out = ((AbsStreaming<E>) curr.val).sink(curr.key, sinker, args);
                    list.add(new KeyedData<>(curr.key, out));
                    latch.down();
                }
            }
            latch.finish();
            try {
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list.iterator();
        });
        return AbsStreaming.source(iterator).setContext(copyContext());
    }


    @Override
    public <OUT> Streaming<OUT> merge(KeyedStreamingSinker<K, E, OUT> sinker, Object... args) {
        inject(sinker, args);
        Iterator<OUT> iterator = new LazyIterator<>(() -> {
            List<OUT> list = new CopyOnWriteArrayList<>();
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                latch.count();
                if (getContext().isParallel()) {
                    getContext().getPool().submit(() -> {
                        try {
                            OUT out = ((AbsStreaming<E>) curr.val).sink(curr.key, sinker, args);
                            list.add(out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            latch.down();
                        }
                    });
                } else {
                    OUT out = ((AbsStreaming<E>) curr.val).sink(curr.key, sinker, args);
                    list.add(out);
                    latch.down();
                }
            }
            latch.finish();
            try {
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list.iterator();
        });
        return AbsStreaming.source(iterator).setContext(copyContext());
    }


    @Override
    public <OUT> KeyedStreaming<K, OUT> process(StreamingProcessor<E, OUT> processor, Object... args) {
        inject(processor, args);
        Iterator<KeyedData<K, Streaming<OUT>>> ret = new LazyIterator<>(() -> {
            List<KeyedData<K, Streaming<OUT>>> list = new CopyOnWriteArrayList<>();
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                latch.count();
                if (getContext().isParallel()) {
                    getContext().getPool().submit(() -> {
                        try {
                            Streaming<OUT> out = curr.val.process(processor, args);
                            list.add(new KeyedData<>(curr.key, out));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            latch.down();
                        }
                    });
                } else {
                    Streaming<OUT> out = curr.val.process(processor, args);
                    list.add(new KeyedData<>(curr.key, out));
                    latch.down();
                }
            }
            latch.finish();
            try {
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list.iterator();
        });
        return new AbsKeyedStreaming<K, OUT>(ret).setContext(copyContext());
    }

    @Override
    public <OUT> Streaming<KeyedData<K, OUT>> sink(StreamingSinker<E, OUT> sinker, Object... args) {
        inject(sinker, args);
        Iterator<KeyedData<K, OUT>> iterator = new LazyIterator<>(() -> {
            List<KeyedData<K, OUT>> list = new CopyOnWriteArrayList<>();
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                latch.count();
                if (getContext().isParallel()) {
                    getContext().getPool().submit(() -> {
                        try {
                            OUT out = curr.val.sink(sinker, args);
                            list.add(new KeyedData<>(curr.key, out));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            latch.down();
                        }
                    });
                } else {
                    OUT out = curr.val.sink(sinker, args);
                    list.add(new KeyedData<>(curr.key, out));
                    latch.down();
                }
            }
            latch.finish();
            try {
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list.iterator();
        });
        return AbsStreaming.source(iterator).setContext(copyContext());
    }


    @Override
    public <OUT> Streaming<OUT> merge(StreamingSinker<E, OUT> sinker, Object... args) {
        inject(sinker, args);
        Iterator<OUT> iterator = new LazyIterator<>(() -> {
            List<OUT> list = new CopyOnWriteArrayList<>();
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                latch.count();
                if (getContext().isParallel()) {
                    getContext().getPool().submit(() -> {
                        try {
                            OUT out = curr.val.sink(sinker, args);
                            list.add(out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            latch.down();
                        }
                    });
                } else {
                    OUT out = curr.val.sink(sinker, args);
                    list.add(out);
                    latch.down();
                }
            }
            latch.finish();
            try {
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list.iterator();
        });
        return AbsStreaming.source(iterator).setContext(copyContext());
    }

    @Override
    public Streaming<E> flat() {
        Iterator<E> iterator = new LazyIterator<>(() -> {
            List<Iterator<E>> list = new LinkedList<>();
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> in = keyIterator.next();
                LazyIterator<E> iter = new LazyIterator<>(() -> {
                    return in.val.sink(new StreamingSinkWrapper<E, Iterator<E>>() {
                        @Override
                        public Iterator<E> sink(Iterator<E> iterator) {
                            return iterator;
                        }
                    });
                });
                list.add(iter);
            }
            return new MergeIterator<>(list.iterator());
        });
        return AbsStreaming.source(iterator).setContext(copyContext());
    }

    @Override
    public KeyedStreaming<K, E> filter(KeyedPredicate<K, E> filter, Object... args) {
        inject(filter, args);
        return process(KeyedProcessWrappers.filter(filter), args);
    }

    @Override
    public KeyedStreaming<K, E> before(KeyedPredicate<K, E> filter) {
        inject(filter);
        return process(KeyedProcessWrappers.before(filter));
    }

    @Override
    public KeyedStreaming<K, E> after(KeyedPredicate<K, E> filter) {
        inject(filter);
        return process(KeyedProcessWrappers.after(filter));
    }

    @Override
    public KeyedStreaming<K, E> between(KeyedPredicate<K, E> begin, KeyedPredicate<K, E> end) {
        inject(begin);
        inject(end);
        return process(KeyedProcessWrappers.between(begin, end));
    }

    @Override
    public <R> KeyedStreaming<K, E> distinct(KeyedFunction<K, E, R> keyer) {
        inject(keyer);
        return process(KeyedProcessWrappers.distinct(keyer));
    }

    @Override
    public <OUT> KeyedStreaming<K, OUT> map(KeyedFunction<K, E, OUT> mapper, Object... args) {
        inject(mapper, args);
        return process(KeyedProcessWrappers.mapper(mapper), args);
    }

    @Override
    public <OUT> KeyedStreaming<K, OUT> flatMap(KeyedBiConsumer<K, E, Consumer<OUT>> mapper, Object... args) {
        inject(mapper, args);
        return process(KeyedProcessWrappers.flatMap(mapper), args);
    }

    @Override
    public KeyedStreaming<K, E> sort(KeyedComparator<K, E> comparator, boolean anti, Object... args) {
        inject(comparator, args);
        return process(KeyedProcessWrappers.sort(comparator, anti), args);
    }

    @Override
    public Streaming<KeyedData<K, E>> reduce(KeyedBiFunction<K, E, E, E> reducer) {
        inject(reducer);
        return sink(KeyedSinkWrappers.reduce(reducer));
    }

    @Override
    public <OUT> Streaming<KeyedData<K, OUT>> aggregate(int computeSize, KeyedFunction<K, List<E>, OUT> computer, KeyedBiFunction<K, OUT, OUT, OUT> accumulator) {
        inject(computer);
        inject(accumulator);
        return sink(KeyedSinkWrappers.aggregate(computeSize, computer, accumulator));
    }

    @Override
    public <OUT> Streaming<KeyedData<K, OUT>> aggregate(KeyedFunction<K, List<E>, OUT> computer, KeyedBiFunction<K, OUT, OUT, OUT> accumulator) {
        return aggregate(256, computer, accumulator);
    }

    @Override
    public <OUT, ACC> Streaming<KeyedData<K, OUT>> aggregate(int batchSize, KeyedSupplier<K, ACC> creator, KeyedBiFunction<K, E, ACC, ACC> accumulator, KeyedBiFunction<K, ACC, ACC, ACC> merger, KeyedFunction<K, ACC, OUT> outputer) {
        inject(creator);
        inject(accumulator);
        inject(merger);
        inject(outputer);
        return sink(KeyedSinkWrappers.aggregate(batchSize, creator, accumulator, merger, outputer));
    }

    @Override
    public <OUT, ACC> Streaming<KeyedData<K, OUT>> aggregate(KeyedSupplier<K, ACC> creator, KeyedBiFunction<K, E, ACC, ACC> accumulator, KeyedBiFunction<K, ACC, ACC, ACC> merger, KeyedFunction<K, ACC, OUT> outputer) {
        return aggregate(1024, creator, accumulator, merger, outputer);
    }

    @Override
    public Streaming<KeyedData<K, E>> max(KeyedComparator<K, E> comparator) {
        inject(comparator);
        return sink(KeyedSinkWrappers.max(comparator));
    }

    @Override
    public Streaming<KeyedData<K, E>> min(KeyedComparator<K, E> comparator) {
        inject(comparator);
        return sink(KeyedSinkWrappers.min(comparator));
    }

    @Override
    public Streaming<KeyedData<K, E>> first(KeyedPredicate<K, E> filter) {
        inject(filter);
        return sink(KeyedSinkWrappers.first(filter));
    }

    @Override
    public Streaming<KeyedData<K, E>> last(KeyedPredicate<K, E> filter) {
        inject(filter);
        return sink(KeyedSinkWrappers.last(filter));
    }

    @Override
    public void each(KeyedConsumer<K, E> consumer) {
        inject(consumer);
        sink(KeyedSinkWrappers.each(consumer));
    }

    @Override
    public void each(KeyedPredicate<K, E> consumer) {
        inject(consumer);
        sink(KeyedSinkWrappers.each(consumer));
    }

    @Override
    public Streaming<KeyedData<K, Boolean>> anyMatch(KeyedPredicate<K, E> filter) {
        inject(filter);
        return sink(KeyedSinkWrappers.anyMatch(filter));
    }

    @Override
    public Streaming<KeyedData<K, Boolean>> allMatch(KeyedPredicate<K, E> filter) {
        inject(filter);
        return sink(KeyedSinkWrappers.allMatch(filter));
    }

    @Override
    public <C extends Collection<E>> void batch(int batchSize, KeyedSupplier<K, C> supplier, KeyedConsumer<K, C> consumer) {
        inject(supplier);
        inject(consumer);
        sink(KeyedSinkWrappers.batch(batchSize, supplier, consumer));
    }

    @Override
    public void batch(int batchSize, KeyedConsumer<K, Collection<E>> consumer) {
        inject(consumer);
        sink(KeyedSinkWrappers.batch(batchSize, (key) -> new LinkedList<>(), consumer));
    }

    @Override
    public KeyedStreaming<K, E> sync(KeyedConsumer<K, Collection<E>> consumer) {
        inject(consumer);
        return process(KeyedProcessWrappers.sync(consumer));
    }

    @Override
    public KeyedStreaming<K, E> peek(KeyedConsumer<K, E> consumer, Object... args) {
        inject(consumer, args);
        return process(KeyedProcessWrappers.peek(consumer), args);
    }

    @Override
    public KeyedStreaming<K, E> resample(double rate, KeyedConsumer<K, E> consumer) {
        inject(consumer);
        return process(KeyedProcessWrappers.resample(rate, consumer));
    }

    @Override
    public KeyedStreaming<K, E> filter(Predicate<E> filter, Object... args) {
        inject(filter, args);
        return process(ProcessWrappers.filter(filter), args);
    }

    @Override
    public KeyedStreaming<K, E> before(Predicate<E> filter) {
        inject(filter);
        return process(ProcessWrappers.before(filter));
    }

    @Override
    public KeyedStreaming<K, E> after(Predicate<E> filter) {
        inject(filter);
        return process(ProcessWrappers.after(filter));
    }

    @Override
    public KeyedStreaming<K, E> head(int count) {
        return process(ProcessWrappers.head(count));
    }

    @Override
    public KeyedStreaming<K, E> tail(int count) {
        return process(ProcessWrappers.tail(count));
    }

    @Override
    public KeyedStreaming<K, E> resample(double rate) {
        return process(ProcessWrappers.resample(rate));
    }

    @Override
    public KeyedStreaming<K, E> between(Predicate<E> begin, Predicate<E> end) {
        inject(begin);
        inject(end);
        return process(ProcessWrappers.between(begin, end));
    }

    @Override
    public KeyedStreaming<K, E> skip(int count) {
        return process(ProcessWrappers.skip(count));
    }

    @Override
    public KeyedStreaming<K, E> limit(int count) {
        return process(ProcessWrappers.limit(count));
    }

    @Override
    public <R> KeyedStreaming<K, E> distinct(Function<E, R> keyer) {
        inject(keyer);
        return process(ProcessWrappers.distinct(keyer));
    }

    @Override
    public KeyedStreaming<K, E> distinct() {
        return distinct((e) -> e);
    }

    @Override
    public <OUT> KeyedStreaming<K, OUT> map(Function<E, OUT> mapper, Object... args) {
        inject(mapper, args);
        return process(ProcessWrappers.mapper(mapper), args);
    }

    @Override
    public <OUT> KeyedStreaming<K, OUT> flatMap(BiConsumer<E, Consumer<OUT>> mapper, Object... args) {
        inject(mapper, args);
        return process(ProcessWrappers.flatMap(mapper), args);
    }

    @Override
    public KeyedStreaming<K, E> sort(Comparator<E> comparator, boolean anti, Object... args) {
        inject(comparator, args);
        return process(ProcessWrappers.sort(comparator, anti), args);
    }

    @Override
    public KeyedStreaming<K, E> sort(boolean anti) {
        return sort(new DefaultComparator<E>(), anti);
    }

    @Override
    public KeyedStreaming<K, E> sort() {
        return sort(false);
    }

    @Override
    public KeyedStreaming<K, E> shuffle() {
        return process(ProcessWrappers.shuffle());
    }

    @Override
    public KeyedStreaming<K, E> reverse() {
        return process(ProcessWrappers.reverse());
    }

    @Override
    public Streaming<KeyedData<K, E>> reduce(BiFunction<E, E, E> reducer) {
        inject(reducer);
        return sink(SinkWrappers.reduce(reducer));
    }

    @Override
    public <OUT> Streaming<KeyedData<K, OUT>> aggregate(int computeSize, Function<List<E>, OUT> computer, BiFunction<OUT, OUT, OUT> accumulator) {
        inject(computer);
        inject(accumulator);
        return sink(SinkWrappers.aggregate(computeSize, computer, accumulator));
    }

    @Override
    public <OUT> Streaming<KeyedData<K, OUT>> aggregate(Function<List<E>, OUT> computer, BiFunction<OUT, OUT, OUT> accumulator) {
        return aggregate(256, computer, accumulator);
    }

    @Override
    public <OUT, ACC> Streaming<KeyedData<K, OUT>> aggregate(int batchSize, Supplier<ACC> creator, BiFunction<E, ACC, ACC> accumulator, BiFunction<ACC, ACC, ACC> merger, Function<ACC, OUT> outputer) {
        inject(creator);
        inject(accumulator);
        inject(merger);
        inject(outputer);
        return sink(SinkWrappers.aggregate(batchSize, creator, accumulator, merger, outputer));
    }

    @Override
    public <OUT, ACC> Streaming<KeyedData<K, OUT>> aggregate(Supplier<ACC> creator, BiFunction<E, ACC, ACC> accumulator, BiFunction<ACC, ACC, ACC> merger, Function<ACC, OUT> outputer) {
        return aggregate(1024, creator, accumulator, merger, outputer);
    }

    @Override
    public Streaming<KeyedData<K, E>> max(Comparator<E> comparator) {
        inject(comparator);
        return sink(SinkWrappers.max(comparator));
    }

    @Override
    public Streaming<KeyedData<K, E>> min(Comparator<E> comparator) {
        inject(comparator);
        return sink(SinkWrappers.min(comparator));
    }

    @Override
    public Streaming<KeyedData<K, Integer>> count() {
        return sink(SinkWrappers.count());
    }

    @Override
    public Streaming<KeyedData<K, E>> first(Predicate<E> filter) {
        inject(filter);
        return sink(SinkWrappers.first(filter));
    }

    @Override
    public Streaming<KeyedData<K, E>> first() {
        return first((e) -> true);
    }

    @Override
    public Streaming<KeyedData<K, E>> last(Predicate<E> filter) {
        inject(filter);
        return sink(SinkWrappers.last(filter));
    }

    @Override
    public Streaming<KeyedData<K, E>> last() {
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
    public Streaming<KeyedData<K, Boolean>> anyMatch(Predicate<E> filter) {
        inject(filter);
        return sink(SinkWrappers.anyMatch(filter));
    }

    @Override
    public Streaming<KeyedData<K, Boolean>> allMatch(Predicate<E> filter) {
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
    public KeyedStreaming<K, E> sync(Consumer<Collection<E>> consumer) {
        inject(consumer);
        return process(ProcessWrappers.sync(consumer));
    }

    @Override
    public KeyedStreaming<K, E> peek(Consumer<E> consumer, Object... args) {
        inject(consumer, args);
        return process(ProcessWrappers.peek(consumer), args);
    }

    @Override
    public KeyedStreaming<K, E> fork(Consumer<KeyedStreaming<K, E>> consumer) {
        inject(consumer);
        Iterator<KeyedData<K, Streaming<E>>> ret = new LazyIterator<>(() -> {
            List<KeyedData<K, List<E>>> list = new LinkedList<>();
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                list.add(new KeyedData<>(curr.key, curr.val.collection(new LinkedList<>())));
            }

            List<KeyedData<K, Streaming<E>>> origin = new LinkedList<>();
            List<KeyedData<K, Streaming<E>>> fk = new LinkedList<>();
            for (KeyedData<K, List<E>> item : list) {
                origin.add(new KeyedData<>(item.key, Streaming.source(item.val).setContext(copyContext())));
                fk.add(new KeyedData<>(item.key, Streaming.source(item.val).setContext(copyContext())));
            }
            consumer.accept(new AbsKeyedStreaming<>(fk.iterator()).setContext(copyContext()));

            return origin.iterator();
        });
        return new AbsKeyedStreaming<K, E>(ret).setContext(copyContext());
    }

    @Override
    public KeyedStreaming<K, E> join(KeyedStreaming<K, E> streaming) {
        Iterator<KeyedData<K, Streaming<E>>> ret = new LazyIterator<>(() -> {
            Map<K, Streaming<E>> srcMap = new HashMap<>();
            Streaming<E> srcNull = null;
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                if (curr.key != null) {
                    srcMap.put(curr.key, curr.val);
                } else {
                    srcNull = curr.val;
                }
            }
            List<KeyedData<K, Iterator<E>>> dst = streaming.iterator().list();
            Map<K, Iterator<E>> dstMap = new HashMap<>();
            Iterator<E> dstNull = null;
            for (KeyedData<K, Iterator<E>> item : dst) {
                if (item.key != null) {
                    dstMap.put(item.key, item.val);
                } else {
                    dstNull = item.val;
                }
            }

            List<KeyedData<K, Streaming<E>>> list = new LinkedList<>();

            for (K key : srcMap.keySet()) {
                Streaming<E> strm = srcMap.get(key);
                if (dstMap.containsKey(key)) {
                    Iterator<E> iter = dstMap.get(key);
                    dstMap.remove(key);
                    Streaming<E> jns = strm.join(Streaming.source(iter).setContext(copyContext()));
                    list.add(new KeyedData<>(key, jns));
                } else {
                    list.add(new KeyedData<>(key, strm));
                }
            }

            for (K key : dstMap.keySet()) {
                Iterator<E> iter = dstMap.get(key);
                list.add(new KeyedData<>(key, Streaming.source(iter).setContext(copyContext())));
            }

            if (srcNull != null && dstNull != null) {
                Streaming<E> jns = srcNull.join(Streaming.source(dstNull).setContext(copyContext()));
                list.add(new KeyedData<>(null, jns));
            } else if (srcNull != null) {
                list.add(new KeyedData<>(null, srcNull));
            } else if (dstNull != null) {
                list.add(new KeyedData<>(null, Streaming.source(dstNull).setContext(copyContext())));
            }

            return list.iterator();
        });
        return new AbsKeyedStreaming<K, E>(ret).setContext(copyContext());
    }

    @Override
    public KeyedStreaming<K, E> merge(KeyedStreaming<K, E> streaming, Comparator<E> comparator) {
        inject(comparator);
        Iterator<KeyedData<K, Streaming<E>>> ret = new LazyIterator<>(() -> {
            Map<K, Streaming<E>> srcMap = new HashMap<>();
            Streaming<E> srcNull = null;
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                if (curr.key != null) {
                    srcMap.put(curr.key, curr.val);
                } else {
                    srcNull = curr.val;
                }
            }
            List<KeyedData<K, Iterator<E>>> dst = streaming.iterator().list();
            Map<K, Iterator<E>> dstMap = new HashMap<>();
            Iterator<E> dstNull = null;
            for (KeyedData<K, Iterator<E>> item : dst) {
                if (item.key != null) {
                    dstMap.put(item.key, item.val);
                } else {
                    dstNull = item.val;
                }
            }

            List<KeyedData<K, Streaming<E>>> list = new LinkedList<>();

            for (K key : srcMap.keySet()) {
                Streaming<E> strm = srcMap.get(key);
                if (dstMap.containsKey(key)) {
                    Iterator<E> iter = dstMap.get(key);
                    dstMap.remove(key);
                    Streaming<E> jns = strm.merge(Streaming.source(iter).setContext(copyContext()), comparator);
                    list.add(new KeyedData<>(key, jns));
                } else {
                    list.add(new KeyedData<>(key, strm));
                }
            }

            for (K key : dstMap.keySet()) {
                Iterator<E> iter = dstMap.get(key);
                list.add(new KeyedData<>(key, Streaming.source(iter).setContext(copyContext())));
            }

            if (srcNull != null && dstNull != null) {
                Streaming<E> jns = srcNull.merge(Streaming.source(dstNull).setContext(copyContext()), comparator);
                list.add(new KeyedData<>(null, jns));
            } else if (srcNull != null) {
                list.add(new KeyedData<>(null, srcNull));
            } else if (dstNull != null) {
                list.add(new KeyedData<>(null, Streaming.source(dstNull).setContext(copyContext())));
            }

            return list.iterator();
        });
        return new AbsKeyedStreaming<K, E>(ret).setContext(copyContext());
    }

    @Override
    public <OUT, CO> KeyedStreaming<K, OUT> connect(KeyedStreaming<K, CO> coStreaming, BiPredicate<E, CO> condition, BiFunction<E, CO, OUT> linker) {
        inject(condition);
        inject(linker);
        Iterator<KeyedData<K, Streaming<OUT>>> ret = new LazyIterator<>(() -> {
            Map<K, Streaming<E>> srcMap = new HashMap<>();
            Streaming<E> srcNull = null;
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                if (curr.key != null) {
                    srcMap.put(curr.key, curr.val);
                } else {
                    srcNull = curr.val;
                }
            }
            List<KeyedData<K, Iterator<CO>>> dst = coStreaming.iterator().list();
            Map<K, Iterator<CO>> dstMap = new HashMap<>();
            Iterator<CO> dstNull = null;
            for (KeyedData<K, Iterator<CO>> item : dst) {
                if (item.key != null) {
                    dstMap.put(item.key, item.val);
                } else {
                    dstNull = item.val;
                }
            }

            List<KeyedData<K, Streaming<OUT>>> list = new LinkedList<>();

            for (K key : srcMap.keySet()) {
                Streaming<E> strm = srcMap.get(key);
                if (dstMap.containsKey(key)) {
                    Iterator<CO> iter = dstMap.get(key);
                    dstMap.remove(key);
                    Streaming<OUT> jns = strm.connect(Streaming.source(iter).setContext(copyContext()), condition, linker);
                    list.add(new KeyedData<>(key, jns));
                }
            }


            if (srcNull != null && dstNull != null) {
                Streaming<OUT> jns = srcNull.connect(Streaming.source(dstNull).setContext(copyContext()), condition, linker);
                list.add(new KeyedData<>(null, jns));
            }

            return list.iterator();
        });
        return new AbsKeyedStreaming<K, OUT>(ret).setContext(copyContext());
    }

    @Override
    public <OUT, CO> KeyedStreaming<K, OUT> combine(KeyedStreaming<K, CO> coStreaming, BiFunction<E, CO, OUT> linker) {
        inject(coStreaming);
        inject(linker);
        Iterator<KeyedData<K, Streaming<OUT>>> ret = new LazyIterator<>(() -> {
            Map<K, Streaming<E>> srcMap = new HashMap<>();
            Streaming<E> srcNull = null;
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                if (curr.key != null) {
                    srcMap.put(curr.key, curr.val);
                } else {
                    srcNull = curr.val;
                }
            }
            List<KeyedData<K, Iterator<CO>>> dst = coStreaming.iterator().list();
            Map<K, Iterator<CO>> dstMap = new HashMap<>();
            Iterator<CO> dstNull = null;
            for (KeyedData<K, Iterator<CO>> item : dst) {
                if (item.key != null) {
                    dstMap.put(item.key, item.val);
                } else {
                    dstNull = item.val;
                }
            }

            List<KeyedData<K, Streaming<OUT>>> list = new LinkedList<>();

            for (K key : srcMap.keySet()) {
                Streaming<E> strm = srcMap.get(key);
                if (dstMap.containsKey(key)) {
                    Iterator<CO> iter = dstMap.get(key);
                    dstMap.remove(key);
                    Streaming<OUT> jns = strm.combine(Streaming.source(iter).setContext(copyContext()), linker);
                    list.add(new KeyedData<>(key, jns));
                }
            }


            if (srcNull != null && dstNull != null) {
                Streaming<OUT> jns = srcNull.combine(Streaming.source(dstNull).setContext(copyContext()), linker);
                list.add(new KeyedData<>(null, jns));
            }

            return list.iterator();
        });
        return new AbsKeyedStreaming<K, OUT>(ret).setContext(copyContext());
    }

    @Override
    public KeyedStreaming<K, E> resample(double rate, Consumer<E> consumer) {
        inject(consumer);
        return process(ProcessWrappers.resample(rate, consumer));
    }

    @Override
    public <R> KeyedStreaming<K, E> include(KeyedStreaming<K, E> streaming, Function<E, R> keyer) {
        inject(streaming);
        inject(keyer);
        Iterator<KeyedData<K, Streaming<E>>> ret = new LazyIterator<>(() -> {
            Map<K, Streaming<E>> srcMap = new HashMap<>();
            Streaming<E> srcNull = null;
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                if (curr.key != null) {
                    srcMap.put(curr.key, curr.val);
                } else {
                    srcNull = curr.val;
                }
            }
            List<KeyedData<K, Iterator<E>>> dst = streaming.iterator().list();
            Map<K, Iterator<E>> dstMap = new HashMap<>();
            Iterator<E> dstNull = null;
            for (KeyedData<K, Iterator<E>> item : dst) {
                if (item.key != null) {
                    dstMap.put(item.key, item.val);
                } else {
                    dstNull = item.val;
                }
            }

            List<KeyedData<K, Streaming<E>>> list = new LinkedList<>();

            for (K key : srcMap.keySet()) {
                Streaming<E> strm = srcMap.get(key);
                if (dstMap.containsKey(key)) {
                    Iterator<E> iter = dstMap.get(key);
                    dstMap.remove(key);
                    Streaming<E> jns = strm.include(Streaming.source(iter).setContext(copyContext()), keyer);
                    list.add(new KeyedData<>(key, jns));
                } else {
                    list.add(new KeyedData<>(key, strm));
                }
            }

            for (K key : dstMap.keySet()) {
                Iterator<E> iter = dstMap.get(key);
                list.add(new KeyedData<>(key, Streaming.source(iter).setContext(copyContext())));
            }

            if (srcNull != null && dstNull != null) {
                Streaming<E> jns = srcNull.include(Streaming.source(dstNull).setContext(copyContext()), keyer);
                list.add(new KeyedData<>(null, jns));
            } else if (srcNull != null) {
                list.add(new KeyedData<>(null, srcNull));
            } else if (dstNull != null) {
                list.add(new KeyedData<>(null, Streaming.source(dstNull).setContext(copyContext())));
            }

            return list.iterator();
        });
        return new AbsKeyedStreaming<K, E>(ret).setContext(copyContext());
    }

    @Override
    public KeyedStreaming<K, E> include(KeyedStreaming<K, E> streaming) {
        inject(streaming);
        return include(streaming, e -> e);
    }

    @Override
    public <R> KeyedStreaming<K, E> exclude(KeyedStreaming<K, E> streaming, Function<E, R> keyer) {
        inject(streaming);
        inject(keyer);
        Iterator<KeyedData<K, Streaming<E>>> ret = new LazyIterator<>(() -> {
            Map<K, Streaming<E>> srcMap = new HashMap<>();
            Streaming<E> srcNull = null;
            while (keyIterator.hasNext()) {
                KeyedData<K, Streaming<E>> curr = keyIterator.next();
                if (curr.key != null) {
                    srcMap.put(curr.key, curr.val);
                } else {
                    srcNull = curr.val;
                }
            }
            List<KeyedData<K, Iterator<E>>> dst = streaming.iterator().list();
            Map<K, Iterator<E>> dstMap = new HashMap<>();
            Iterator<E> dstNull = null;
            for (KeyedData<K, Iterator<E>> item : dst) {
                if (item.key != null) {
                    dstMap.put(item.key, item.val);
                } else {
                    dstNull = item.val;
                }
            }

            List<KeyedData<K, Streaming<E>>> list = new LinkedList<>();

            for (K key : srcMap.keySet()) {
                Streaming<E> strm = srcMap.get(key);
                if (dstMap.containsKey(key)) {
                    Iterator<E> iter = dstMap.get(key);
                    dstMap.remove(key);
                    Streaming<E> jns = strm.exclude(Streaming.source(iter).setContext(copyContext()), keyer);
                    list.add(new KeyedData<>(key, jns));
                } else {
                    list.add(new KeyedData<>(key, strm));
                }
            }

            for (K key : dstMap.keySet()) {
                Iterator<E> iter = dstMap.get(key);
                list.add(new KeyedData<>(key, Streaming.source(iter).setContext(copyContext())));
            }

            if (srcNull != null && dstNull != null) {
                Streaming<E> jns = srcNull.exclude(Streaming.source(dstNull).setContext(copyContext()), keyer);
                list.add(new KeyedData<>(null, jns));
            } else if (srcNull != null) {
                list.add(new KeyedData<>(null, srcNull));
            } else if (dstNull != null) {
                list.add(new KeyedData<>(null, Streaming.source(dstNull).setContext(copyContext())));
            }

            return list.iterator();
        });
        return new AbsKeyedStreaming<K, E>(ret).setContext(copyContext());
    }

    @Override
    public KeyedStreaming<K, E> exclude(KeyedStreaming<K, E> streaming) {
        inject(streaming);
        return exclude(streaming, e -> e);
    }

    @Override
    public Streaming<KeyedData<K, Iterator<E>>> iterator() {
        return sink(SinkWrappers.iterator());
    }

    @Override
    public Streaming<KeyedData<K, Stream<E>>> stream() {
        return sink(SinkWrappers.stream());
    }

    @Override
    public <C extends Collection<E>> Streaming<KeyedData<K, C>> collection(Supplier<C> supplier) {
        return sink(SinkWrappers.collection(supplier));
    }

    @Override
    public Streaming<KeyedData<K, List<E>>> list() {
        return collection(LinkedList::new);
    }

    @Override
    public Streaming<KeyedData<K, Set<E>>> set() {
        return collection(LinkedHashSet::new);
    }

    @Override
    public Streaming<KeyedData<K, E[]>> array(E[] arr) {
        return sink(SinkWrappers.array(arr));
    }

    @Override
    public <R, A> Streaming<KeyedData<K, R>> collect(Collector<E, A, R> collector) {
        return sink(SinkWrappers.collect(collector));
    }

    @Override
    public Streaming<KeyedData<K, String>> stringify(Function<E, Object> mapper, Object open, Object separator, Object close) {
        return sink(SinkWrappers.stringify(mapper, open, separator, close));
    }

    @Override
    public Streaming<KeyedData<K, Map<Streaming.Measure, Object>>> measures(Streaming.Measure... measures) {
        return sink(SinkWrappers.measures(measures));
    }
}
