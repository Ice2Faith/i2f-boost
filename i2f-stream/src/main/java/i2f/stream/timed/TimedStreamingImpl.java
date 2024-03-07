package i2f.stream.timed;

import i2f.stream.Streaming;
import i2f.stream.impl.*;
import i2f.stream.window.TimeWindowInfo;
import i2f.stream.window.ViewTimeWindowInfo;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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
    public Streaming<Map.Entry<List<Map.Entry<Long, E>>, TimeWindowInfo>> slideTimeWindow(long windowMillSeconds, long slideMillSeconds) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {

            LinkedList<SimpleEntry<LinkedList<Map.Entry<Long, E>>, TimeWindowInfo>> waitList = new LinkedList<>();
            AtomicLong elementCount = new AtomicLong(0L);
            AtomicLong windowCount = new AtomicLong(0L);
            AtomicLong submitWindowCount = new AtomicLong(0L);
            AtomicLong current = new AtomicLong(0);

            return new SupplierBufferIterator<Map.Entry<List<Map.Entry<Long, E>>, TimeWindowInfo>>(() -> {
                synchronized (waitList) {
                    while (this.holdIterator.hasNext()) {
                        Map.Entry<Long, E> elem = this.holdIterator.next();
                        elementCount.incrementAndGet();

                        for (SimpleEntry<LinkedList<Map.Entry<Long, E>>, TimeWindowInfo> entry : waitList) {
                            if (entry.getValue().getWindowEndTime() > elem.getKey()) {
                                entry.getKey().add(elem);
                                entry.getValue().setRealEndTime(elem.getKey());
                            }
                        }

                        boolean isNew = false;
                        if (elementCount.get() == 1) {
                            current.set(elem.getKey());
                            LinkedList<Map.Entry<Long, E>> newList = new LinkedList<>();
                            newList.add(elem);
                            windowCount.incrementAndGet();

                            TimeWindowInfo info=new TimeWindowInfo();
                            info.setElementCount(elementCount.get());
                            info.setWindowCount(windowCount.get());
                            info.setSubmitWindowCount(submitWindowCount.get());
                            info.setWindowBeginTime(current.get());
                            info.setWindowEndTime(current.get()+windowMillSeconds);
                            info.setRealEndTime(elem.getKey());
                            waitList.add(new SimpleEntry<>(newList, info));
                            isNew = true;
                        }


                        if (elem.getKey() >= current.get() + slideMillSeconds) {
                            current.getAndAdd(slideMillSeconds);

                            if (!isNew) {
                                current.set(elem.getKey());
                                LinkedList<Map.Entry<Long, E>> newList = new LinkedList<>();
                                newList.add(elem);
                                windowCount.incrementAndGet();

                                TimeWindowInfo info=new TimeWindowInfo();
                                info.setElementCount(elementCount.get());
                                info.setWindowCount(windowCount.get());
                                info.setSubmitWindowCount(submitWindowCount.get());
                                info.setWindowBeginTime(current.get());
                                info.setWindowEndTime(current.get()+windowMillSeconds);
                                info.setRealEndTime(elem.getKey());
                                waitList.add(new SimpleEntry<>(newList, info));
                            }
                        }


                        Collection<Map.Entry<List<Map.Entry<Long, E>>, TimeWindowInfo>> buff = new LinkedList<>();
                        while (!waitList.isEmpty()) {
                            SimpleEntry<LinkedList<Map.Entry<Long, E>>, TimeWindowInfo> first = waitList.getFirst();
                            if (first.getValue().getWindowEndTime() <= elem.getKey()) {
                                submitWindowCount.incrementAndGet();
                                first.getValue().setSubmitWindowCount(submitWindowCount.get());
                                buff.add(new SimpleEntry<>(first.getKey(), first.getValue()));
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

                    Collection<Map.Entry<List<Map.Entry<Long, E>>, TimeWindowInfo>> buff = new LinkedList<>();
                    while (!waitList.isEmpty()) {
                        SimpleEntry<LinkedList<Map.Entry<Long, E>>, TimeWindowInfo> first = waitList.getFirst();
                        submitWindowCount.incrementAndGet();
                        first.getValue().setSubmitWindowCount(submitWindowCount.get());
                        buff.add(new SimpleEntry<>(first.getKey(), first.getValue()));
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
    public Streaming<Map.Entry<List<Map.Entry<Long, E>>, TimeWindowInfo>> sessionTimeWindow(long sessionTimeoutMillSeconds) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            LinkedList<SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, TimeWindowInfo>>> waitList = new LinkedList<>();
            AtomicLong elementCount = new AtomicLong(0L);
            AtomicLong windowCount = new AtomicLong(0L);
            AtomicLong submitWindowCount = new AtomicLong(0L);
            AtomicLong current = new AtomicLong(0);

            return new SupplierBufferIterator<Map.Entry<List<Map.Entry<Long, E>>, TimeWindowInfo>>(() -> {
                synchronized (waitList) {
                    while (this.holdIterator.hasNext()) {
                        Map.Entry<Long, E> elem = this.holdIterator.next();
                        elementCount.incrementAndGet();

                        boolean isTimeout=false;
                        Collection<Map.Entry<List<Map.Entry<Long, E>>, TimeWindowInfo>> buff = new LinkedList<>();
                        for (SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, TimeWindowInfo>> entry : waitList) {
                            if (entry.getValue().getKey() + sessionTimeoutMillSeconds >= elem.getKey()) {
                                entry.getValue().setKey(elem.getKey());
                                entry.getKey().add(elem);

                                entry.getValue().getValue().setWindowEndTime(entry.getValue().getKey() + sessionTimeoutMillSeconds);
                                entry.getValue().getValue().setRealEndTime(elem.getKey());
                            } else {
                                submitWindowCount.incrementAndGet();
                                entry.getValue().getValue().setSubmitWindowCount(submitWindowCount.get());
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

                            TimeWindowInfo info=new TimeWindowInfo();
                            info.setElementCount(elementCount.get());
                            info.setWindowCount(windowCount.get());
                            info.setSubmitWindowCount(submitWindowCount.get());
                            info.setWindowBeginTime(current.get());
                            info.setWindowEndTime(current.get()+sessionTimeoutMillSeconds);
                            info.setRealEndTime(elem.getKey());
                            waitList.add(new SimpleEntry<>(newList, new SimpleEntry<>(elem.getKey(), info)));
                        }



                        while (!waitList.isEmpty()) {
                            SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, TimeWindowInfo>> first = waitList.getFirst();
                            if (first.getValue().getKey() + sessionTimeoutMillSeconds < elem.getKey()) {
                                submitWindowCount.incrementAndGet();
                                first.getValue().getValue().setSubmitWindowCount(submitWindowCount.get());
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

                    Collection<Map.Entry<List<Map.Entry<Long, E>>, TimeWindowInfo>> buff = new LinkedList<>();
                    while (!waitList.isEmpty()) {
                        SimpleEntry<LinkedList<Map.Entry<Long, E>>, SimpleEntry<Long, TimeWindowInfo>> first = waitList.getFirst();
                        submitWindowCount.incrementAndGet();
                        first.getValue().getValue().setSubmitWindowCount(submitWindowCount.get());
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
    public Streaming<Map.Entry<List<Map.Entry<Long, E>>, ViewTimeWindowInfo>> viewTimeWindow(long beforeMillSeconds, long afterMillSeconds) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {

            LinkedList<SimpleEntry<LinkedList<Map.Entry<Long, E>>,ViewTimeWindowInfo>> waitList = new LinkedList<>();

            AtomicLong elementCount = new AtomicLong(0L);
            AtomicLong windowCount = new AtomicLong(0L);
            AtomicLong submitWindowCount = new AtomicLong(0L);
            LinkedList<Map.Entry<Long, E>> beforeList = new LinkedList<>();

            return new SupplierBufferIterator<Map.Entry<List<Map.Entry<Long, E>>,ViewTimeWindowInfo>>(() -> {
                while (this.holdIterator.hasNext()) {
                    Map.Entry<Long, E> elem = this.holdIterator.next();
                    elementCount.incrementAndGet();

                    for (SimpleEntry<LinkedList<Map.Entry<Long, E>>, ViewTimeWindowInfo> entry : waitList) {
                        if(entry.getValue().getWindowEndTime()>=elem.getKey()){
                            entry.getKey().add(elem);
                            entry.getValue().setRealEndTime(elem.getKey());
                        }
                    }

                    while(!beforeList.isEmpty()){
                        Map.Entry<Long, E> first = beforeList.getFirst();
                        if(first.getKey()<elem.getKey()-beforeMillSeconds){
                            beforeList.removeFirst();
                        }else{
                            break;
                        }
                    }

                    LinkedList<Map.Entry<Long,E>> nextList=new LinkedList<>();
                    nextList.addAll(beforeList);
                    nextList.add(elem);
                    windowCount.incrementAndGet();

                    ViewTimeWindowInfo info=new ViewTimeWindowInfo();
                    info.setElementCount(elementCount.get());
                    info.setWindowCount(windowCount.get());
                    info.setSubmitWindowCount(submitWindowCount.get());
                    info.setWindowBeginTime(elem.getKey()-beforeMillSeconds);
                    info.setWindowEndTime(elem.getKey()+afterMillSeconds);
                    info.setRealBeginTime(nextList.getFirst().getKey());
                    info.setRealEndTime(elem.getKey());
                    waitList.add(new SimpleEntry<>(nextList,info));

                    Collection<Map.Entry<List<Map.Entry<Long, E>>, ViewTimeWindowInfo>> buff=new LinkedList<>();
                    while(!waitList.isEmpty()){
                        SimpleEntry<LinkedList<Map.Entry<Long, E>>, ViewTimeWindowInfo> first = waitList.getFirst();
                        if(first.getValue().getWindowEndTime()<elem.getKey()){
                            submitWindowCount.incrementAndGet();
                            first.getValue().setSubmitWindowCount(submitWindowCount.get());
                            buff.add(new SimpleEntry<>(first.getKey(),first.getValue()));
                            waitList.removeFirst();
                        }else{
                            break;
                        }
                    }

                    beforeList.add(elem);

                    if(!buff.isEmpty()){
                        return Reference.of(buff);
                    }else{
                        return Reference.nop();
                    }
                }

                Collection<Map.Entry<List<Map.Entry<Long, E>>, ViewTimeWindowInfo>> buff=new LinkedList<>();
                while(!waitList.isEmpty()){
                    SimpleEntry<LinkedList<Map.Entry<Long, E>>, ViewTimeWindowInfo> first = waitList.getFirst();
                    submitWindowCount.incrementAndGet();
                    first.getValue().setSubmitWindowCount(submitWindowCount.get());
                    buff.add(new SimpleEntry<>(first.getKey(),first.getValue()));
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
    public <T, R> Streaming<Map.Entry<Map.Entry<Long, E>, R>> latelyAggregateTime(long latelyMillSeconds, Supplier<T> firstSupplier, BiFunction<T, E, T> accumulator, Function<T, R> finisher) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {

            LinkedList<Map.Entry<Long,E>> beforeList=new LinkedList<>();
            AtomicLong elemCount=new AtomicLong(0L);

            return new SupplierIterator<Map.Entry<Map.Entry<Long,E>,R>>(() -> {
                while (this.holdIterator.hasNext()) {
                    Map.Entry<Long,E> elem = this.holdIterator.next();
                    elemCount.incrementAndGet();

                    while(!beforeList.isEmpty()){
                        Map.Entry<Long, E> first = beforeList.getFirst();
                        if(first.getKey()<elem.getKey()-latelyMillSeconds){
                            beforeList.removeFirst();
                        }else{
                            break;
                        }
                    }

                    T cvt = firstSupplier.get();
                    for (Map.Entry<Long,E> item : beforeList) {
                        cvt=accumulator.apply(cvt,item.getValue());
                    }
                    cvt = accumulator.apply(cvt, elem.getValue());


                    beforeList.add(elem);

                    while(!beforeList.isEmpty()){
                        Map.Entry<Long, E> first = beforeList.getFirst();
                        if(first.getKey()<elem.getKey()-latelyMillSeconds){
                            beforeList.removeFirst();
                        }else{
                            break;
                        }
                    }

                    return Reference.of(new SimpleEntry<>(elem, finisher.apply(cvt)));
                }

                richAfter(this.holdIterator);
                return Reference.finish();
            });

        }), this);
    }
}
