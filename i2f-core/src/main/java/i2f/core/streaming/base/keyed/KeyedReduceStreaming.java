package i2f.core.streaming.base.keyed;

import i2f.core.functional.jvf.BiFunction;
import i2f.core.functional.jvf.BiSupplier;
import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.base.AbsStreaming;
import i2f.core.tuple.Tuples;
import i2f.core.tuple.impl.Tuple2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class KeyedReduceStreaming<K, E> extends AbsStreaming<Tuple2<K, E>, E> {
    private BiSupplier<K, E> key;
    private BiFunction<E, E, E> reducer;

    public KeyedReduceStreaming(BiSupplier<K, E> key, BiFunction<E, E, E> reducer) {
        this.key = key;
        this.reducer = reducer;
    }

    @Override
    public Iterator<Tuple2<K, E>> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            List<Tuple2<K, E>> ret = new LinkedList<Tuple2<K, E>>();
            Map<K, E> map = new ConcurrentHashMap<>();
            parallelizeProcess(iterator, pool, (item, collector) -> {
                K k = key.get(item);
                if (!map.containsKey(k)) {
                    map.put(k, null);
                }
                item = reducer.get(map.get(k), item);
                map.put(k, item);
            });

            for (Map.Entry<K, E> item : map.entrySet()) {
                ret.add(Tuples.of(item.getKey(), item.getValue()));
            }
            return ret.iterator();
        });
    }
}
