package i2f.core.streaming.base;

import i2f.core.functional.jvf.BiSupplier;
import i2f.core.streaming.AbsStreaming;
import i2f.core.tuple.Tuples;
import i2f.core.tuple.impl.Tuple2;

import java.util.*;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class KeyedAggregateStreaming<K, E, RT> extends AbsStreaming<Tuple2<K, RT>, E> {
    private BiSupplier<K, E> key;
    private BiSupplier<RT, Iterator<E>> mapper;

    public KeyedAggregateStreaming(BiSupplier<K, E> key, BiSupplier<RT, Iterator<E>> mapper) {
        this.key = key;
        this.mapper = mapper;
    }

    @Override
    public Iterator<Tuple2<K, RT>> apply(Iterator<E> iterator) {
        List<Tuple2<K, RT>> ret = new LinkedList<Tuple2<K, RT>>();
        HashMap<K, List<E>> map = new HashMap<>();
        while (iterator.hasNext()) {
            E item = iterator.next();
            K k = key.get(item);
            if (!map.containsKey(k)) {
                map.put(k, new LinkedList<E>());
            }
            map.get(k).add(item);
        }
        for (Map.Entry<K, List<E>> item : map.entrySet()) {
            RT val = mapper.get(item.getValue().iterator());
            ret.add(Tuples.of(item.getKey(), val));
        }
        return ret.iterator();
    }
}
