package i2f.core.streaming.base.process;

import i2f.core.functional.common.IMapper;
import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class MapStreaming<E, R> extends AbsStreaming<R, E> {
    private IMapper<R, E> mapper;

    public MapStreaming(IMapper<R, E> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Iterator<R> apply(Iterator<E> iterator, ExecutorService pool) {
        return parallelizeProcess(iterator, pool, (item, collector) -> {
            collector.add(mapper.get(item));
        });
    }
}
