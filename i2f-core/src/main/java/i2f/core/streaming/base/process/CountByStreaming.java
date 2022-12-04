package i2f.core.streaming.base.process;

import i2f.core.streaming.AbsStreaming;
import i2f.core.tuple.Tuples;
import i2f.core.tuple.impl.Tuple2;

import java.util.*;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class CountByStreaming<E> extends AbsStreaming<Tuple2<E, Integer>, E> {

    public CountByStreaming() {

    }

    @Override
    public Iterator<Tuple2<E, Integer>> apply(Iterator<E> iterator) {
        Map<E, Integer> map = new LinkedHashMap<>();
        Integer nullCount = 0;
        while (iterator.hasNext()) {
            E item = iterator.next();
            if (item == null) {
                nullCount++;
            } else {
                if (!map.containsKey(item)) {
                    map.put(item, 0);
                }
                map.put(item, map.get(item) + 1);
            }
        }
        List<Tuple2<E, Integer>> ret = new LinkedList<>();
        for (Map.Entry<E, Integer> item : map.entrySet()) {
            ret.add(Tuples.of(item.getKey(), item.getValue()));
        }
        if (nullCount > 0) {
            ret.add(Tuples.<E, Integer>of(null, nullCount));
        }
        return ret.iterator();
    }
}
