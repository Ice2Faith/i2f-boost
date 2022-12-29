package i2f.core.streaming.base.sink;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.stream.Collector;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class CollectorSinkStreaming<R, A, E> extends AbsSinkStreaming<R, E, E> {
    public Collector<E, A, R> collector;

    public CollectorSinkStreaming(Collector<E, A, R> collector) {
        this.collector = collector;
    }

    @Override
    protected R sink(Iterator<E> iterator, ExecutorService pool) {
        A container = collector.supplier().get();
        BiConsumer<A, E> accumulator = collector.accumulator();
        while (iterator.hasNext()) {
            E item = iterator.next();
            accumulator.accept(container, item);
        }
        return collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)
                ? (R) container
                : collector.finisher().apply(container);
    }
}
