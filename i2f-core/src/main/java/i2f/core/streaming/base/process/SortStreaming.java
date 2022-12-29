package i2f.core.streaming.base.process;

import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.AbsStreaming;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class SortStreaming<E> extends AbsStreaming<E, E> {
    private Comparator<E> comparator;

    public SortStreaming(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            List<E> ret = new LinkedList<E>();
            while (iterator.hasNext()) {
                E item = iterator.next();
                ret.add(item);
            }
            ret.sort(comparator);
            return ret.iterator();
        });
    }
}
