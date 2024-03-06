package i2f.stream.impl;

import i2f.stream.Streaming;
import i2f.stream.richable.RichStreamProcessor;
import i2f.stream.thread.AtomicCountDownLatch;
import i2f.stream.thread.AtomicCountDownLatchRunnable;
import i2f.stream.thread.NamingForkJoinPool;
import i2f.stream.timed.TimedStreaming;
import i2f.stream.timed.TimedStreamingImpl;

import java.io.IOException;
import java.io.Writer;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2024/2/23 10:26
 * @desc
 */
public class StreamingImpl<E> implements Streaming<E> {

    protected Iterator<E> holdIterator;

    protected Map<String, Object> localContext;
    protected Map<String, Object> globalContext;

    protected boolean parallel;
    protected int parallelCount;
    protected ExecutorService pool;

    public static ExecutorService DEFAULT_POOL = NamingForkJoinPool.getPool(Runtime.getRuntime().availableProcessors(),
            "streaming", "default(" + Runtime.getRuntime().availableProcessors() + ")");

    public StreamingImpl(Iterator<E> iterator) {
        this.holdIterator = iterator;
        prepare();
    }

    public StreamingImpl(Iterator<E> iterator, Map<String, Object> globalContext) {
        this.holdIterator = iterator;
        this.globalContext = globalContext;
        prepare();
    }

    public StreamingImpl(Iterator<E> iterator, StreamingImpl<?> parent) {
        this.holdIterator = iterator;
        this.globalContext = globalContext;
        prepare();
        this.parallel = parent.parallel;
        this.parallelCount = parent.parallelCount;
        this.pool = parent.pool;
    }

    protected void prepare() {
        this.parallel = false;
        this.parallelCount = Runtime.getRuntime().availableProcessors();
        this.pool = DEFAULT_POOL;
        localContext = new HashMap<>();
        if (globalContext == null) {
            globalContext = new HashMap<>();
        }
    }

    protected boolean isInjectTarget(Object inject) {
        if (inject == null) {
            return false;
        }
        return inject instanceof RichStreamProcessor;
    }

    protected RichStreamProcessor tryConvertAsRich(Object inject) {
        if (!isInjectTarget(inject)) {
            return null;
        }
        return (RichStreamProcessor) inject;
    }

    protected void richInject(Object inject) {
        RichStreamProcessor processor = tryConvertAsRich(inject);
        if (processor == null) {
            return;
        }
        processor.setLocalContext(this.localContext);
        processor.setGlobalContext(this.globalContext);
        processor.setParallel(this.parallel);
        processor.setParallelCount(this.parallelCount);
        processor.setPool(this.pool);
    }

    protected void richBefore(Object inject) {
        richInject(inject);
        RichStreamProcessor processor = tryConvertAsRich(inject);
        if (processor == null) {
            return;
        }
        processor.onBefore();
    }

    protected void richAfter(Object inject) {
        richInject(inject);
        RichStreamProcessor processor = tryConvertAsRich(inject);
        if (processor == null) {
            return;
        }
        processor.onAfter();
    }

    @Override
    public Streaming<E> parallel() {
        this.parallel = true;
        return this;
    }

    @Override
    public Streaming<E> sequence() {
        this.parallel = false;
        return this;
    }

    @Override
    public Streaming<E> pool(ExecutorService pool) {
        this.pool = pool;
        if (this.pool == null) {
            this.pool = DEFAULT_POOL;
        }
        return this;
    }

    @Override
    public Streaming<E> defaultPool() {
        this.pool = DEFAULT_POOL;
        return this;
    }

    @Override
    public Streaming<E> pool(int size) {
        if (size <= 0) {
            size = 1;
        }
        this.pool = NamingForkJoinPool.getPool(size, "streaming",
                "pool(" + size + ")");
        return this;
    }

    @Override
    public Streaming<E> parallelism(int count) {
        this.parallelCount=count;
        return this;
    }

    public static <R, E> Iterator<R> delegateParallelism(int parallelCount,
                                                         Supplier<ExecutorService> poolSupplier,
                                                         Supplier<AtomicCountDownLatch> latchSupplier,
                                                         Iterator<E> holdIterator,
                                                         BiConsumer<E, Consumer<R>> consumer
    ){
        return new GeneratorIterator<>((collector)->{
            try {

                AtomicCountDownLatch latch = latchSupplier.get();
                ExecutorService pool = poolSupplier.get();
                latch.begin();
                while (holdIterator.hasNext()) {
                    E elem = holdIterator.next();
                    latch.count();

                    AtomicCountDownLatchRunnable task = new AtomicCountDownLatchRunnable(latch) {
                        @Override
                        public void doTask(AtomicCountDownLatch resource) throws Throwable {
                            consumer.accept(elem,(e)->collector.accept(Reference.of(e)));
                        }

                    };
                    pool.submit(task);

                    if(parallelCount>0 && latch.current()==parallelCount){
                        latch.finish();
                        latch.await();

                        latch.begin();
                    }
                }

                latch.finish();
                latch.await();

                collector.accept(Reference.finish());
            } catch (Exception e) {
                throw new IllegalStateException("parallel exception", e);
            }
        });
    }

    @Override
    public <R> Streaming<R> process(BiConsumer<E, Consumer<R>> mapper) {
        return new StreamingImpl<>(new LazyIterator<R>(() -> {
            richBefore(mapper);
            richBefore(this.holdIterator);
            try {
                if (this.parallel) {
                    return delegateParallelism(
                            this.parallelCount,
                            () -> this.pool,
                            AtomicCountDownLatch::new,
                            this.holdIterator,
                            (e,collector) -> {
                                mapper.accept(e, collector);
                            });
                } else {
                    return new SupplierBufferIterator<>(()->{
                        while (this.holdIterator.hasNext()) {
                            E elem = this.holdIterator.next();
                            List<R> result = new LinkedList<>();
                            mapper.accept(elem, result::add);
                            return Reference.of(result);
                        }
                        return Reference.finish();
                    });
                }
            } finally {
                richAfter(this.holdIterator);
                richAfter(mapper);
            }
        }), this);
    }

