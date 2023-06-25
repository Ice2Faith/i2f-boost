package i2f.core.streaming.base.sink;

import i2f.core.lang.functional.jvf.BiSupplier;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class CollectMapSinkStreaming<K, V, MAP extends Map<K, V>, E> extends AbsSinkStreaming<MAP, E, E> {
    public MAP map;
    public BiSupplier<K, E> key;
    public BiSupplier<V, E> val;

    public CollectMapSinkStreaming(MAP map, BiSupplier<K, E> key, BiSupplier<V, E> val) {
        this.map = map;
        this.key = key;
        this.val = val;
    }

    @Override
    protected MAP sink(Iterator<E> iterator, ExecutorService pool) {
        while (iterator.hasNext()) {
            E item = iterator.next();
            K k = key.get(item);
            V v = val.get(item);
            map.put(k, v);
        }
        return map;
    }
}
