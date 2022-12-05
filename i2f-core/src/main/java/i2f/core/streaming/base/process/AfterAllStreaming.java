package i2f.core.streaming.base.process;

import i2f.core.functional.common.IFilter;
import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class AfterAllStreaming<E> extends AbsStreaming<E, E> {
    private IFilter<E> filter;

    public AfterAllStreaming(IFilter<E> filter) {
        this.filter = filter;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        List<E> ret = new LinkedList<E>();
        boolean keepAlive = false;
        while (iterator.hasNext()) {
            E item = iterator.next();
            if (!keepAlive && filter.test(item)) {
                keepAlive = true;
            }
            if (keepAlive) {
                ret.add(item);
            }
        }
        return ret.iterator();
    }
}