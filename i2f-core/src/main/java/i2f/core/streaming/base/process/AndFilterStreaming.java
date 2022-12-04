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
public class AndFilterStreaming<E> extends AbsStreaming<E, E> {
    private IFilter<E>[] filters;

    public AndFilterStreaming(IFilter<E>... filters) {
        this.filters = filters;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator) {
        if (filters == null || filters.length == 0) {
            return iterator;
        }
        List<E> ret = new LinkedList<E>();
        while (iterator.hasNext()) {
            E item = iterator.next();
            boolean ok = true;
            for (IFilter<E> filter : filters) {
                if (!filter.test(item)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                ret.add(item);
            }
        }
        return ret.iterator();
    }
}
