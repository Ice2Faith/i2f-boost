package i2f.core.streaming.process;

import i2f.core.streaming.Streaming;
import i2f.core.streaming.comparator.AntiComparator;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2023/5/2 14:21
 * @desc
 */
public class ProcessWrappers {
    public static <E> StreamingProcessor<E, E> filter(Predicate<E> filter) {
        return new StreamingProcessCollectParallelWrapper<E, E>() {
            @Override
            public void handle(E elem, Consumer<E> consumer) {
                if (filter.test(elem)) {
                    consumer.accept(elem);
                }
            }
        };
    }

    public static <E, OUT> StreamingProcessor<E, OUT> mapper(Function<E, OUT> mapper) {
        return new StreamingProcessCollectParallelWrapper<E, OUT>() {
            @Override
            public void handle(E elem, Consumer<OUT> consumer) {
                consumer.accept(mapper.apply(elem));
            }
        };
    }

    public static <E, OUT> StreamingProcessor<E, OUT> flatMap(BiConsumer<E, Consumer<OUT>> mapper) {
        return new StreamingProcessCollectParallelWrapper<E, OUT>() {
            @Override
            public void handle(E elem, Consumer<OUT> consumer) {
                mapper.accept(elem, consumer);
            }
        };
    }

    public static <E> StreamingProcessor<E, E> before(Predicate<E> filter) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (filter.test(val)) {
                        break;
                    }
                    consumer.accept(val);
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> after(Predicate<E> filter) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                boolean ok = false;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (filter.test(val)) {
                        ok = true;
                    }
                    if (ok) {
                        consumer.accept(val);
                    }
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> head(int count) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                int cnt = count;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    cnt--;
                    if (cnt < 0) {
                        break;
                    }
                    consumer.accept(val);
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> tail(int count) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                LinkedList<E> list = new LinkedList<>();
                int lsize = 0;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (lsize >= count) {
                        list.removeFirst();
                    }
                    list.add(val);
                    lsize++;
                }
                for (E item : list) {
                    consumer.accept(item);
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> resample(double rate) {
        return new StreamingProcessCollectParallelWrapper<E, E>() {
            private SecureRandom rand = new SecureRandom();

            @Override
            public void handle(E elem, Consumer<E> consumer) {
                if (rand.nextDouble() <= rate) {
                    consumer.accept(elem);
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> between(Predicate<E> begin, Predicate<E> end) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                boolean ok = false;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (begin.test(val)) {
                        ok = true;
                    }
                    if (ok) {
                        consumer.accept(val);
                        if (end.test(val)) {
                            break;
                        }
                    }
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> skip(int count) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                int cnt = count;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (cnt <= 0) {
                        consumer.accept(val);
                    }
                    cnt--;
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> limit(int count) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                int cnt = count;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    if (cnt < 0) {
                        break;
                    }
                    cnt--;
                    consumer.accept(val);
                }
            }
        };
    }

    public static <E, R> StreamingProcessor<E, E> distinct(Function<E, R> keyer) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                Set<R> kset = new LinkedHashSet<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    R k = keyer.apply(val);
                    if (!kset.contains(k)) {
                        consumer.accept(val);
                        kset.add(k);
                    }
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> sort(Comparator<E> comparator, boolean anti) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                List<E> list = new LinkedList<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    list.add(val);
                }
                if (anti) {
                    list.sort(new AntiComparator<>(comparator));
                } else {
                    list.sort(comparator);
                }
                for (E item : list) {
                    consumer.accept(item);
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> shuffle() {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                List<E> rlist = new LinkedList<>();
                int size = 0;
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    rlist.add(val);
                    size++;
                }
                Random rand = new Random();
                ArrayList<E> list = new ArrayList<>(size);
                list.addAll(rlist);
                for (int i = size - 1; i > 0; i--) {
                    if (rand.nextDouble() < 0.95) {
                        int j = rand.nextInt(i);
                        E tmp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, tmp);
                    }
                }
                for (E item : list) {
                    consumer.accept(item);
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> reverse() {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> consumer) {
                LinkedList<E> list = new LinkedList<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    list.add(val);
                }
                while (!list.isEmpty()) {
                    E val = list.getLast();
                    consumer.accept(val);
                    list.removeLast();
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> sync(Consumer<Collection<E>> consumer) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> collector) {
                List<E> list = new LinkedList<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    list.add(val);
                }
                consumer.accept(list);
                for (E item : list) {
                    collector.accept(item);
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> peek(Consumer<E> consumer) {
        return new StreamingProcessCollectParallelWrapper<E, E>() {
            @Override
            public void handle(E elem, Consumer<E> collector) {
                collector.accept(elem);
                consumer.accept(elem);
            }
        };
    }

    public static <E> StreamingProcessor<E, E> fork(Consumer<Streaming<E>> consumer) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> collector) {
                List<E> list = new LinkedList<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    collector.accept(val);
                    list.add(val);
                }
                consumer.accept(Streaming.source(list));
            }
        };
    }

    public static <E> StreamingProcessor<E, E> join(Streaming<E> streaming) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> collector) {
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    collector.accept(val);
                }
                streaming.each(collector);
            }
        };
    }

    public static <E> StreamingProcessor<E, E> merge(Streaming<E> streaming, Comparator<E> comparator) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> collector) {
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
                        if (comparator.compare(v1, v2) <= 0) {
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

    public static <E, OUT, CO> StreamingProcessor<E, OUT> connect(Streaming<CO> coStreaming, BiPredicate<E, CO> condition, BiFunction<E, CO, OUT> linker) {
        return new StreamingProcessCollectWrapper<E, OUT>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<OUT> collector) {
                List<E> list1 = new LinkedList<>();
                while (iterator.hasNext()) {
                    list1.add(iterator.next());
                }
                LinkedList<CO> list2 = coStreaming.collection(new LinkedList<>());
                for (E e1 : list1) {
                    for (CO e2 : list2) {
                        if (condition.test(e1, e2)) {
                            OUT val = linker.apply(e1, e2);
                            collector.accept(val);
                        }
                    }
                }
            }
        };
    }

    public static <E, OUT, CO> StreamingProcessor<E, OUT> combine(Streaming<CO> coStreaming, BiFunction<E, CO, OUT> linker) {
        return new StreamingProcessCollectWrapper<E, OUT>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<OUT> collector) {
                Iterator<CO> iter = coStreaming.iterator();
                while (iterator.hasNext() && iter.hasNext()) {
                    E e1 = iterator.next();
                    CO e2 = iter.next();
                    OUT val = linker.apply(e1, e2);
                    collector.accept(val);
                }
            }
        };
    }

    public static <E> StreamingProcessor<E, E> resample(double rate, Consumer<E> consumer) {
        return new StreamingProcessCollectParallelWrapper<E, E>() {
            private SecureRandom rand = new SecureRandom();

            @Override
            public void handle(E val, Consumer<E> collector) {
                collector.accept(val);
                if (rand.nextDouble() <= rate) {
                    consumer.accept(val);
                }
            }
        };
    }

    public static <E, R> StreamingProcessor<E, E> include(Streaming<E> streaming, Function<E, R> keyer) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> collector) {
                Set<R> rset = new LinkedHashSet<>();
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    R k = keyer.apply(val);
                    rset.add(k);
                    collector.accept(val);
                }
                Iterator<E> iter = streaming.iterator();
                while (iter.hasNext()) {
                    E val = iter.next();
                    R k = keyer.apply(val);
                    if (!rset.contains(k)) {
                        collector.accept(val);
                        rset.add(k);
                    }
                }
            }
        };
    }

    public static <E, R> StreamingProcessor<E, E> exclude(Streaming<E> streaming, Function<E, R> keyer) {
        return new StreamingProcessCollectWrapper<E, E>() {
            @Override
            public void collect(Iterator<E> iterator, Consumer<E> collector) {
                Set<R> rset = new LinkedHashSet<>();
                Iterator<E> iter = streaming.iterator();
                while (iter.hasNext()) {
                    E val = iter.next();
                    R k = keyer.apply(val);
                    rset.add(k);
                }
                while (iterator.hasNext()) {
                    E val = iterator.next();
                    R k = keyer.apply(val);
                    if (!rset.contains(k)) {
                        collector.accept(val);
                    }
                }

            }
        };
    }
}
