package i2f.core.streaming.base.sink;

import i2f.core.streaming.AbsStreaming;
import i2f.core.thread.NamingForkJoinPool;
import i2f.core.tuple.Tuples;
import i2f.core.tuple.impl.Tuple2;

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

    protected Tuple2<Iterator, ExecutorService> result() {
        ExecutorService pool = null;
        boolean hasParallelOpen = false;
        Integer parallelCount = null;
        AbsStreaming curr = this;
        while (curr.prev != null) {
            if (curr.isParallel()) {
                hasParallelOpen = true;
            }
            if (parallelCount == null && curr.parallelize != null) {
                parallelCount = curr.parallelize;
            }
            curr = curr.prev;
        }

        if (hasParallelOpen) {
            if (parallelCount == null) {
                parallelCount = 0;
            }
            parallelCount = Math.max(parallelCount, Runtime.getRuntime().availableProcessors());
            pool = NamingForkJoinPool.getPool(parallelCount, "streaming", "task");
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
        return Tuples.of(after, pool);
    }

    public R sink() {
        Tuple2<Iterator, ExecutorService> tuple = result();
        Iterator rs = tuple.t1;
        ExecutorService pool = tuple.t2;
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
