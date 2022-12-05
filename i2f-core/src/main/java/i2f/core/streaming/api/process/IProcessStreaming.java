package i2f.core.streaming.api.process;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

@FunctionalInterface
public interface IProcessStreaming<R, E> {
    Iterator<R> apply(Iterator<E> iterator, ExecutorService pool);

    default void create() {
        System.out.println(this.getClass().getSimpleName() + " create.");
    }

    default void destroy() {
        System.out.println(this.getClass().getSimpleName() + " destroy.");
    }
}
