package i2f.core.streaming.iterator;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:34
 * @desc
 */
public class SyncQueueIterator<E> implements Iterator<E> {
    private final LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<>();
    private final AtomicInteger count = new AtomicInteger(0);
    private final AtomicBoolean finish = new AtomicBoolean(false);
    private final AtomicBoolean first = new AtomicBoolean(true);

    public SyncQueueIterator() {
    }

    public void finish() {
        this.finish.set(true);
    }

    public void require(boolean first) {

    }

    public void check() {
        require(first.get());
        first.set(false);
    }

    public int count() {
        return count.get();
    }

    public synchronized void put(E elem) {
        try {
            queue.put(elem);
            count.incrementAndGet();
        } catch (Exception e) {
            throw new IllegalStateException("sync queue iterator put error : " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasNext() {
        check();
        if (count.get() > 0) {
            return true;
        }
        if (!this.finish.get()) {
            while (!this.finish.get()) {
                try {
                    Thread.sleep(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (this.count.get() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public synchronized E next() {
        try {
            count.decrementAndGet();
            E elem = queue.take();
            return elem;
        } catch (Exception e) {
            throw new IllegalStateException("sync queue iterator take error : " + e.getMessage(), e);
        }
    }

}
