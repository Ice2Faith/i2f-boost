package i2f.stream.timed;

import i2f.stream.Streaming;
import i2f.stream.impl.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/3/6 16:28
 * @desc
 */
public class TimedStreamingImpl<E> extends StreamingImpl<Map.Entry<Long, E>> implements TimedStreaming<E> {
    private Function<E, Long> timestampMapper;

    public TimedStreamingImpl(Iterator<E> iterator, Function<E, Long> timestampMapper) {
        super(timedIterator(iterator, timestampMapper));
        this.timestampMapper = timestampMapper;
    }

    public TimedStreamingImpl(Iterator<E> iterator, Map<String, Object> globalContext, Function<E, Long> timestampMapper) {
        super(timedIterator(iterator, timestampMapper), globalContext);
        this.timestampMapper = timestampMapper;
    }

    public TimedStreamingImpl(Iterator<E> iterator, StreamingImpl<?> parent, Function<E, Long> timestampMapper) {
        super(timedIterator(iterator, timestampMapper), parent);
        this.timestampMapper = timestampMapper;
    }

    public TimedStreamingImpl(Iterator<Map.Entry<Long, E>> iterator) {
        super(iterator);
    }

    public TimedStreamingImpl(Iterator<Map.Entry<Long, E>> iterator, Map<String, Object> globalContext) {
        super(iterator, globalContext);
    }

    public TimedStreamingImpl(Iterator<Map.Entry<Long, E>> iterator, StreamingImpl<?> parent) {
        super(iterator, parent);
    }

    public static <E> Iterator<Map.Entry<Long, E>> timedIterator(Iterator<E> iterator, Function<E, Long> timestampMapper) {
        return new SupplierIterator<>(() -> {
            while (iterator.hasNext()) {
                E elem = iterator.next();
                Long ts = timestampMapper.apply(elem);
                return Reference.of(new SimpleEntry<>(ts, elem));
            }
            return Reference.finish();
        });
    }

    @Override
    public TimedStreaming<E> timeOrdered(long maxDelayMillSeconds) {
        return new TimedStreamingImpl<>(new LazyIterator<Map.Entry<Long, E>>(() -> {
            TreeMap<Long, List<Map.Entry<Long, E>>> delayMap = new TreeMap<>();
            return new SupplierBufferIterator<>(() -> {
                synchronized (delayMap) {
                    while (this.holdIterator.hasNext()) {
                        Map.Entry<Long, E> elem = this.holdIterator.next();
                        Long ts = elem.getKey();
                        if (!delayMap.containsKey(ts)) {
                            delayMap.put(ts, new LinkedList<>());
                        }
                        delayMap.get(ts).add(elem);

                        List<Long> releaseList = new LinkedList<>();
                        for (Map.Entry<Long, List<Map.Entry<Long, E>>> entry : delayMap.entrySet()) {
                            Long dts = entry.getKey();
                            if (dts + maxDelayMillSeconds <= ts) {
                                releaseList.add(dts);
                            }
                        }

                        List<Map.Entry<Long, E>> once = new LinkedList<>();
                        for (Long dts : releaseList) {
                            List<Map.Entry<Long, E>> next = delayMap.remove(dts);
                            once.addAll(next);
                        }

                        return Reference.of(once);
                    }

                    if (!delayMap.isEmpty()) {
                        List<Map.Entry<Long, E>> once = new LinkedList<>();
                        for (Map.Entry<Long, List<Map.Entry<Long, E>>> entry : delayMap.entrySet()) {
                            once.addAll(entry.getValue());
                        }
                        return Reference.of(once);
                    }

                    return Reference.finish();
                }
            });
        }), this);
    }

