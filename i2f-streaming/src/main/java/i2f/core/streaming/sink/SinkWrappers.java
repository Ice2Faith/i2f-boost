package i2f.core.streaming.sink;

import i2f.core.streaming.Streaming;
import i2f.core.streaming.comparator.DefaultComparator;
import i2f.core.streaming.data.KeyedData;
import i2f.core.streaming.parallel.AtomicCountDownLatch;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2023/5/2 14:38
 * @desc
 */
public class SinkWrappers {
    public static <E> StreamingSinkWrapper<E, E> reduce(BiFunction<E, E, E> reducer) {
        return new StreamingSinkWrapper<E, E>() {
            @Override
            public E sink(Iterator<E> iterator) {
                E val = null;
                boolean first = true;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (first) {
                        val = curr;
                    } else {
                        val = reducer.apply(val, curr);
                    }
                    first = false;
                }
                return val;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, E> max(Comparator<E> comparator) {
        return new StreamingSinkWrapper<E, E>() {
            @Override
            public E sink(Iterator<E> iterator) {
                E val = null;
                boolean first = true;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (first) {
                        val = curr;
                    } else {
                        if (comparator.compare(val, curr) < 0) {
                            val = curr;
                        }
                    }
                    first = false;
                }
                return val;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, E> min(Comparator<E> comparator) {
        return new StreamingSinkWrapper<E, E>() {
            @Override
            public E sink(Iterator<E> iterator) {
                E val = null;
                boolean first = true;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (first) {
                        val = curr;
                    } else {
                        if (comparator.compare(val, curr) > 0) {
                            val = curr;
                        }
                    }
                    first = false;
                }
                return val;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, Integer> count() {
        return new StreamingSinkWrapper<E, Integer>() {
            @Override
            public Integer sink(Iterator<E> iterator) {
                int size = 0;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    size++;
                }
                return size;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, E> first(Predicate<E> filter) {
        return new StreamingSinkWrapper<E, E>() {
            @Override
            public E sink(Iterator<E> iterator) {
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (filter.test(curr)) {
                        return curr;
                    }
                }
                return null;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, E> last(Predicate<E> filter) {
        return new StreamingSinkWrapper<E, E>() {
            @Override
            public E sink(Iterator<E> iterator) {
                E val = null;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (filter.test(curr)) {
                        val = curr;
                    }
                }
                return val;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, Integer> each(Consumer<E> consumer) {
        return new StreamingSinkWrapper<E, Integer>() {
            @Override
            public Integer sink(Iterator<E> iterator) {
                if (getContext().isParallel()) {
                    AtomicCountDownLatch latch = new AtomicCountDownLatch();
                    while (iterator.hasNext()) {
                        E val = iterator.next();
                        getContext().getPool().submit(() -> {
                            try {
                                consumer.accept(val);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            } finally {
                                latch.down();
                            }
                        });
                    }
                    latch.finish();
                    try {
                        latch.await();
                    } catch (Exception e) {
                        throw new IllegalStateException("streaming parallel await error : " + e.getMessage(), e);
                    }
                } else {
                    while (iterator.hasNext()) {
                        E val = iterator.next();
                        consumer.accept(val);
                    }
                }
                return 0;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, Integer> each(Predicate<E> consumer) {
        return new StreamingSinkWrapper<E, Integer>() {
            @Override
            public Integer sink(Iterator<E> iterator) {
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    boolean ok = consumer.test(curr);
                    if (!ok) {
                        break;
                    }
                }
                return 0;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, Boolean> anyMatch(Predicate<E> filter) {
        return new StreamingSinkWrapper<E, Boolean>() {
            @Override
            public Boolean sink(Iterator<E> iterator) {
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (filter.test(curr)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, Boolean> allMatch(Predicate<E> filter) {
        return new StreamingSinkWrapper<E, Boolean>() {
            @Override
            public Boolean sink(Iterator<E> iterator) {
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (!filter.test(curr)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static <E, C extends Collection<E>> StreamingSinkWrapper<E, Integer> batch(int batchSize, Supplier<C> supplier, Consumer<C> consumer) {
        return new StreamingSinkWrapper<E, Integer>() {
            @Override
            public Integer sink(Iterator<E> iterator) {
                C once = supplier.get();
                int size = 0;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    once.add(curr);
                    size++;
                    if (size == batchSize) {
                        consumer.accept(once);
                        once = supplier.get();
                        size = 0;
                    }
                }
                if (size > 0) {
                    consumer.accept(once);
                }
                return 0;
            }
        };
    }

    public static <E, C extends Collection<E>> StreamingSinkWrapper<E, C> collection(C col) {
        return new StreamingSinkWrapper<E, C>() {
            @Override
            public C sink(Iterator<E> iterator) {
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    col.add(val);
                }
                return col;
            }
        };
    }

    public static <E, C extends Collection<E>> StreamingSinkWrapper<E, C> collection(Supplier<C> supplier) {
        return new StreamingSinkWrapper<E, C>() {
            @Override
            public C sink(Iterator<E> iterator) {
                C col = supplier.get();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    col.add(val);
                }
                return col;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, Iterator<E>> iterator() {
        return new StreamingSinkWrapper<E, Iterator<E>>() {
            @Override
            public Iterator<E> sink(Iterator<E> iterator) {
                return iterator;
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, Stream<E>> stream() {
        return new StreamingSinkWrapper<E, Stream<E>>() {
            @Override
            public Stream<E> sink(Iterator<E> iterator) {
                List<E> col = new LinkedList<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    col.add(val);
                }
                return col.stream();
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, E[]> array(E[] arr) {
        return new StreamingSinkWrapper<E, E[]>() {
            @Override
            public E[] sink(Iterator<E> iterator) {
                List<E> col = new LinkedList<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    col.add(val);
                }
                return col.toArray(arr);
            }
        };
    }

    public static <R, A, E> StreamingSinkWrapper<E, R> collect(Collector<E, A, R> collector) {
        return new StreamingSinkWrapper<E, R>() {
            @Override
            public R sink(Iterator<E> iterator) {
                A container = collector.supplier().get();
                BiConsumer<A, E> accumulator = collector.accumulator();
                while (iterator.hasNext()) {
                    E item = iterator.next();
                    accumulator.accept(container, item);
                }
                return collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)
                        ? (R) container
                        : collector.finisher().apply(container);
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, String> stringify(Function<E, Object> mapper, Object open, Object separator, Object close) {
        return new StreamingSinkWrapper<E, String>() {
            @Override
            public String sink(Iterator<E> iterator) {
                StringBuilder builder = new StringBuilder();
                boolean isFirst = true;
                while (iterator.hasNext()) {
                    E item = iterator.next();
                    if (isFirst) {
                        if (open != null) {
                            builder.append(open);
                        }
                    }
                    if (!isFirst) {
                        if (separator != null) {
                            builder.append(separator);
                        }
                    }
                    if (mapper != null) {
                        builder.append(mapper.apply(item));
                    } else {
                        builder.append(item);
                    }
                    isFirst = false;
                }
                if (!isFirst) {
                    if (close != null) {
                        builder.append(close);
                    }
                }
                return builder.toString();
            }
        };
    }

    public static <E> StreamingSinkWrapper<E, Map<Streaming.Measure, Object>> measures(Streaming.Measure... measures) {
        return new StreamingSinkWrapper<E, Map<Streaming.Measure, Object>>() {
            @Override
            public Map<Streaming.Measure, Object> sink(Iterator<E> iterator) {
                Map<Streaming.Measure, Object> ret = new HashMap<>();
                if (measures == null || measures.length == 0) {
                    return ret;
                }
                Set<Streaming.Measure> requires = new HashSet<>();
                boolean requireSort = false;
                for (Streaming.Measure item : measures) {
                    requires.add(item);
                    if (item == Streaming.Measure.COUNT) {
                        ret.put(item, 0);
                    }
                    if (item == Streaming.Measure.NULL_COUNT) {
                        ret.put(item, 0);
                    }
                    if (item == Streaming.Measure.NON_NULL_COUNT) {
                        ret.put(item, 0);
                    }
                    if (item == Streaming.Measure.DISTINCT_COUNT) {
                        ret.put(item, 0);
                    }
                    if (item == Streaming.Measure.MEDIAN) {
                        requireSort = true;
                    }
                }
                Comparator<E> comparator = new DefaultComparator<>();
                boolean isFirst = true;
                List<E> list = new LinkedList<>();
                int totalCount = 0;
                Set<E> set = new LinkedHashSet<>();
                Map<E, Integer> map = new LinkedHashMap<>();
                int distinctCount = 0;
                int nullCount = 0;
                BigDecimal sum = new BigDecimal("0");
                E eval = null;
                int notNullCount = 0;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    totalCount++;
                    if (requireSort) {
                        list.add(val);
                    }
                    if (val == null) {
                        nullCount++;
                    } else {
                        notNullCount++;
                    }
                    if (isFirst) {
                        if (requires.contains(Streaming.Measure.FIRST)) {
                            ret.put(Streaming.Measure.FIRST, val);
                        }
                    }
                    if (requires.contains(Streaming.Measure.LAST)) {
                        ret.put(Streaming.Measure.LAST, val);
                    }

                    if (requires.contains(Streaming.Measure.DISTINCT_COUNT)) {
                        if (val != null) {
                            if (!set.contains(val)) {
                                distinctCount++;
                                set.add(val);
                            }
                        }
                    }

                    if (requires.contains(Streaming.Measure.MOST) || requires.contains(Streaming.Measure.LEAST)) {
                        if (val != null) {
                            if (!map.containsKey(val)) {
                                map.put(val, 0);
                            }
                            map.put(val, map.get(val) + 1);
                        }
                    }
                    if (requires.contains(Streaming.Measure.MIN)) {
                        if (isFirst) {
                            ret.put(Streaming.Measure.MIN, val);
                        } else {
                            E old = (E) ret.get(Streaming.Measure.MIN);
                            if (comparator.compare(old, val) > 0) {
                                ret.put(Streaming.Measure.MIN, val);
                            }
                        }
                    }

                    if (requires.contains(Streaming.Measure.MAX)) {
                        if (isFirst) {
                            ret.put(Streaming.Measure.MAX, val);
                        } else {
                            E old = (E) ret.get(Streaming.Measure.MAX);
                            if (comparator.compare(old, val) < 0) {
                                ret.put(Streaming.Measure.MAX, val);
                            }
                        }
                    }

                    if (requires.contains(Streaming.Measure.SUM) || requires.contains(Streaming.Measure.AVG)) {
                        if (val != null) {
                            eval = val;
                            if (val instanceof Number) {
                                sum = sum.add(new BigDecimal(String.valueOf(val)));
                            }
                        }
                    }

                    isFirst = false;
                }

                if (requires.contains(Streaming.Measure.COUNT)) {
                    ret.put(Streaming.Measure.COUNT, totalCount);
                }

                if (requires.contains(Streaming.Measure.NULL_COUNT)) {
                    ret.put(Streaming.Measure.NULL_COUNT, nullCount);
                }
                if (requires.contains(Streaming.Measure.NON_NULL_COUNT)) {
                    ret.put(Streaming.Measure.NON_NULL_COUNT, notNullCount);
                }

                if (requires.contains(Streaming.Measure.DISTINCT_COUNT)) {
                    if (nullCount > 0) {
                        distinctCount++;
                    }
                    ret.put(Streaming.Measure.DISTINCT_COUNT, distinctCount);
                }

                if (requires.contains(Streaming.Measure.MOST) || requires.contains(Streaming.Measure.LEAST)) {
                    KeyedData<E, Integer> most = null;
                    KeyedData<E, Integer> least = null;
                    if (nullCount > 0) {
                        most = new KeyedData<>(null, nullCount);
                        least = new KeyedData<>(null, nullCount);
                    }
                    for (Map.Entry<E, Integer> entry : map.entrySet()) {
                        if (most == null) {
                            most = new KeyedData<>(entry.getKey(), entry.getValue());
                        } else {
                            if (entry.getValue() > most.val) {
                                most = new KeyedData<>(entry.getKey(), entry.getValue());
                            }
                        }
                        if (least == null) {
                            least = new KeyedData<>(entry.getKey(), entry.getValue());
                        } else {
                            if (entry.getValue() < least.val) {
                                least = new KeyedData<>(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                    if (requires.contains(Streaming.Measure.MOST)) {
                        if (most != null) {
                            ret.put(Streaming.Measure.MOST, most.key);
                        }
                    }
                    if (requires.contains(Streaming.Measure.LEAST)) {
                        if (least != null) {
                            ret.put(Streaming.Measure.LEAST, least.key);
                        }
                    }
                }

                if (requires.contains(Streaming.Measure.SUM)) {
                    E elem = castBigDecimal2Number(eval, sum);
                    ret.put(Streaming.Measure.SUM, elem);
                }

                if (requires.contains(Streaming.Measure.AVG)) {
                    BigDecimal num = sum.divide(new BigDecimal(notNullCount), 10, RoundingMode.HALF_UP);
                    E elem = castBigDecimal2Number(eval, num);
                    ret.put(Streaming.Measure.AVG, elem);
                }

                if (requireSort) {
                    list.sort(comparator);
                }

                if (requires.contains(Streaming.Measure.MEDIAN)) {
                    if (totalCount > 0) {
                        int idx = totalCount / 2;
                        E elem = list.get(idx);
                        ret.put(Streaming.Measure.MEDIAN, elem);
                    }
                }

                return ret;
            }
        };
    }

    public static <E> E castBigDecimal2Number(E eval, BigDecimal num) {
        if (num == null) {
            return null;
        }
        if (eval instanceof Integer) {
            return (E) (Integer) num.intValue();
        }
        if (eval instanceof Double) {
            return (E) (Double) num.doubleValue();
        }
        if (eval instanceof Float) {
            return (E) (Float) num.floatValue();
        }
        if (eval instanceof Long) {
            return (E) (Long) num.longValue();
        }
        if (eval instanceof Short) {
            return (E) (Short) num.shortValue();
        }
        if (eval instanceof BigInteger) {
            return (E) num.toBigInteger();
        }
        if (eval instanceof BigDecimal) {
            return (E) num;
        }
        return null;
    }


    public static class AggregateRecursiveTask<E, OUT> extends RecursiveTask<OUT> {
        private int computeSize;
        private int size;
        private List<E> list;
        private Function<List<E>, OUT> computer;
        private BiFunction<OUT, OUT, OUT> accumulator;

        public AggregateRecursiveTask(int computeSize, int size, List<E> list, Function<List<E>, OUT> computer, BiFunction<OUT, OUT, OUT> accumulator) {
            this.computeSize = computeSize;
            this.size = size;
            this.list = list;
            this.computer = computer;
            this.accumulator = accumulator;
        }

        @Override
        protected OUT compute() {
            if (size <= computeSize) {
                return computer.apply(list);
            }
            int mid = size / 2;
            List<E> left = new LinkedList<>();
            int leftSize = 0;
            List<E> right = new LinkedList<>();
            int rightSize = 0;
            int idx = 0;
            for (E item : list) {
                if (idx < mid) {
                    left.add(item);
                    leftSize++;
                } else {
                    right.add(item);
                    rightSize++;
                }
                idx++;
            }
            AggregateRecursiveTask<E, OUT> leftTask = new AggregateRecursiveTask<>(computeSize, leftSize, left, computer, accumulator);
            AggregateRecursiveTask<E, OUT> rightTask = new AggregateRecursiveTask<>(computeSize, rightSize, right, computer, accumulator);

            invokeAll(leftTask, rightTask);

            return accumulator.apply(leftTask.join(), rightTask.join());
        }
    }

    public static <E, OUT> StreamingSinkWrapper<E, OUT> aggregate(int computeSize, Function<List<E>, OUT> computer, BiFunction<OUT, OUT, OUT> accumulator) {
        return new StreamingSinkWrapper<E, OUT>() {
            @Override
            public OUT sink(Iterator<E> iterator) {
                List<E> list = new LinkedList<>();
                int size = 0;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    list.add(curr);
                    size++;
                }
                ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
                OUT ret = pool.invoke(new AggregateRecursiveTask<E, OUT>(computeSize, size, list, computer, accumulator));
                return ret;
            }
        };
    }

    public static class ComplexAggregateRecursiveTask<E, ACC> extends RecursiveTask<ACC> {
        private int computeSize;
        private int size;
        private List<E> list;
        private Supplier<ACC> creator;
        private BiFunction<E, ACC, ACC> accumulator;
        private BiFunction<ACC, ACC, ACC> merger;

        public ComplexAggregateRecursiveTask(int computeSize, int size, List<E> list, Supplier<ACC> creator, BiFunction<E, ACC, ACC> accumulator, BiFunction<ACC, ACC, ACC> merger) {
            this.computeSize = computeSize;
            this.size = size;
            this.list = list;
            this.creator = creator;
            this.accumulator = accumulator;
            this.merger = merger;
        }

        @Override
        protected ACC compute() {
            if (size <= computeSize) {
                ACC acc = creator.get();
                for (E item : list) {
                    acc = accumulator.apply(item, acc);
                }
                return acc;
            }
            int mid = size / 2;
            List<E> left = new LinkedList<>();
            int leftSize = 0;
            List<E> right = new LinkedList<>();
            int rightSize = 0;
            int idx = 0;
            for (E item : list) {
                if (idx < mid) {
                    left.add(item);
                    leftSize++;
                } else {
                    right.add(item);
                    rightSize++;
                }
                idx++;
            }
            ComplexAggregateRecursiveTask<E, ACC> leftTask = new ComplexAggregateRecursiveTask<>(computeSize, leftSize, left, creator, accumulator, merger);
            ComplexAggregateRecursiveTask<E, ACC> rightTask = new ComplexAggregateRecursiveTask<>(computeSize, rightSize, right, creator, accumulator, merger);

            invokeAll(leftTask, rightTask);

            return merger.apply(leftTask.join(), rightTask.join());
        }
    }

    public static <E, OUT, ACC> StreamingSinkWrapper<E, OUT> aggregate(int batchSize,
                                                                       Supplier<ACC> creator,
                                                                       BiFunction<E, ACC, ACC> accumulator,
                                                                       BiFunction<ACC, ACC, ACC> merger,
                                                                       Function<ACC, OUT> outputer) {
        return new StreamingSinkWrapper<E, OUT>() {
            @Override
            public OUT sink(Iterator<E> iterator) {
                List<E> list = new LinkedList<>();
                int size = 0;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    list.add(curr);
                    size++;
                }
                ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
                ACC acc = pool.invoke(new ComplexAggregateRecursiveTask<>(batchSize, size, list, creator, accumulator, merger));
                return outputer.apply(acc);
            }
        };
    }
}
