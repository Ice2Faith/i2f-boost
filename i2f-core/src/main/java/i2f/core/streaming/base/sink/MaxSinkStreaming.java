package i2f.core.streaming.base.sink;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class MaxSinkStreaming<E> extends AbsSinkStreaming<E, E, E> {
    private Comparator<E> comparator;

    public MaxSinkStreaming(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    protected E sink(Iterator<E> iterator, ExecutorService pool) {
        E ret = null;
        boolean first = false;
        while (iterator.hasNext()) {
            E item = iterator.next();
            if (first) {
                ret = item;
            } else {
                if (comparator.compare(item, ret) > 0) {
                    ret = item;
                }
            }
            first = false;
        }
        return ret;
    }
}
