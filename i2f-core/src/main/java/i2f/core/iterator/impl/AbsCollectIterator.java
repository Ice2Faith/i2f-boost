package i2f.core.iterator.impl;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class AbsCollectIterator<T> implements Iterator<T> {
    private volatile LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

    public void collect(T elem) {
        queue.add(elem);
    }

    protected boolean isEnding() {
        return false;
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty() || !isEnding();
    }

    @Override
    public T next() {
        try {
            return queue.take();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
