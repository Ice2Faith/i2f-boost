package i2f.core.streaming.base.process;

import i2f.core.container.iterator.impl.LazyIterator;
import i2f.core.lang.functional.common.IFilter;
import i2f.core.streaming.base.AbsStreaming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class BeforeAllStreaming<E> extends AbsStreaming<E, E> {
    private IFilter<E> filter;

    public BeforeAllStreaming(IFilter<E> filter) {
        this.filter = filter;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            List<E> ret = new LinkedList<E>();
            boolean keepAlive = true;
            while (iterator.hasNext()) {
                E item = iterator.next();
                if (keepAlive && filter.test(item)) {
                    keepAlive = false;
                }
                if (keepAlive) {
                    ret.add(item);
                }
            }
            return ret.iterator();
        });
    }
}
