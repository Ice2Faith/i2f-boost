package i2f.core.streaming.base.process;

import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.base.AbsStreaming;
import i2f.core.tuple.Tuples;
import i2f.core.tuple.impl.Tuple2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class CountByStreaming<E> extends AbsStreaming<Tuple2<E, Integer>, E> {

    public CountByStreaming() {

    }

    @Override
    public Iterator<Tuple2<E, Integer>> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            Map<E, Integer> map = new ConcurrentHashMap<>();
            AtomicInteger nullCount = new AtomicInteger(0);
            parallelizeProcess(iterator, pool, (item, collector) -> {
                if (item == null) {
                    nullCount.incrementAndGet();
                } else {
                    if (!map.containsKey(item)) {
                        map.put(item, 0);
                    }
                    map.put(item, map.get(item) + 1);
                }
            });

            List<Tuple2<E, Integer>> ret = new LinkedList<>();
            for (Map.Entry<E, Integer> item : map.entrySet()) {
                ret.add(Tuples.of(item.getKey(), item.getValue()));
            }
            if (nullCount.get() > 0) {
                ret.add(Tuples.<E, Integer>of(null, nullCount.get()));
            }
            return ret.iterator();
        });
    }
}
