package i2f.core.streaming.base.process;

import i2f.core.container.iterator.impl.LazyIterator;
import i2f.core.lang.functional.jvf.BiSupplier;
import i2f.core.streaming.base.AbsStreaming;
import i2f.core.type.tuple.Tuples;
import i2f.core.type.tuple.impl.Tuple2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class KeyByStreaming<K, E> extends AbsStreaming<Tuple2<K, Collection<E>>, E> {

    public BiSupplier<K, E> key;

    public KeyByStreaming(BiSupplier<K, E> key) {
        this.key = key;
    }

    @Override
    public Iterator<Tuple2<K, Collection<E>>> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            Map<K, List<E>> map = new ConcurrentHashMap<>();
            parallelizeProcess(iterator, pool, (item, collector) -> {
                K k = key.get(item);
                System.out.println("k===" + k + ":" + item);
                if (!map.containsKey(k)) {
                    map.put(k, new LinkedList<>());
                }
                map.get(k).add(item);
            });

            List<Tuple2<K, Collection<E>>> ret = new LinkedList<>();
            for (Map.Entry<K, List<E>> item : map.entrySet()) {
                ret.add(Tuples.of(item.getKey(), item.getValue()));
            }

            return ret.iterator();
        });
    }
}
