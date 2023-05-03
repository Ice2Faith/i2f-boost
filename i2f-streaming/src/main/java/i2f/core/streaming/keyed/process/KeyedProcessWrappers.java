package i2f.core.streaming.keyed.process;

import i2f.core.streaming.Streaming;
import i2f.core.streaming.comparator.AntiComparator;
import i2f.core.streaming.keyed.functional.*;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2023/5/2 16:26
 * @desc
 */
public class KeyedProcessWrappers {
    public static <K, E> KeyedStreamingProcessor<K, E, E> filter(KeyedPredicate<K, E> filter) {
        return new KeyedStreamingProcessCollectParallelWrapper<K, E, E>() {
            @Override
            public void handle(K key, E elem, Consumer<E> consumer) {
                if (filter.test(elem, key)) {
                    consumer.accept(elem);
                }
            }
        };
    }


    public static <K, E, OUT> KeyedStreamingProcessor<K, E, OUT> mapper(KeyedFunction<K, E, OUT> mapper) {
        return new KeyedStreamingProcessCollectParallelWrapper<K, E, OUT>() {
            @Override
            public void handle(K key, E elem, Consumer<OUT> consumer) {
                consumer.accept(mapper.apply(elem, key));
            }
        };
    }

    public static <K, E, OUT> KeyedStreamingProcessor<K, E, OUT> flatMap(KeyedBiConsumer<K, E, Consumer<OUT>> mapper) {
        return new KeyedStreamingProcessCollectParallelWrapper<K, E, OUT>() {
            @Override
            public void handle(K key, E elem, Consumer<OUT> consumer) {
                mapper.accept(elem, consumer, key);
            }
        };
    }

