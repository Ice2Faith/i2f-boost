package i2f.core.streaming.base.sink;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class CollectSinkStreaming<R extends Collection<M>, M, E> extends AbsSinkStreaming<R, M, E> {
    public R col;

    public CollectSinkStreaming(R col) {
        this.col = col;
    }

    @Override
    protected R sink(Iterator<M> iterator) {
        while (iterator.hasNext()) {
            M item = iterator.next();
            col.add(item);
        }
        return col;
    }
}
