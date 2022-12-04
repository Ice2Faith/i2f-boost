package i2f.core.streaming.base.sink;

import i2f.core.functional.common.IFilter;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class LastMatchSinkStreaming<E> extends AbsSinkStreaming<E, E, E> {

    private IFilter<E> filter;

    public LastMatchSinkStreaming(IFilter<E> filter) {
        this.filter = filter;
    }

    @Override
    protected E sink(Iterator<E> iterator) {
        E ret = null;
        while (iterator().hasNext()) {
            E item = iterator.next();
            if (filter.test(item)) {
                ret = item;
            }
        }
        return ret;
    }
}