    public static <K, E> KeyedStreamingProcessor<K, E, E> before(KeyedPredicate<K, E> filter) {
        return new KeyedStreamingProcessCollectWrapper<K, E, E>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<E> consumer) {
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (filter.test(val, key)) {
                        break;
                    }
                    consumer.accept(val);
                }
            }
        };
    }

    public static <K, E> KeyedStreamingProcessor<K, E, E> after(KeyedPredicate<K, E> filter) {
        return new KeyedStreamingProcessCollectWrapper<K, E, E>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<E> consumer) {
                boolean ok = false;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (filter.test(val, key)) {
                        ok = true;
                    }
                    if (ok) {
                        consumer.accept(val);
                    }
                }
            }
        };
    }


    public static <K, E> KeyedStreamingProcessor<K, E, E> between(KeyedPredicate<K, E> begin, KeyedPredicate<K, E> end) {
        return new KeyedStreamingProcessCollectWrapper<K, E, E>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<E> consumer) {
                boolean ok = false;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (begin.test(val, key)) {
                        ok = true;
                    }
                    if (ok) {
                        consumer.accept(val);
                        if (end.test(val, key)) {
                            break;
                        }
                    }
                }
            }
        };
    }

    public static <K, E, R> KeyedStreamingProcessor<K, E, E> distinct(KeyedFunction<K, E, R> keyer) {
        return new KeyedStreamingProcessCollectWrapper<K, E, E>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<E> consumer) {
                Set<R> kset = new LinkedHashSet<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    R k = keyer.apply(val, key);
                    if (!kset.contains(k)) {
                        consumer.accept(val);
                        kset.add(k);
                    }
                }
            }
        };
    }

    public static <K, E> KeyedStreamingProcessor<K, E, E> sort(KeyedComparator<K, E> comparator, boolean anti) {
        return new KeyedStreamingProcessCollectWrapper<K, E, E>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<E> consumer) {
                List<E> list = new LinkedList<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    list.add(val);
                }
                Comparator<E> adapter = new Comparator<E>() {
                    @Override
                    public int compare(E o1, E o2) {
                        return comparator.compare(o1, o2, key);
                    }
                };
                if (anti) {
                    list.sort(new AntiComparator<>(adapter));
                } else {
                    list.sort(adapter);
                }
                for (E item : list) {
                    consumer.accept(item);
                }
            }
        };
    }

    public static <K, E> KeyedStreamingProcessor<K, E, E> sync(KeyedConsumer<K, Collection<E>> consumer) {
        return new KeyedStreamingProcessCollectWrapper<K, E, E>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<E> collector) {
                List<E> list = new LinkedList<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    list.add(val);
                }
                consumer.accept(list, key);
                for (E item : list) {
                    collector.accept(item);
                }
            }
        };
    }

    public static <K, E> KeyedStreamingProcessor<K, E, E> peek(KeyedConsumer<K, E> consumer) {
        return new KeyedStreamingProcessCollectParallelWrapper<K, E, E>() {
            @Override
            public void handle(K key, E val, Consumer<E> collector) {
                collector.accept(val);
                consumer.accept(val, key);
            }
        };
    }

    public static <K, E> KeyedStreamingProcessor<K, E, E> fork(KeyedConsumer<K, Streaming<E>> consumer) {
        return new KeyedStreamingProcessCollectWrapper<K, E, E>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<E> collector) {
                List<E> list = new LinkedList<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    collector.accept(val);
                    list.add(val);
                }
                consumer.accept(Streaming.source(list), key);
            }
        };
    }


    public static <K, E> KeyedStreamingProcessor<K, E, E> merge(Streaming<E> streaming, KeyedComparator<K, E> comparator) {
        return new KeyedStreamingProcessCollectWrapper<K, E, E>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<E> collector) {
                Iterator<E> iter = streaming.iterator();
                E v1 = null;
                boolean bv1 = false;
                E v2 = null;
                boolean bv2 = false;
                while (true) {
                    if (!bv1 && iterator.hasNext()) {
                        v1 = iterator.next();
                        bv1 = true;
                    }
                    if (!bv2 && iter.hasNext()) {
                        v2 = iter.next();
                        bv2 = true;
                    }
                    if (!bv1 && !bv2) {
                        break;
                    }
                    if (bv1 && bv2) {
                        if (comparator.compare(v1, v2, key) <= 0) {
                            collector.accept(v1);
                            bv1 = false;
                        } else {
                            collector.accept(v2);
                            bv2 = false;
                        }
                    } else if (bv1) {
                        collector.accept(v1);
                        bv1 = false;
                    } else {
                        collector.accept(v2);
                        bv2 = false;
                    }
                }
            }
        };
    }

    public static <K, E, OUT, CO> KeyedStreamingProcessor<K, E, OUT> connect(Streaming<CO> coStreaming, KeyedBiPredicate<K, E, CO> condition, KeyedBiFunction<K, E, CO, OUT> linker) {
        return new KeyedStreamingProcessCollectWrapper<K, E, OUT>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<OUT> collector) {
                List<E> list1 = new LinkedList<>();
                while (iterator.hasNext()) {
                    list1.add(iterator.next());
                }
                LinkedList<CO> list2 = coStreaming.collection(new LinkedList<>());
                for (E e1 : list1) {
                    for (CO e2 : list2) {
                        if (condition.test(e1, e2, key)) {
                            OUT val = linker.apply(e1, e2, key);
                            collector.accept(val);
                        }
                    }
                }
            }
        };
    }

    public static <K, E, OUT, CO> KeyedStreamingProcessor<K, E, OUT> combine(Streaming<CO> coStreaming, KeyedBiFunction<K, E, CO, OUT> linker) {
        return new KeyedStreamingProcessCollectWrapper<K, E, OUT>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<OUT> collector) {
                Iterator<CO> iter = coStreaming.iterator();
                while (iterator.hasNext() && iter.hasNext()) {
                    E e1 = iterator.next();
                    CO e2 = iter.next();
                    OUT val = linker.apply(e1, e2, key);
                    collector.accept(val);
                }
            }
        };
    }

    public static <K, E> KeyedStreamingProcessor<K, E, E> resample(double rate, KeyedConsumer<K, E> consumer) {
        return new KeyedStreamingProcessCollectParallelWrapper<K, E, E>() {
            private SecureRandom rand = new SecureRandom();

            @Override
            public void handle(K key, E val, Consumer<E> collector) {
                collector.accept(val);
                if (rand.nextDouble() <= rate) {
                    consumer.accept(val, key);
                }
            }
        };
    }

    public static <K, E, R> KeyedStreamingProcessor<K, E, E> include(Streaming<E> streaming, KeyedFunction<K, E, R> keyer) {
        return new KeyedStreamingProcessCollectWrapper<K, E, E>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<E> collector) {
                Set<R> rset = new LinkedHashSet<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    R k = keyer.apply(val, key);
                    rset.add(k);
                    collector.accept(val);
                }
                Iterator<E> iter = streaming.iterator();
                while (iter.hasNext()) {
                    E val = iter.next();
                    R k = keyer.apply(val, key);
                    if (!rset.contains(k)) {
                        collector.accept(val);
                        rset.add(k);
                    }
                }
            }
        };
    }

    public static <K, E, R> KeyedStreamingProcessor<K, E, E> exclude(Streaming<E> streaming, KeyedFunction<K, E, R> keyer) {
        return new KeyedStreamingProcessCollectWrapper<K, E, E>() {
            @Override
            public void collect(K key, Iterator<E> iterator, Consumer<E> collector) {
                Set<R> rset = new LinkedHashSet<>();
                Iterator<E> iter = streaming.iterator();
                while (iter.hasNext()) {
                    E val = iter.next();
                    R k = keyer.apply(val, key);
                    rset.add(k);
                }
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    R k = keyer.apply(val, key);
                    if (!rset.contains(k)) {
                        collector.accept(val);
                    }
                }

            }
        };
    }
}