    @Override
    public Streaming<Map.Entry<E, Long>> indexed() {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                AtomicLong idx = new AtomicLong(0);
                return new SupplierIterator<>(() -> {
                    while (this.holdIterator.hasNext()) {
                        E elem = this.holdIterator.next();
                        return Reference.of(new SimpleEntry<>(elem, idx.getAndIncrement()));
                    }
                    richAfter(this.holdIterator);
                    return Reference.finish();
                });
            } finally {

            }
        }), this);
    }

    @Override
    public <R> Streaming<R> process(Function<Iterator<E>, Iterator<R>> process) {
        return new StreamingImpl<>(new LazyIterator<R>(() -> {
            richBefore(process);
            richBefore(this.holdIterator);
            try {
                return process.apply(this.holdIterator);
            } finally {
                richAfter(this.holdIterator);
                richAfter(process);
            }
        }), this);
    }

    @Override
    public Streaming<E> filter(Predicate<E> filter) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(filter);
            richBefore(this.holdIterator);
            try {
                if (parallel) {
                    Iterator<E> iterator = delegateParallelism(this.parallelCount,
                            () -> this.pool,
                            AtomicCountDownLatch::new,
                            this.holdIterator,
                            (e,collector) -> {
                                if (filter.test(e)) {
                                    collector.accept(e);
                                }
                            });
                    richAfter(this.holdIterator);
                    richAfter(filter);
                    return iterator;
                } else {
                    return new SupplierIterator<>(() -> {
                        while (this.holdIterator.hasNext()) {
                            E elem = this.holdIterator.next();
                            if (filter.test(elem)) {
                                return Reference.of(elem);
                            }
                        }
                        richAfter(this.holdIterator);
                        richAfter(filter);
                        return Reference.finish();
                    });
                }
            } finally {

            }
        }), this);
    }

    @Override
    public Streaming<E> afterAll(Predicate<E> filter) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(filter);
            richBefore(this.holdIterator);
            try {
                AtomicBoolean hasFind = new AtomicBoolean(false);
                return new SupplierIterator<>(() -> {
                    while (this.holdIterator.hasNext()) {
                        E elem = this.holdIterator.next();
                        if (!hasFind.get()) {
                            if (filter.test(elem)) {
                                hasFind.set(true);
                            }
                        }
                        if (hasFind.get()) {
                            return Reference.of(elem);
                        } else {
                            return Reference.nop();
                        }
                    }
                    richAfter(this.holdIterator);
                    richAfter(filter);
                    return Reference.finish();
                });

            } finally {

            }
        }), this);
    }

    @Override
    public Streaming<E> beforeAll(Predicate<E> filter) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(filter);
            richBefore(this.holdIterator);
            try {
                AtomicBoolean hasFind = new AtomicBoolean(false);
                return new SupplierIterator<>(() -> {
                    while (this.holdIterator.hasNext()) {
                        E elem = this.holdIterator.next();
                        if (!hasFind.get()) {
                            if (filter.test(elem)) {
                                hasFind.set(true);
                            }
                        }
                        if (!hasFind.get()) {
                            return Reference.of(elem);
                        } else {
                            break;
                        }
                    }
                    richAfter(this.holdIterator);
                    richAfter(filter);
                    return Reference.finish();
                });

            } finally {

            }
        }), this);
    }

    @Override
    public Streaming<E> rangeAll(Predicate<E> beginFilter, Predicate<E> endFilter, boolean includeBegin, boolean includeEnd) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(beginFilter);
            richBefore(endFilter);
            richBefore(this.holdIterator);
            try {
                AtomicBoolean hasFind = new AtomicBoolean(false);
                return new SupplierIterator<>(() -> {
                    while (this.holdIterator.hasNext()) {
                        E elem = this.holdIterator.next();
                        boolean isBegin = false;
                        if (!hasFind.get()) {
                            if (beginFilter.test(elem)) {
                                hasFind.set(true);
                                isBegin = true;
                            }
                        }
                        if (!hasFind.get()) {
                            return Reference.nop();
                        }
                        boolean isEnd = endFilter.test(elem);
                        if (!isEnd) {
                            if (isBegin) {
                                return includeBegin ? Reference.of(elem) : Reference.nop();
                            } else {
                                return Reference.of(elem);
                            }
                        } else {
                            if (includeEnd) {
                                return Reference.of(elem);
                            } else {
                                break;
                            }
                        }
                    }
                    richAfter(this.holdIterator);
                    richAfter(beginFilter);
                    richAfter(endFilter);
                    return Reference.finish();
                });

            } finally {

            }
        }), this);
    }

    @Override
    public <R> Streaming<R> map(Function<E, R> mapper) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(mapper);
            richBefore(this.holdIterator);
            try {
                if (parallel) {
                    Iterator<R> iterator = delegateParallelism(this.parallelCount,
                            () -> this.pool,
                            AtomicCountDownLatch::new,
                            this.holdIterator,
                            (e,collector) -> {
                                R ret = mapper.apply(e);
                                collector.accept(ret);
                            });
                    richAfter(this.holdIterator);
                    richAfter(mapper);
                    return iterator;
                } else {
                    return new SupplierIterator<>(() -> {
                        while (this.holdIterator.hasNext()) {
                            E elem = this.holdIterator.next();
                            R ret = mapper.apply(elem);
                            return Reference.of(ret);
                        }
                        richAfter(this.holdIterator);
                        richAfter(mapper);
                        return Reference.finish();
                    });
                }
            } finally {

            }
        }), this);
    }

    @Override
    public <R> Streaming<R> flatMap(BiConsumer<E, Consumer<R>> collector) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(collector);
            richBefore(this.holdIterator);
            try {
                if (parallel) {
                    return delegateParallelism(this.parallelCount,
                            () -> this.pool,
                            AtomicCountDownLatch::new,
                            this.holdIterator,
                            (elem,collect) -> {
                                collector.accept(elem, collect::accept);
                            });
                } else {
                    return new SupplierBufferIterator<>(() -> {
                        while (this.holdIterator.hasNext()) {
                            E elem = this.holdIterator.next();
                            List<R> buffer = new LinkedList<>();
                            collector.accept(elem, (e) -> {
                                buffer.add(e);
                            });
                            return Reference.of(buffer);
                        }
                        richAfter(this.holdIterator);
                        richAfter(collector);
                        return Reference.finish();
                    });
                }
            } finally {

            }
        }), this);
    }

    @Override
    public Streaming<E> recursive(BiConsumer<E, Consumer<E>> collector) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(collector);
            richBefore(this.holdIterator);
            try {
                return new SupplierBufferIterator<>(() -> {
                    while (this.holdIterator.hasNext()) {
                        E elem = this.holdIterator.next();
                        LinkedList<E> buffer = innerRecursiveMap(elem, collector);
                        buffer.addFirst(elem);
                        return Reference.of(buffer);
                    }
                    richAfter(this.holdIterator);
                    richAfter(collector);
                    return Reference.finish();
                });
            } finally {

            }
        }), this);
    }

    @Override
    public <R> Streaming<R> recursiveMap(BiConsumer<E, Consumer<R>> initCollector, BiConsumer<R, Consumer<R>> recursiveCollector) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(initCollector);
            richBefore(recursiveCollector);
            richBefore(this.holdIterator);
            try {
                return new SupplierBufferIterator<>(() -> {
                    while (this.holdIterator.hasNext()) {
                        E elem = this.holdIterator.next();
                        LinkedList<R> ret = new LinkedList<>();

                        LinkedList<R> once = new LinkedList<>();
                        initCollector.accept(elem, once::add);
                        for (R item : once) {
                            LinkedList<R> buffer = innerRecursiveMap(item, recursiveCollector);
                            ret.add(item);
                            ret.addAll(buffer);
                        }

                        return Reference.of(ret);
                    }
                    richAfter(this.holdIterator);
                    richAfter(initCollector);
                    richAfter(recursiveCollector);
                    return Reference.finish();
                });
            } finally {

            }
        }), this);
    }

    public static <E> LinkedList<E> innerRecursiveMap(E elem, BiConsumer<E, Consumer<E>> collector) {
        LinkedList<E> ret = new LinkedList<>();
        LinkedList<E> curr = new LinkedList<>();
        collector.accept(elem, curr::add);
        ret.addAll(curr);
        for (E item : curr) {
            List<E> next = innerRecursiveMap(item, collector);
            ret.addAll(next);
        }
        return ret;
    }

    @Override
    public Streaming<E> skip(long count) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                AtomicLong cnt = new AtomicLong(0);
                return new SupplierIterator<>(() -> {
                    while (this.holdIterator.hasNext() && (cnt.get() < count || count < 0)) {
                        cnt.getAndIncrement();
                    }
                    while (this.holdIterator.hasNext()) {
                        E elem = this.holdIterator.next();
                        return Reference.of(elem);
                    }
                    richAfter(this.holdIterator);
                    return Reference.finish();
                });
            } finally {

            }
        }), this);
    }

    @Override
    public Streaming<E> limit(long count) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                AtomicLong cnt = new AtomicLong(0);
                return new SupplierIterator<>(() -> {
                    while (this.holdIterator.hasNext() && (cnt.get() < count || count < 0)) {
                        E elem = this.holdIterator.next();
                        return Reference.of(elem);
                    }
                    richAfter(this.holdIterator);
                    return Reference.finish();
                });
            } finally {

            }
        }), this);
    }

    @Override
    public Streaming<E> tail(int count) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                LinkedList<E> ret=new LinkedList<>();
                AtomicInteger cnt = new AtomicInteger(0);
                while(this.holdIterator.hasNext()){
                    E elem = this.holdIterator.next();
                    ret.add(elem);
                    cnt.incrementAndGet();
                    if(cnt.get()>count){
                        ret.removeFirst();
                        cnt.decrementAndGet();
                    }
                }
                return ret.iterator();
            } finally {
                richAfter(this.holdIterator);
            }
        }), this);
    }

    @Override
    public Streaming<E> sort(boolean asc) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                LinkedList<E> list = new LinkedList<>();
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    list.add(elem);
                }
                E elem = null;
                for (E item : list) {
                    if (item != null) {
                        elem = item;
                        break;
                    }
                }
                Comparator<E> comparator = new Comparator<E>() {
                    @Override
                    public int compare(E o1, E o2) {
                        return 0;
                    }
                };
                if (elem instanceof Comparable) {
                    comparator = new Comparator<E>() {
                        @Override
                        public int compare(E o1, E o2) {
                            if (o1 == o2) {
                                return 0;
                            }
                            if (o1 != null) {
                                return ((Comparable) o1).compareTo(o2);
                            }
                            return 0 - (((Comparable) o2).compareTo(o1));
                        }
                    };
                }
                if(!asc){
                    comparator=comparator.reversed();
                }
                list.sort(comparator);
                return list.iterator();
            } finally {
                richAfter(this.holdIterator);
            }
        }), this);
    }

    @Override
    public Streaming<E> sort(Comparator<E> comparator) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(comparator);
            richBefore(this.holdIterator);
            try {
                LinkedList<E> list = new LinkedList<>();
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    list.add(elem);
                }
                list.sort(comparator);
                return list.iterator();
            } finally {
                richAfter(this.holdIterator);
                richAfter(comparator);
            }
        }), this);
    }

    @Override
    public Streaming<E> reverse() {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                LinkedList<E> list = new LinkedList<>();
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    list.add(elem);
                }
                LinkedList<E> ret = new LinkedList<>();
                for (E item : list) {
                    ret.addFirst(item);
                }
                return ret.iterator();
            } finally {
                richAfter(this.holdIterator);
            }
        }), this);
    }

    @Override
    public Streaming<E> shuffle() {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                LinkedList<E> list = new LinkedList<>();
                int cnt = 0;
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    list.add(elem);
                    cnt++;
                }
                for (int i = cnt - 1; i > 0; i--) {
                    E elem = list.remove(i);
                    int idx = (int) (Math.random() * cnt);
                    list.add(idx, elem);
                }
                return list.iterator();
            } finally {
                richAfter(this.holdIterator);
            }
        }), this);
    }

    @Override
    public Streaming<E> distinct() {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                LinkedHashSet<E> list = new LinkedHashSet<>();
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    list.add(elem);
                }
                return list.iterator();
            } finally {
                richAfter(this.holdIterator);
            }
        }), this);
    }

    @Override
    public Streaming<E> sample(double rate) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                return new SupplierIterator<>(() -> {
                    while (this.holdIterator.hasNext()) {
                        E elem = this.holdIterator.next();
                        if (Math.random() <= rate) {
                            return Reference.of(elem);
                        }

                    }
                    richAfter(this.holdIterator);
                    return Reference.finish();
                });
            } finally {
            }
        }), this);
    }

    @Override
    public Streaming<E> sampleCount(int count) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                LinkedList<E> list = new LinkedList<>();
                int cnt = 0;
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    list.add(elem);
                    cnt++;
                }
                LinkedList<E> ret = new LinkedList<>();
                for (int i = 0; i < count && cnt > 0; i++) {
                    int idx = (int) (Math.random() * cnt);
                    E elem = list.remove(idx);
                    ret.add(elem);
                    cnt--;
                }
                return ret.iterator();
            } finally {
                richAfter(this.holdIterator);
            }
        }), this);
    }

    @Override
    public Streaming<E> peek(Consumer<E> consumer) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(consumer);
            try {
                LazyIterator<E> iterator = new LazyIterator<E>(this.holdIterator) {
                    @Override
                    public E next() {
                        E elem = super.next();
                        consumer.accept(elem);
                        return elem;
                    }
                };

                return iterator;
            } finally {
                richAfter(consumer);
            }
        }), this);
    }

    @Override
    public Streaming<E> print(String prefix) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            try {
                LazyIterator<E> iterator = new LazyIterator<E>(this.holdIterator) {
                    @Override
                    public E next() {
                        E elem = super.next();
                        if(prefix!=null){
                            System.out.println(prefix+elem);
                        }else{
                            System.out.println(elem);
                        }
                        return elem;
                    }
                };

                return iterator;
            } finally {
            }
        }), this);
    }


    @Override
    public Streaming<E> merge(Streaming<E> streaming) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            try {
                return new MergeIterator<>(this.holdIterator, streaming.iterator());
            } finally {

            }
        }), this);
    }

    @Override
    public <T> Streaming<E> merge(Streaming<T> streaming, Function<T, E> mapper) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(mapper);
            try {
                Iterator<T> mergeIterator = streaming.iterator();
                return new MergeIterator<>(this.holdIterator, new MapperIterator<>(mergeIterator, mapper));
            } finally {
                richAfter(mapper);
            }
        }), this);
    }

    @Override
    public Streaming<E> mixed(Streaming<E> streaming) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            try {
                return new MixedIterator<E>(this.holdIterator, streaming.iterator());
            } finally {

            }
        }), this);
    }

    @Override
    public Streaming<E> include(Streaming<E> streaming) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            LinkedHashSet<E> set = streaming.toCollection(new LinkedHashSet<>());
            try {
                return new SupplierIterator<>(() -> {
                    while (this.holdIterator.hasNext()) {
                        E elem = this.holdIterator.next();
                        if (set.contains(elem)) {
                            return Reference.of(elem);
                        }
                    }
                    return Reference.finish();
                });
            } finally {

            }
        }), this);
    }

    @Override
    public Streaming<E> exclude(Streaming<E> streaming) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            LinkedHashSet<E> set = streaming.toCollection(new LinkedHashSet<>());
            try {
                return new SupplierIterator<>(() -> {
                    while (this.holdIterator.hasNext()) {
                        E elem = this.holdIterator.next();
                        if (!set.contains(elem)) {
                            return Reference.of(elem);
                        }
                    }
                    return Reference.finish();
                });
            } finally {

            }
        }), this);
    }

    @Override
    public <K> Streaming<Map.Entry<K, Streaming<E>>> keyBy(Function<E, K> keySupplier) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(keySupplier);
            richBefore(this.holdIterator);
            try {
                LinkedList<E> nullList = new LinkedList<>();
                Map<K, LinkedList<E>> map = new LinkedHashMap<>();
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    K key = keySupplier.apply(elem);
                    if (key == null) {
                        nullList.add(elem);
                    } else {
                        if (!map.containsKey(key)) {
                            map.put(key, new LinkedList<>());
                        }
                        map.get(key).add(elem);
                    }
                }

                List<Map.Entry<K, Streaming<E>>> ret = new LinkedList<>();
                for (Map.Entry<K, LinkedList<E>> entry : map.entrySet()) {
                    ret.add(new SimpleEntry<>(entry.getKey(), new StreamingImpl<>(entry.getValue().iterator(), this.globalContext)));
                }
                ret.add(new SimpleEntry<>(null, new StreamingImpl<>(nullList.iterator(), this.globalContext)));

                return ret.iterator();
            } finally {
                richAfter(this.holdIterator);
                richAfter(keySupplier);
            }
        }), this);
    }

    @Override
    public <K, R> Streaming<Map.Entry<K, R>> keyBy(Function<E, K> keySupplier, Function<Streaming<E>, R> finishMapper) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(keySupplier);
            richBefore(finishMapper);
            richBefore(this.holdIterator);
            try {
                LinkedList<E> nullList = new LinkedList<>();
                Map<K, LinkedList<E>> map = new LinkedHashMap<>();
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    K key = keySupplier.apply(elem);
                    if (key == null) {
                        nullList.add(elem);
                    } else {
                        if (!map.containsKey(key)) {
                            map.put(key, new LinkedList<>());
                        }
                        map.get(key).add(elem);
                    }
                }

                List<Map.Entry<K, R>> ret = new LinkedList<>();
                for (Map.Entry<K, LinkedList<E>> entry : map.entrySet()) {
                    StreamingImpl<E> streaming = new StreamingImpl<>(entry.getValue().iterator(), this.globalContext);
                    ret.add(new SimpleEntry<>(entry.getKey(), finishMapper.apply(streaming)));
                }
                StreamingImpl<E> streaming = new StreamingImpl<>(nullList.iterator(), this.globalContext);

                ret.add(new SimpleEntry<>(null, finishMapper.apply(streaming)));

                return ret.iterator();
            } finally {
                richAfter(this.holdIterator);
                richAfter(keySupplier);
                richAfter(finishMapper);
            }
        }), this);
    }

    @Override
    public <K, V extends Collection<E>> Streaming<Map.Entry<K, V>> keyBy(Supplier<V> containerSupplier, Function<E, K> keySupplier) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(keySupplier);
            richBefore(this.holdIterator);
            try {
                V nullList = containerSupplier.get();
                Map<K, V> map = new LinkedHashMap<>();
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    K key = keySupplier.apply(elem);
                    if (key == null) {
                        nullList.add(elem);
                    } else {
                        if (!map.containsKey(key)) {
                            map.put(key, containerSupplier.get());
                        }
                        map.get(key).add(elem);
                    }
                }

                List<Map.Entry<K, V>> ret = new LinkedList<>();
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    ret.add(new SimpleEntry<>(entry.getKey(), entry.getValue()));
                }
                ret.add(new SimpleEntry<>(null, nullList));

                return ret.iterator();
            } finally {
                richAfter(this.holdIterator);
                richAfter(keySupplier);
            }
        }), this);
    }

    @Override
    public Streaming<Map.Entry<E, Long>> countBy() {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(this.holdIterator);
            try {
                long nullCount = 0;
                Map<E, Long> map = new LinkedHashMap<>();
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    if (elem == null) {
                        nullCount++;
                    } else {
                        if (!map.containsKey(elem)) {
                            map.put(elem, 1L);
                        } else {
                            map.put(elem, map.get(elem) + 1);
                        }
                    }
                }

                List<Map.Entry<E, Long>> ret = new LinkedList<>();
                for (Map.Entry<E, Long> entry : map.entrySet()) {
                    ret.add(new SimpleEntry<>(entry.getKey(), entry.getValue()));
                }
                ret.add(new SimpleEntry<>(null, nullCount));

                return ret.iterator();
            } finally {
                richAfter(this.holdIterator);
            }
        }), this);
    }

    @Override
    public <K> Streaming<Map.Entry<K, Long>> countBy(Function<E, K> keySupplier) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(keySupplier);
            richBefore(this.holdIterator);
            try {
                long nullCount = 0;
                Map<K, Long> map = new LinkedHashMap<>();
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    K key = keySupplier.apply(elem);
                    if (key == null) {
                        nullCount++;
                    } else {
                        if (!map.containsKey(key)) {
                            map.put(key, 1L);
                        } else {
                            map.put(key, map.get(key) + 1);
                        }
                    }
                }

                List<Map.Entry<K, Long>> ret = new LinkedList<>();
                for (Map.Entry<K, Long> entry : map.entrySet()) {
                    ret.add(new SimpleEntry<>(entry.getKey(), entry.getValue()));
                }
                ret.add(new SimpleEntry<>(null, nullCount));

                return ret.iterator();
            } finally {
                richAfter(keySupplier);
                richAfter(this.holdIterator);
            }
        }), this);
    }

    @Override
    public Streaming<Map.Entry<List<E>, Map.Entry<Integer, Integer>>> viewWindow(int beforeCount, int afterCount) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {

            LinkedList<SimpleEntry<LinkedList<E>, SimpleEntry<Integer, Integer>>> waitList = new LinkedList<>();

            LinkedList<E> beforeList = new LinkedList<>();
            AtomicInteger beforeSize = new AtomicInteger(0);

            return new SupplierBufferIterator<Map.Entry<List<E>, Map.Entry<Integer, Integer>>>(() -> {
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();

                    LinkedList<E> newList = new LinkedList<>();
                    newList.addAll(beforeList);

                    newList.add(elem);

                    for (SimpleEntry<LinkedList<E>, SimpleEntry<Integer, Integer>> entry : waitList) {
                        if (entry.getValue().getValue() < afterCount) {
                            entry.getKey().add(elem);
                            entry.getValue().setValue(entry.getValue().getValue() + 1);
                        }
                    }

                    waitList.add(new SimpleEntry<>(newList, new SimpleEntry<>(beforeSize.get(), 0)));

                    Collection<Map.Entry<List<E>, Map.Entry<Integer, Integer>>> buff=new ArrayList<>();
                    while (!waitList.isEmpty()) {
                        SimpleEntry<LinkedList<E>, SimpleEntry<Integer, Integer>> first = waitList.getFirst();
                        if (first.getValue().getValue() >= afterCount) {
                            buff.add(new SimpleEntry<>(first.getKey(), first.getValue()));
                            waitList.removeFirst();
                        }else{
                            break;
                        }
                    }

                    beforeList.add(elem);
                    beforeSize.incrementAndGet();
                    if (beforeSize.get() > beforeCount) {
                        beforeList.removeFirst();
                        beforeSize.decrementAndGet();
                    }

                    if(!buff.isEmpty()){
                        return Reference.of(buff);
                    }else{
                        return Reference.nop();
                    }
                }

                Collection<Map.Entry<List<E>, Map.Entry<Integer, Integer>>> buff=new ArrayList<>();
                while (!waitList.isEmpty()) {
                    SimpleEntry<LinkedList<E>, SimpleEntry<Integer, Integer>> first = waitList.getFirst();
                    buff.add(new SimpleEntry<>(first.getKey(), first.getValue()));
                    waitList.removeFirst();
                }

                if(!buff.isEmpty()){
                    return Reference.of(buff);
                }

                richAfter(this.holdIterator);
                return Reference.finish();
            });

        }), this);
    }


    @Override
    public Streaming<Map.Entry<List<E>,Map.Entry<Long,Long>>> slideWindow(int windowSize, int slideCount) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {

            LinkedList<SimpleEntry<LinkedList<E>, SimpleEntry<Integer,SimpleEntry<Long,Long>>>> waitList = new LinkedList<>();
            AtomicInteger idx=new AtomicInteger(0);
            AtomicLong elemCount=new AtomicLong(0L);
            AtomicLong windowCount=new AtomicLong(0L);

            return new SupplierBufferIterator<Map.Entry<List<E>,Map.Entry<Long,Long>>>(() -> {
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    elemCount.incrementAndGet();

                    for (SimpleEntry<LinkedList<E>, SimpleEntry<Integer,SimpleEntry<Long,Long>>> entry : waitList) {
                        entry.getKey().add(elem);
                        entry.getValue().setKey(entry.getValue().getKey()+1);
                    }

                    if(idx.get()==0){
                        LinkedList<E> newList = new LinkedList<>();
                        newList.add(elem);
                        windowCount.incrementAndGet();
                        waitList.add(new SimpleEntry<>(newList,new SimpleEntry<>(1,new SimpleEntry<>(elemCount.get(),windowCount.get()))));
                    }

                    Collection<Map.Entry<List<E>,Map.Entry<Long,Long>>> buff=new LinkedList<>();
                    while(!waitList.isEmpty()){
                        SimpleEntry<LinkedList<E>, SimpleEntry<Integer, SimpleEntry<Long, Long>>> first = waitList.getFirst();
                        if(first.getValue().getKey()>=windowSize){
                            buff.add(new SimpleEntry<>(first.getKey(),first.getValue().getValue()));
                            waitList.removeFirst();
                        }else{
                            break;
                        }
                    }


                    idx.updateAndGet((v)->(v+1)%slideCount);


                    if(!buff.isEmpty()){
                        return Reference.of(buff);
                    }else{
                        return Reference.nop();
                    }
                }

                Collection<Map.Entry<List<E>,Map.Entry<Long,Long>>> buff=new LinkedList<>();
                while (!waitList.isEmpty()) {
                    SimpleEntry<LinkedList<E>, SimpleEntry<Integer, SimpleEntry<Long, Long>>> first = waitList.getFirst();
                    buff.add(new SimpleEntry<>(first.getKey(),first.getValue().getValue()));
                    waitList.removeFirst();
                }

                if(!buff.isEmpty()){
                    return Reference.of(buff);
                }

                richAfter(this.holdIterator);
                return Reference.finish();
            });

        }), this);
    }


    @Override
    public <R> Streaming<Map.Entry<List<E>, R>> conditionWindow(Supplier<R> initConditionSupplier, Function<E, R> currentConditionMapper, BiPredicate<R, R> conditionChangePredicater) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {

            R initCond = initConditionSupplier.get();
            SimpleEntry<List<E>, R> current=new SimpleEntry<>(new LinkedList<>(),initCond);

            return new SupplierIterator<>(() -> {
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    R currCond = currentConditionMapper.apply(elem);
                    Reference<Map.Entry<List<E>,R>> ret=Reference.nop();
                    if(!conditionChangePredicater.test(current.getValue(),currCond)){
                        if(!current.getKey().isEmpty()){
                            ret=Reference.of(new SimpleEntry<>(current.getKey(),current.getValue()));
                        }
                        current.setKey(new LinkedList<>());
                        current.setValue(currCond);
                    }
                    current.getKey().add(elem);
                    return ret;
                }


                if(!current.getKey().isEmpty()){
                    Reference<Map.Entry<List<E>, R>> ret = Reference.of(new SimpleEntry<>(current.getKey(), current.getValue()));
                    current.setKey(new LinkedList<>());
                    return ret;
                }

                richAfter(this.holdIterator);
                return Reference.finish();
            });

        }), this);
    }

    @Override
    public <T> Streaming<Map.Entry<E, T>> connect(Streaming<T> other) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            return new ConnectIterator<>(this.holdIterator, other.iterator());
        }), this);
    }

    @Override
    public <T> Streaming<Map.Entry<E, T>> join(Streaming<T> other, BiPredicate<E, T> conditional) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(conditional);
            richBefore(this.holdIterator);
            try {
                LinkedList<Map.Entry<E, T>> ret = new LinkedList<>();
                LinkedList<T> rightList = other.toCollection(new LinkedList<>());
                while (this.holdIterator.hasNext()) {
                    E left = this.holdIterator.next();
                    for (T right : rightList) {
                        if (conditional.test(left, right)) {
                            ret.add(new SimpleEntry<>(left, right));
                        }
                    }
                }
                return ret.iterator();
            } finally {
                richAfter(this.holdIterator);
                richAfter(conditional);
            }
        }), this);
    }

    @Override
    public <K, T> Streaming<Map.Entry<E, T>> join(Streaming<T> other, Function<E, K> leftKeySupplier, Function<T, K> rightKeySupplier) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            richBefore(leftKeySupplier);
            richBefore(rightKeySupplier);
            richBefore(this.holdIterator);
            try {
                LinkedList<Map.Entry<E, T>> ret = new LinkedList<>();
                Map.Entry<LinkedList<T>, HashMap<K, LinkedList<T>>> group = other.toGroup(new HashMap<>(), LinkedList::new, rightKeySupplier);
                while (this.holdIterator.hasNext()) {
                    E left = this.holdIterator.next();
                    K key = leftKeySupplier.apply(left);
                    LinkedList<T> rightList = null;
                    if (key == null) {
                        rightList = group.getKey();
                    } else {
                        rightList = group.getValue().get(key);
                    }
                    if (rightList == null) {
                        continue;
                    }
                    for (T item : rightList) {
                        ret.add(new SimpleEntry<>(left, item));
                    }
                }
                return ret.iterator();
            } finally {
                richAfter(this.holdIterator);
                richAfter(leftKeySupplier);
                richAfter(rightKeySupplier);
            }
        }), this);
    }

    @Override
    public <R> R collect(Function<Iterator<E>, R> mapper) {
        richBefore(mapper);
        richBefore(this.holdIterator);
        try {
            return mapper.apply(this.holdIterator);
        } finally {
            richAfter(this.holdIterator);
            richAfter(mapper);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ResourcesIterator<>(this.holdIterator,
                (iterator,holder)->{
                    richBefore(iterator);
                    return iterator;
                },
                (iterator,holder)->{
                    richAfter(iterator);
                });
    }

    @Override
    public <R extends Collection<E>> R toCollection(R collection) {
        richBefore(this.holdIterator);
        try {
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                collection.add(elem);
            }
            return collection;
        } finally {
            richAfter(this.holdIterator);
        }
    }

    @Override
    public <K, V, R extends Map<K, V>> R toMap(R map, Function<E, K> keySupplier, Function<E, V> valueSupplier, BiFunction<V, V, V> valueSelector) {
        richBefore(keySupplier);
        richBefore(valueSupplier);
        richBefore(valueSelector);
        richBefore(this.holdIterator);
        try {
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                K key = keySupplier.apply(elem);
                V value = valueSupplier.apply(elem);
                if (map.containsKey(key)) {
                    V old = map.get(key);
                    value = valueSelector.apply(old, value);
                }
                map.put(key, value);
            }
            return map;
        } finally {
            richAfter(this.holdIterator);
            richAfter(keySupplier);
            richAfter(valueSupplier);
            richAfter(valueSelector);
        }
    }

    @Override
    public <K, V extends Collection<E>, R extends Map<K, V>> Map.Entry<V, R> toGroup(R map, Supplier<V> containerSupplier, Function<E, K> keySupplier) {
        richBefore(keySupplier);
        richBefore(this.holdIterator);
        try {
            V nullList = containerSupplier.get();
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                K key = keySupplier.apply(elem);
                if (key == null) {
                    nullList.add(elem);
                    continue;
                }
                if (!map.containsKey(key)) {
                    map.put(key, containerSupplier.get());
                }
                map.get(key).add(elem);
            }
            return new SimpleEntry<>(nullList, map);
        } finally {
            richAfter(this.holdIterator);
            richAfter(keySupplier);
        }
    }

    @Override
    public Reference<E> first() {
        richBefore(this.holdIterator);
        try {
            if (this.holdIterator.hasNext()) {
                return Reference.of(this.holdIterator.next());
            }
            return Reference.nop();
        } finally {
            richAfter(this.holdIterator);
        }
    }

    @Override
    public Reference<E> last() {
        richBefore(this.holdIterator);
        try {
            E elem = null;
            boolean isNop = true;
            while (this.holdIterator.hasNext()) {
                elem = this.holdIterator.next();
                isNop = false;
            }
            return isNop ? Reference.nop() : Reference.of(elem);
        } finally {
            richAfter(this.holdIterator);
        }
    }

    @Override
    public Reference<E> first(Predicate<E> filter) {
        richBefore(filter);
        richBefore(this.holdIterator);
        try {
            E ret = null;
            boolean isNop = true;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (filter.test(elem)) {
                    ret = elem;
                    isNop = false;
                    break;
                }
            }
            return isNop ? Reference.nop() : Reference.of(ret);
        } finally {
            richAfter(this.holdIterator);
            richAfter(filter);
        }
    }

    @Override
    public Reference<E> last(Predicate<E> filter) {
        richBefore(filter);
        richBefore(this.holdIterator);
        try {
            E ret = null;
            boolean isNop = true;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (filter.test(elem)) {
                    ret = elem;
                    isNop = false;
                }
            }
            return isNop ? Reference.nop() : Reference.of(ret);
        } finally {
            richAfter(this.holdIterator);
            richAfter(filter);
        }
    }

    @Override
    public boolean anyMatch(Predicate<E> filter) {
        richBefore(filter);
        richBefore(this.holdIterator);
        try {
            boolean ret = false;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (filter.test(elem)) {
                    ret = true;
                    break;
                }
            }
            return ret;
        } finally {
            richAfter(this.holdIterator);
            richAfter(filter);
        }
    }

    @Override
    public boolean allMatch(Predicate<E> filter) {
        richBefore(filter);
        richBefore(this.holdIterator);
        try {
            boolean ret = true;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (!filter.test(elem)) {
                    ret = false;
                    break;
                }
            }
            return ret;
        } finally {
            richAfter(this.holdIterator);
            richAfter(filter);
        }
    }

    @Override
    public Reference<E> min(Comparator<E> comparator) {
        richBefore(comparator);
        richBefore(this.holdIterator);
        try {
            E ret = null;
            boolean isNop = true;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (isNop) {
                    ret = elem;
                } else {
                    if (comparator.compare(ret, elem) > 0) {
                        ret = elem;
                    }
                }
                isNop = false;
            }
            return isNop ? Reference.nop() : Reference.of(ret);
        } finally {
            richAfter(this.holdIterator);
            richAfter(comparator);
        }
    }

    @Override
    public Reference<E> max(Comparator<E> comparator) {
        richBefore(comparator);
        richBefore(this.holdIterator);
        try {
            E ret = null;
            boolean isNop = true;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (isNop) {
                    ret = elem;
                } else {
                    if (comparator.compare(ret, elem) < 0) {
                        ret = elem;
                    }
                }
                isNop = false;
            }
            return isNop ? Reference.nop() : Reference.of(ret);
        } finally {
            richAfter(this.holdIterator);
            richAfter(comparator);
        }
    }

    @Override
    public Reference<E> most() {
        richBefore(this.holdIterator);
        try {
            E ret = null;
            boolean isNop = true;
            long nullCount = 0;
            Map<E, Long> countMap = new HashMap<>();
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (elem == null) {
                    nullCount++;
                } else {
                    if (!countMap.containsKey(elem)) {
                        countMap.put(elem, 1L);
                    } else {
                        countMap.put(elem, countMap.get(elem) + 1);
                    }
                }
                isNop = false;
            }
            if (isNop) {
                return Reference.nop();
            }
            long maxCount = nullCount;
            for (Map.Entry<E, Long> entry : countMap.entrySet()) {
                if (entry.getValue() > maxCount) {
                    ret = entry.getKey();
                    maxCount = entry.getValue();
                }
            }
            return Reference.of(ret);
        } finally {
            richAfter(this.holdIterator);
        }
    }

    @Override
    public <K> Reference<Map.Entry<K, E>> most(Function<E, K> keySupplier) {
        richBefore(keySupplier);
        richBefore(this.holdIterator);
        try {
            Map.Entry<K, E> ret = null;
            E nullElem = null;
            boolean isNop = true;
            long nullCount = 0;
            Map<K, SimpleEntry<E, Long>> countMap = new HashMap<>();
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                K key = keySupplier.apply(elem);
                if (key == null) {
                    nullCount++;
                    if (nullElem == null) {
                        nullElem = elem;
                    }
                } else {
                    if (!countMap.containsKey(key)) {
                        countMap.put(key, new SimpleEntry<>(elem, 1L));
                    } else {
                        SimpleEntry<E, Long> entry = countMap.get(key);
                        entry.setValue(entry.getValue() + 1);
                        countMap.put(key, entry);
                    }
                }
                isNop = false;
            }
            if (isNop) {
                return Reference.nop();
            }
            long maxCount = nullCount;
            ret = new SimpleEntry<>(null, nullElem);
            for (Map.Entry<K, SimpleEntry<E, Long>> entry : countMap.entrySet()) {
                if (entry.getValue().getValue() > maxCount) {
                    ret = new SimpleEntry<>(entry.getKey(), entry.getValue().getKey());
                    maxCount = entry.getValue().getValue();
                }
            }
            return Reference.of(ret);
        } finally {
            richAfter(this.holdIterator);
            richAfter(keySupplier);
        }
    }


    @Override
    public Reference<E> least() {
        richBefore(this.holdIterator);
        try {
            E ret = null;
            boolean isNop = true;
            long nullCount = 0;
            Map<E, Long> countMap = new HashMap<>();
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (elem == null) {
                    nullCount++;
                } else {
                    if (!countMap.containsKey(elem)) {
                        countMap.put(elem, 1L);
                    } else {
                        countMap.put(elem, countMap.get(elem) + 1);
                    }
                }
                isNop = false;
            }
            if (isNop) {
                return Reference.nop();
            }
            long minCount = nullCount;
            for (Map.Entry<E, Long> entry : countMap.entrySet()) {
                if (entry.getValue() < minCount) {
                    ret = entry.getKey();
                    minCount = entry.getValue();
                }
            }
            return Reference.of(ret);
        } finally {
            richAfter(this.holdIterator);
        }
    }

    @Override
    public <K> Reference<Map.Entry<K, E>> least(Function<E, K> keySupplier) {
        richBefore(keySupplier);
        richBefore(this.holdIterator);
        try {
            Map.Entry<K, E> ret = null;
            E nullElem = null;
            boolean isNop = true;
            long nullCount = 0;
            Map<K, SimpleEntry<E, Long>> countMap = new HashMap<>();
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                K key = keySupplier.apply(elem);
                if (key == null) {
                    nullCount++;
                    if (nullElem == null) {
                        nullElem = elem;
                    }
                } else {
                    if (!countMap.containsKey(key)) {
                        countMap.put(key, new SimpleEntry<>(elem, 1L));
                    } else {
                        SimpleEntry<E, Long> entry = countMap.get(key);
                        entry.setValue(entry.getValue() + 1);
                        countMap.put(key, entry);
                    }
                }
                isNop = false;
            }
            if (isNop) {
                return Reference.nop();
            }
            long maxCount = nullCount;
            ret = new SimpleEntry<>(null, nullElem);
            for (Map.Entry<K, SimpleEntry<E, Long>> entry : countMap.entrySet()) {
                if (entry.getValue().getValue() < maxCount) {
                    ret = new SimpleEntry<>(entry.getKey(), entry.getValue().getKey());
                    maxCount = entry.getValue().getValue();
                }
            }
            return Reference.of(ret);
        } finally {
            richAfter(this.holdIterator);
            richAfter(keySupplier);
        }
    }


    @Override
    public Reference<E> reduce(Supplier<E> firstSupplier, BiFunction<E, E, E> accumulator) {
        richBefore(firstSupplier);
        richBefore(accumulator);
        richBefore(this.holdIterator);
        try {
            E ret = firstSupplier.get();
            boolean isNop = true;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                ret = accumulator.apply(ret, elem);
                isNop = false;
            }
            if (isNop) {
                return Reference.nop();
            }
            return Reference.of(ret);
        } finally {
            richAfter(this.holdIterator);
            richAfter(firstSupplier);
            richAfter(accumulator);
        }
    }

    @Override
    public <T, R> Reference<R> aggregate(Supplier<T> firstSupplier, BiFunction<T, E, T> accumulator, Function<T, R> finisher) {
        richBefore(firstSupplier);
        richBefore(accumulator);
        richBefore(finisher);
        richBefore(this.holdIterator);
        try {
            T ret = firstSupplier.get();
            boolean isNop = true;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                ret = accumulator.apply(ret, elem);
                isNop = false;
            }
            if (isNop) {
                return Reference.nop();
            }
            return Reference.of(finisher.apply(ret));
        } finally {
            richAfter(this.holdIterator);
            richAfter(firstSupplier);
            richAfter(accumulator);
            richAfter(finisher);
        }
    }

    @Override
    public long count() {
        richBefore(this.holdIterator);
        try {
            long ret = 0;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                ret++;
            }
            return ret;
        } finally {
            richAfter(this.holdIterator);
        }
    }

    @Override
    public void forEach(Consumer<E> consumer) {
        richBefore(consumer);
        richBefore(this.holdIterator);
        try {
            if (parallel) {
                delegateParallelism(this.parallelCount,
                        () -> this.pool,
                        AtomicCountDownLatch::new,
                        this.holdIterator,
                        (elem,collector) -> {
                            consumer.accept(elem);
                        });
            } else {
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    consumer.accept(elem);
                }
            }
        } finally {
            richAfter(this.holdIterator);
            richAfter(consumer);
        }
    }

    @Override
    public void forEach(BiConsumer<E, Long> consumer) {
        richBefore(consumer);
        richBefore(this.holdIterator);
        try {
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            latch.begin();
            long idx = 0;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (this.parallel) {
                    latch.count();
                    long taskIdx = idx;
                    this.pool.submit(new AtomicCountDownLatchRunnable(latch) {
                        @Override
                        public void doTask(AtomicCountDownLatch resource) throws Throwable {
                            consumer.accept(elem, taskIdx);
                        }
                    });
                } else {
                    consumer.accept(elem, idx);
                }
                idx++;
            }
            latch.finish();
            if (this.parallel) {
                latch.await();
            }
        } catch (Exception e) {
            throw new IllegalStateException("parallel run exception", e);
        } finally {
            richAfter(this.holdIterator);
            richAfter(consumer);
        }
    }

    @Override
    public void sysout(String prefix) {
        richBefore(this.holdIterator);
        try {
            if (parallel) {
                delegateParallelism(this.parallelCount,
                        () -> this.pool,
                        AtomicCountDownLatch::new,
                        this.holdIterator,
                        (elem,collector) -> {
                            if(prefix!=null){
                                System.out.println(prefix+elem);
                            }else{
                                System.out.println(elem);
                            }
                        });
            } else {
                while (this.holdIterator.hasNext()) {
                    E elem = this.holdIterator.next();
                    if(prefix!=null){
                        System.out.println(prefix+elem);
                    }else{
                        System.out.println(elem);
                    }
                }
            }
        } finally {
            richAfter(this.holdIterator);
        }
    }

    @Override
    public void broadcast(Collection<BiConsumer<E, Long>> biConsumers) {
        for (BiConsumer<E, Long> item : biConsumers) {
            richBefore(item);
        }
        richBefore(this.holdIterator);
        try {
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            latch.begin();
            long idx = 0;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                for (BiConsumer<E, Long> item : biConsumers) {
                    if (this.parallel) {
                        latch.count();
                        long taskIdx = idx;
                        this.pool.submit(new AtomicCountDownLatchRunnable(latch) {
                            @Override
                            public void doTask(AtomicCountDownLatch resource) throws Throwable {
                                item.accept(elem, taskIdx);
                            }
                        });
                    } else {
                        item.accept(elem, idx);
                    }
                }
                idx++;
            }
            latch.finish();
            if (this.parallel) {
                latch.await();
            }
        } catch (Exception e) {
            throw new IllegalStateException("parallel run exception", e);
        } finally {
            richAfter(this.holdIterator);
            for (BiConsumer<E, Long> item : biConsumers) {
                richAfter(item);
            }
        }
    }

    @Override
    public void ring(Collection<BiConsumer<E, Long>> biConsumers) {
        int cnt = 0;
        List<BiConsumer<E, Long>> peeks = new ArrayList<>();
        for (BiConsumer<E, Long> item : biConsumers) {
            richBefore(item);
            peeks.add(item);
            cnt++;
        }
        richBefore(this.holdIterator);
        try {
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            latch.begin();
            long idx = 0;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                int peek = (int) (idx % cnt);
                BiConsumer<E, Long> consumer = peeks.get(peek);
                if (this.parallel) {
                    latch.count();
                    long taskIdx = idx;
                    this.pool.submit(new AtomicCountDownLatchRunnable(latch) {
                        @Override
                        public void doTask(AtomicCountDownLatch resource) throws Throwable {
                            consumer.accept(elem, taskIdx);
                        }
                    });
                } else {
                    consumer.accept(elem, idx);
                }
                idx++;
            }
            latch.finish();
            if (this.parallel) {
                latch.await();
            }
        } catch (Exception e) {
            throw new IllegalStateException("parallel run exception", e);
        } finally {
            richAfter(this.holdIterator);
            for (BiConsumer<E, Long> item : biConsumers) {
                richAfter(item);
            }
        }
    }

    @Override
    public void random(Collection<BiConsumer<E, Long>> biConsumers) {
        SecureRandom random = new SecureRandom();
        int cnt = 0;
        List<BiConsumer<E, Long>> peeks = new ArrayList<>();
        for (BiConsumer<E, Long> item : biConsumers) {
            richBefore(item);
            peeks.add(item);
            cnt++;
        }
        richBefore(this.holdIterator);
        try {
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            latch.begin();
            long idx = 0;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                int peek = random.nextInt(cnt);
                BiConsumer<E, Long> consumer = peeks.get(peek);
                if (this.parallel) {
                    latch.count();
                    long taskIdx = idx;
                    this.pool.submit(new AtomicCountDownLatchRunnable(latch) {
                        @Override
                        public void doTask(AtomicCountDownLatch resource) throws Throwable {
                            consumer.accept(elem, taskIdx);
                        }
                    });
                } else {
                    consumer.accept(elem, idx);
                }
                idx++;
            }
            latch.finish();
            if (this.parallel) {
                latch.await();
            }
        } catch (Exception e) {
            throw new IllegalStateException("parallel run exception", e);
        } finally {
            richAfter(this.holdIterator);
            for (BiConsumer<E, Long> item : biConsumers) {
                richAfter(item);
            }
        }
    }

    @Override
    public <C extends Collection<E>> void batch(int batchSize, Supplier<C> containerSupplier, Consumer<C> consumer) {
        richBefore(containerSupplier);
        richBefore(consumer);
        richBefore(this.holdIterator);
        try {
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            latch.begin();
            C col = containerSupplier.get();
            int count = 0;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                col.add(elem);
                count++;
                if (count == batchSize) {
                    if (this.parallel) {
                        latch.count();
                        C taskCol = containerSupplier.get();
                        taskCol.addAll(col);
                        this.pool.submit(new AtomicCountDownLatchRunnable(latch) {
                            @Override
                            public void doTask(AtomicCountDownLatch resource) throws Throwable {
                                consumer.accept(taskCol);
                            }
                        });
                    } else {
                        consumer.accept(col);
                    }
                    col = containerSupplier.get();
                    count -= batchSize;
                }
            }
            if (count > 0) {
                if (this.parallel) {
                    latch.count();
                    C taskCol = containerSupplier.get();
                    taskCol.addAll(col);
                    this.pool.submit(new AtomicCountDownLatchRunnable(latch) {
                        @Override
                        public void doTask(AtomicCountDownLatch resource) throws Throwable {
                            consumer.accept(taskCol);
                        }
                    });
                } else {
                    consumer.accept(col);
                }
            }

            latch.finish();
            if (this.parallel) {
                latch.await();
            }
        } catch (Exception e) {
            throw new IllegalStateException("parallel run exception", e);
        } finally {
            richAfter(this.holdIterator);
            richAfter(containerSupplier);
            richAfter(consumer);
        }
    }

    @Override
    public String stringify(String prefix, String separator, String suffix) {
        richBefore(this.holdIterator);
        try {
            StringBuilder builder = new StringBuilder();
            if (prefix != null) {
                builder.append(prefix);
            }
            boolean isFirst = true;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (!isFirst) {
                    if (separator != null) {
                        builder.append(separator);
                    }
                }
                builder.append(elem);
                isFirst = false;
            }
            if (suffix != null) {
                builder.append(suffix);
            }
            return builder.toString();
        } finally {
            richAfter(this.holdIterator);
        }
    }

    @Override
    public void toWriter(Writer writer, String prefix, String separator, String suffix) throws IOException {
        richBefore(this.holdIterator);
        try {
            if (prefix != null) {
                writer.write(prefix);
            }
            boolean isFirst = true;
            while (this.holdIterator.hasNext()) {
                E elem = this.holdIterator.next();
                if (!isFirst) {
                    if (separator != null) {
                        writer.write(separator);
                    }
                }
                writer.write(String.valueOf(elem));
                isFirst = false;
            }
            if (suffix != null) {
                writer.write(suffix);
            }
        } finally {
            richAfter(this.holdIterator);
        }
    }

    @Override
    public TimedStreaming<E> timed(Function<E, Long> timestampMapper) {
        return new TimedStreamingImpl<>(this.holdIterator,this,timestampMapper);
    }
}
