package i2f.core.streaming.base.sink;

import i2f.core.container.array.Arrays;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class ArraySinkStreaming<E> extends AbsSinkStreaming<E[], E, E> {

    public Class<E[]> tarType;

    public ArraySinkStreaming(Class<E[]> tarType) {
        this.tarType = tarType;
    }

    @Override
    protected E[] sink(Iterator<E> iterator, ExecutorService pool) {
        return Arrays.collect(iterator, tarType);
    }
}
