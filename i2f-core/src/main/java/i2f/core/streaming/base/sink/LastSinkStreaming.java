package i2f.core.streaming.base.sink;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class LastSinkStreaming<E> extends AbsSinkStreaming<E, E, E> {

    public LastSinkStreaming() {

    }

    @Override
    protected E sink(Iterator<E> iterator) {
        E ret = null;
        while (iterator().hasNext()) {
            ret = iterator.next();
        }
        return ret;
    }
}
