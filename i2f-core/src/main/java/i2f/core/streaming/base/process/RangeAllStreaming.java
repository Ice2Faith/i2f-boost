package i2f.core.streaming.base.process;

import i2f.core.functional.common.IFilter;
import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class RangeAllStreaming<E> extends AbsStreaming<E, E> {
    private IFilter<E> beginFilter;
    private IFilter<E> endFilter;

    public RangeAllStreaming(IFilter<E> beginFilter, IFilter<E> endFilter) {
        this.beginFilter = beginFilter;
        this.endFilter = endFilter;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator) {
        List<E> ret = new LinkedList<E>();
        boolean keepAlive = false;
        while (iterator.hasNext()) {
            E item = iterator.next();
            if (!keepAlive && beginFilter.test(item)) {
                keepAlive = true;
            }
            if (keepAlive && endFilter.test(item)) {
                keepAlive = false;
            }
            if (keepAlive) {
                ret.add(item);
            }
        }
        return ret.iterator();
    }
}
