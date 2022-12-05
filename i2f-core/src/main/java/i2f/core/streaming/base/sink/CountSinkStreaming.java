package i2f.core.streaming.base.sink;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class CountSinkStreaming<E> extends AbsSinkStreaming<Integer, E, E> {

    public CountSinkStreaming() {

    }

    @Override
    protected Integer sink(Iterator<E> iterator, ExecutorService pool) {
        int cnt = 0;
        while (iterator.hasNext()) {
            E item = iterator.next();
            cnt++;
        }
        return cnt;
    }
}
