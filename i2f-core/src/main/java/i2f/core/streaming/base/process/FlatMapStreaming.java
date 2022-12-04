package i2f.core.streaming.base.process;

import i2f.core.functional.jvf.BiConsumer;
import i2f.core.streaming.AbsStreaming;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class FlatMapStreaming<E, R> extends AbsStreaming<R, E> {
    private BiConsumer<E, Collection<R>> mapper;

    public FlatMapStreaming(BiConsumer<E, Collection<R>> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Iterator<R> apply(Iterator<E> iterator) {
        List<R> ret = new LinkedList<R>();
        while (iterator.hasNext()) {
            E item = iterator.next();
            mapper.accept(item, ret);
        }
        return ret.iterator();
    }
}
