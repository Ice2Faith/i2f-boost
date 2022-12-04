package i2f.core.streaming.api.source;

import java.util.Iterator;

@FunctionalInterface
public interface ISourceStreaming<E> {
    Iterator<E> iterator();

    default void create() {
        System.out.println(this.getClass().getSimpleName() + " create.");
    }

    default void destroy() {
        System.out.println(this.getClass().getSimpleName() + " destroy.");
    }
}
