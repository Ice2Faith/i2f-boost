package i2f.core.streaming.base.sink;

import i2f.core.streaming.AbsStreaming;
import i2f.core.thread.NamingForkJoinPool;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:08
 * @desc
 */
public abstract class AbsSinkStreaming<R, M, E> extends AbsStreaming<M, E> {
    @Override
    public Iterator<M> apply(Iterator<E> iterator, ExecutorService pool) {
        return null;
    }

    protected Iterator result(ExecutorService pool) {
        AbsStreaming curr = this;
        while (curr.prev != null) {
            curr = curr.prev;
        }

        AbsStreaming node = curr;
        while (node != null) {
            if (node.parallel == null) {
                if (node.prev != null) {
                    if (node.prev.parallel != null) {
                        node.parallel = node.prev.parallel;
                    }
                }
            }
            node = node.next;
        }

        AbsStreaming head = curr;
        head.create();
        Iterator after = head.apply(null, head.isParallel() ? pool : null);
        curr = head.next;
        while (curr.next != null) {
            curr.create();
            after = curr.apply(after, curr.isParallel() ? pool : null);
            curr = curr.next;
        }
        return after;
    }

    public R sink() {
        int parallelizeCount = Runtime.getRuntime().availableProcessors();
        int multiFactor = 3;
        ExecutorService pool = NamingForkJoinPool.getPool(parallelizeCount * multiFactor, "streaming", "task");
        Iterator rs = result(pool);
        this.create();
        R ret = sink((Iterator<M>) rs, this.isParallel() ? pool : null);
        AbsStreaming curr = this;
        while (curr != null) {
            curr.destroy();
            curr = curr.prev;
        }
        return ret;
    }

    protected abstract R sink(Iterator<M> iterator, ExecutorService pool);
}
