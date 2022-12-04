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
public class FilterStreaming<E> extends AbsStreaming<E, E> {
    private IFilter<E> filter;

    public FilterStreaming(IFilter<E> filter) {
        this.filter = filter;
    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator) {
        List<E> ret = new LinkedList<E>();
        while (iterator.hasNext()) {
            E item = iterator.next();
            if (filter.test(item)) {
                ret.add(item);
            }
        }
        return ret.iterator();
    }
}