    @Override
    public Streaming<Map.Entry<List<Map.Entry<Long, E>>, Map.Entry<Long, Long>>> slideTimeWindow(long windowMillSeconds, long slideMillSeconds) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {

            LinkedList<SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, SimpleEntry<Long, Long>>>> waitList = new LinkedList<>();
            AtomicLong elemCount = new AtomicLong(0L);
            AtomicLong windowCount = new AtomicLong(0L);
            AtomicLong current = new AtomicLong(0);

            return new SupplierBufferIterator<Map.Entry<List<Map.Entry<Long, E>>, Map.Entry<Long, Long>>>(() -> {
                synchronized (waitList) {
                    while (this.holdIterator.hasNext()) {
                        Map.Entry<Long, E> elem = this.holdIterator.next();
                        elemCount.incrementAndGet();

                        for (SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, SimpleEntry<Long, Long>>> entry : waitList) {
                            if (entry.getValue().getKey() + windowMillSeconds > elem.getKey()) {
                                entry.getKey().add(elem);
                            }
                        }

                        boolean isNew = false;
                        if (elemCount.get() == 1) {
                            current.set(elem.getKey());
                            LinkedList<Map.Entry<Long, E>> newList = new LinkedList<>();
                            newList.add(elem);
                            windowCount.incrementAndGet();
                            waitList.add(new SimpleEntry<>(newList, new SimpleEntry<>(elem.getKey(), new SimpleEntry<>(elemCount.get(), windowCount.get()))));
                            isNew = true;
                        }


                        if (elem.getKey() >= current.get() + slideMillSeconds) {
                            current.getAndAdd(slideMillSeconds);

                            if (!isNew) {
                                current.set(elem.getKey());
                                LinkedList<Map.Entry<Long, E>> newList = new LinkedList<>();
                                newList.add(elem);
                                windowCount.incrementAndGet();
                                waitList.add(new SimpleEntry<>(newList, new SimpleEntry<>(elem.getKey(), new SimpleEntry<>(elemCount.get(), windowCount.get()))));
                            }
                        }


                        Collection<Map.Entry<List<Map.Entry<Long, E>>, Map.Entry<Long, Long>>> buff = new LinkedList<>();
                        while (!waitList.isEmpty()) {
                            SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, SimpleEntry<Long, Long>>> first = waitList.getFirst();
                            if (first.getValue().getKey() + windowMillSeconds <= elem.getKey()) {
                                buff.add(new SimpleEntry<>(first.getKey(), first.getValue().getValue()));
                                waitList.removeFirst();
                            } else {
                                break;
                            }
                        }


                        if (!buff.isEmpty()) {
                            return Reference.of(buff);
                        } else {
                            return Reference.nop();
                        }
                    }

                    Collection<Map.Entry<List<Map.Entry<Long, E>>, Map.Entry<Long, Long>>> buff = new LinkedList<>();
                    while (!waitList.isEmpty()) {
                        SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, SimpleEntry<Long, Long>>> first = waitList.getFirst();
                        buff.add(new SimpleEntry<>(first.getKey(), first.getValue().getValue()));
                        waitList.removeFirst();
                    }

                    if (!buff.isEmpty()) {
                        return Reference.of(buff);
                    }

                    richAfter(this.holdIterator);
                    return Reference.finish();
                }
            });

        }), this);
    }

    @Override
    public Streaming<Map.Entry<List<Map.Entry<Long, E>>, Map.Entry<Long, Long>>> sessionTimeWindow(long sessionTimeoutMillSeconds) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            LinkedList<SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, SimpleEntry<Long, Long>>>> waitList = new LinkedList<>();
            AtomicLong elemCount = new AtomicLong(0L);
            AtomicLong windowCount = new AtomicLong(0L);
            AtomicLong current = new AtomicLong(0);

            return new SupplierBufferIterator<Map.Entry<List<Map.Entry<Long, E>>, Map.Entry<Long, Long>>>(() -> {
                synchronized (waitList) {
                    while (this.holdIterator.hasNext()) {
                        Map.Entry<Long, E> elem = this.holdIterator.next();
                        elemCount.incrementAndGet();

                        boolean isTimeout=false;
                        Collection<Map.Entry<List<Map.Entry<Long, E>>, Map.Entry<Long, Long>>> buff = new LinkedList<>();
                        for (SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, SimpleEntry<Long, Long>>> entry : waitList) {
                            if (entry.getValue().getKey() + sessionTimeoutMillSeconds > elem.getKey()) {
                                entry.getValue().setKey(elem.getKey());
                                entry.getKey().add(elem);
                            } else {
                                buff.add(new SimpleEntry<>(entry.getKey(), entry.getValue().getValue()));
                                isTimeout=true;
                                break;
                            }
                        }

                        if(isTimeout){
                            waitList.clear();
                        }

                        if (waitList.isEmpty()) {
                            current.set(elem.getKey());
                            LinkedList<Map.Entry<Long, E>> newList = new LinkedList<>();
                            newList.add(elem);
                            windowCount.incrementAndGet();
                            waitList.add(new SimpleEntry<>(newList, new SimpleEntry<>(elem.getKey(), new SimpleEntry<>(elemCount.get(), windowCount.get()))));
                        }



                        while (!waitList.isEmpty()) {
                            SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, SimpleEntry<Long, Long>>> first = waitList.getFirst();
                            if (first.getValue().getKey() + sessionTimeoutMillSeconds <= elem.getKey()) {
                                buff.add(new SimpleEntry<>(first.getKey(), first.getValue().getValue()));
                                waitList.removeFirst();
                            } else {
                                break;
                            }
                        }


                        if (!buff.isEmpty()) {
                            return Reference.of(buff);
                        } else {
                            return Reference.nop();
                        }
                    }

                    Collection<Map.Entry<List<Map.Entry<Long, E>>, Map.Entry<Long, Long>>> buff = new LinkedList<>();
                    while (!waitList.isEmpty()) {
                        SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, SimpleEntry<Long, Long>>> first = waitList.getFirst();
                        buff.add(new SimpleEntry<>(first.getKey(), first.getValue().getValue()));
                        waitList.removeFirst();
                    }

                    if (!buff.isEmpty()) {
                        return Reference.of(buff);
                    }

                    richAfter(this.holdIterator);
                    return Reference.finish();
                }
            });

        }), this);
    }

    @Override
    public Streaming<Map.Entry<List<Map.Entry<Long, E>>, Map.Entry<Long, Long>>> viewTimeWindow(long beforeMillSeconds, long afterMillSeconds) {
        return null;
    }
}
