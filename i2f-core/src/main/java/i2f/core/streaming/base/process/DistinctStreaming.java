package i2f.core.streaming.base.process;

import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class DistinctStreaming<E> extends AbsStreaming<E, E> {

    public DistinctStreaming() {

    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator) {
        Set<E> ret = new LinkedHashSet<E>();
        while (iterator.hasNext()) {
            E item = iterator.next();
            ret.add(item);
        }
        return ret.iterator();
    }
}
