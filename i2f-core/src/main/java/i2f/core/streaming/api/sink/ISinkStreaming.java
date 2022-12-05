package i2f.core.streaming.api.sink;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

@FunctionalInterface
public interface ISinkStreaming<R, E> {
    R sink(Iterator<E> iterator, ExecutorService pool);

    default void create() {
        System.out.println(this.getClass().getSimpleName() + " create.");
    }

    default void destroy() {
        System.out.println(this.getClass().getSimpleName() + " destroy.");
    }
}
