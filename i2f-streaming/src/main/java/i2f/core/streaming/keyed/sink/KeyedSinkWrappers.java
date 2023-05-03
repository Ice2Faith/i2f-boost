package i2f.core.streaming.keyed.sink;

import i2f.core.streaming.keyed.functional.*;
import i2f.core.streaming.parallel.AtomicCountDownLatch;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author Ice2Faith
 * @date 2023/5/2 17:24
 * @desc
 */
public class KeyedSinkWrappers {
    public static <K, E> KeyedStreamingSinker<K, E, E> reduce(KeyedBiFunction<K, E, E, E> reducer) {
        return new KeyedStreamingSinkWrapper<K, E, E>() {
            @Override
            public E sink(K key, Iterator<E> iterator) {
                E val = null;
                boolean first = true;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (first) {
                        val = curr;
                    } else {
                        val = reducer.apply(val, curr, key);
                    }
                    first = false;
                }
                return val;
            }
        };
    }

    public static <K, E> KeyedStreamingSinker<K, E, E> max(KeyedComparator<K, E> comparator) {
        return new KeyedStreamingSinkWrapper<K, E, E>() {
            @Override
            public E sink(K key, Iterator<E> iterator) {
                E val = null;
                boolean first = true;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (first) {
                        val = curr;
                    } else {
                        if (comparator.compare(val, curr, key) < 0) {
                            val = curr;
                        }
                    }
                    first = false;
                }
                return val;
            }
        };
    }

    public static <K, E> KeyedStreamingSinker<K, E, E> min(KeyedComparator<K, E> comparator) {
        return new KeyedStreamingSinkWrapper<K, E, E>() {
            @Override
            public E sink(K key, Iterator<E> iterator) {
                E val = null;
                boolean first = true;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (first) {
                        val = curr;
                    } else {
                        if (comparator.compare(val, curr, key) > 0) {
                            val = curr;
                        }
                    }
                    first = false;
                }
                return val;
            }
        };
    }


    public static <K, E> KeyedStreamingSinker<K, E, E> first(KeyedPredicate<K, E> filter) {
        return new KeyedStreamingSinkWrapper<K, E, E>() {
            @Override
            public E sink(K key, Iterator<E> iterator) {
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (filter.test(curr, key)) {
                        return curr;
                    }
                }
                return null;
            }
        };
    }

    public static <K, E> KeyedStreamingSinker<K, E, E> last(KeyedPredicate<K, E> filter) {
        return new KeyedStreamingSinkWrapper<K, E, E>() {
            @Override
            public E sink(K key, Iterator<E> iterator) {
                E val = null;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (filter.test(curr, key)) {
                        val = curr;
                    }
                }
                return val;
            }
        };
    }

    public static <K, E> KeyedStreamingSinker<K, E, Integer> each(KeyedConsumer<K, E> consumer) {
        return new KeyedStreamingSinkWrapper<K, E, Integer>() {
            @Override
            public Integer sink(K key, Iterator<E> iterator) {
                if (getContext().isParallel()) {
                    AtomicCountDownLatch latch = new AtomicCountDownLatch();
                    while (iterator.hasNext()) {
                        E val = iterator.next();
                        getContext().getPool().submit(() -> {
                            try {
                                consumer.accept(val, key);
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
                        consumer.accept(val, key);
                    }
                }
                return 0;
            }
        };
    }

    public static <K, E> KeyedStreamingSinker<K, E, Integer> each(KeyedPredicate<K, E> consumer) {
        return new KeyedStreamingSinkWrapper<K, E, Integer>() {
            @Override
            public Integer sink(K key, Iterator<E> iterator) {
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    boolean ok = consumer.test(curr, key);
                    if (!ok) {
                        break;
                    }
                }
                return 0;
            }
        };
    }

    public static <K, E> KeyedStreamingSinker<K, E, Boolean> anyMatch(KeyedPredicate<K, E> filter) {
        return new KeyedStreamingSinkWrapper<K, E, Boolean>() {
            @Override
            public Boolean sink(K key, Iterator<E> iterator) {
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (filter.test(curr, key)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static <K, E> KeyedStreamingSinker<K, E, Boolean> allMatch(KeyedPredicate<K, E> filter) {
        return new KeyedStreamingSinkWrapper<K, E, Boolean>() {
            @Override
            public Boolean sink(K key, Iterator<E> iterator) {
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    if (!filter.test(curr, key)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static <K, E, C extends Collection<E>> KeyedStreamingSinker<K, E, Integer> batch(int batchSize, KeyedSupplier<K, C> supplier, KeyedConsumer<K, C> consumer) {
        return new KeyedStreamingSinkWrapper<K, E, Integer>() {
            @Override
            public Integer sink(K key, Iterator<E> iterator) {
                C once = supplier.get(key);
                int size = 0;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    once.add(curr);
                    size++;
                    if (size == batchSize) {
                        consumer.accept(once, key);
                        once = supplier.get(key);
                        size = 0;
                    }
                }
                if (size > 0) {
                    consumer.accept(once, key);
                }
                return 0;
            }
        };
    }

    public static <K, E, C extends Collection<E>> KeyedStreamingSinker<K, E, C> collection(KeyedSupplier<K, C> supplier) {
        return new KeyedStreamingSinkWrapper<K, E, C>() {
            @Override
            public C sink(K key, Iterator<E> iterator) {
                C col = supplier.get(key);
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    col.add(val);
                }
                return col;
            }
        };
    }


    public static class KeyedAggregateRecursiveTask<K, E, OUT> extends RecursiveTask<OUT> {
        private K key;
        private int computeSize;
        private int size;
        private List<E> list;
        private KeyedFunction<K, List<E>, OUT> computer;
        private KeyedBiFunction<K, OUT, OUT, OUT> accumulator;

        public KeyedAggregateRecursiveTask(K key, int computeSize, int size, List<E> list, KeyedFunction<K, List<E>, OUT> computer, KeyedBiFunction<K, OUT, OUT, OUT> accumulator) {
            this.key = key;
            this.computeSize = computeSize;
            this.size = size;
            this.list = list;
            this.computer = computer;
            this.accumulator = accumulator;
        }

        @Override
        protected OUT compute() {
            if (size <= computeSize) {
                return computer.apply(list, key);
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
            KeyedAggregateRecursiveTask<K, E, OUT> leftTask = new KeyedAggregateRecursiveTask<>(key, computeSize, leftSize, left, computer, accumulator);
            KeyedAggregateRecursiveTask<K, E, OUT> rightTask = new KeyedAggregateRecursiveTask<>(key, computeSize, rightSize, right, computer, accumulator);

            invokeAll(leftTask, rightTask);

            return accumulator.apply(leftTask.join(), rightTask.join(), key);
        }
    }

    public static <K, E, OUT> KeyedStreamingSinkWrapper<K, E, OUT> aggregate(int computeSize, KeyedFunction<K, List<E>, OUT> computer, KeyedBiFunction<K, OUT, OUT, OUT> accumulator) {
        return new KeyedStreamingSinkWrapper<K, E, OUT>() {
            @Override
            public OUT sink(K key, Iterator<E> iterator) {
                List<E> list = new LinkedList<>();
                int size = 0;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    list.add(curr);
                    size++;
                }
                ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
                OUT ret = pool.invoke(new KeyedAggregateRecursiveTask<K, E, OUT>(key, computeSize, size, list, computer, accumulator));
                return ret;
            }
        };
    }

    public static class KeyedComplexAggregateRecursiveTask<K, E, ACC> extends RecursiveTask<ACC> {
        private K key;
        private int computeSize;
        private int size;
        private List<E> list;
        private KeyedSupplier<K, ACC> creator;
        private KeyedBiFunction<K, E, ACC, ACC> accumulator;
        private KeyedBiFunction<K, ACC, ACC, ACC> merger;

        public KeyedComplexAggregateRecursiveTask(K key, int computeSize, int size, List<E> list, KeyedSupplier<K, ACC> creator, KeyedBiFunction<K, E, ACC, ACC> accumulator, KeyedBiFunction<K, ACC, ACC, ACC> merger) {
            this.key = key;
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
                ACC acc = creator.get(key);
                for (E item : list) {
                    acc = accumulator.apply(item, acc, key);
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
            KeyedComplexAggregateRecursiveTask<K, E, ACC> leftTask = new KeyedComplexAggregateRecursiveTask<>(key, computeSize, leftSize, left, creator, accumulator, merger);
            KeyedComplexAggregateRecursiveTask<K, E, ACC> rightTask = new KeyedComplexAggregateRecursiveTask<>(key, computeSize, rightSize, right, creator, accumulator, merger);

            invokeAll(leftTask, rightTask);

            return merger.apply(leftTask.join(), rightTask.join(), key);
        }
    }

    public static <K, E, OUT, ACC> KeyedStreamingSinkWrapper<K, E, OUT> aggregate(int batchSize,
                                                                                  KeyedSupplier<K, ACC> creator,
                                                                                  KeyedBiFunction<K, E, ACC, ACC> accumulator,
                                                                                  KeyedBiFunction<K, ACC, ACC, ACC> merger,
                                                                                  KeyedFunction<K, ACC, OUT> outputer) {
        return new KeyedStreamingSinkWrapper<K, E, OUT>() {
            @Override
            public OUT sink(K key, Iterator<E> iterator) {
                List<E> list = new LinkedList<>();
                int size = 0;
                while (iterator.hasNext()) {
                    E curr = iterator.next();
                    list.add(curr);
                    size++;
                }
                ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
                ACC acc = pool.invoke(new KeyedComplexAggregateRecursiveTask<>(key, batchSize, size, list, creator, accumulator, merger));
                return outputer.apply(acc, key);
            }
        };
    }
}
