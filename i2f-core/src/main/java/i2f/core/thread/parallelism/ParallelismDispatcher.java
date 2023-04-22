package i2f.core.thread.parallelism;

import i2f.core.thread.LatchRunnable;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author Ice2Faith
 * @date 2023/4/14 10:26
 * @desc
 */
public class ParallelismDispatcher {

    public static <E> ParallelismLatchRunnable dispatch(ExecutorService pool, Collection<E> collection, int parallel, ParallelismTaskRunner<E> runner, Object... args) {
        return dispatch(pool, collection.iterator(), collection.size(), parallel, runner, args);
    }

    public static <E> ParallelismLatchRunnable dispatch(ExecutorService pool, Iterator<E> iterator, int count, int parallel, ParallelismTaskRunner<E> runner, Object... args) {
        ParallelismLatchRunnable ret = new ParallelismLatchRunnable(new CountDownLatch(1)) {
            @Override
            public void doTask() throws Exception {
                CountDownLatch latch = null;
                if (count > 0) {
                    latch = new CountDownLatch(count);
                }
                runCount.set(0);
                submitCount.set(0);
                parallelism.set(0);
                while (true) {
                    int curr = parallelism.get();
                    if (curr < parallel) {
                        while (iterator.hasNext()) {
                            curr = parallelism.incrementAndGet();
                            E elem = iterator.next();
                            int scnt = submitCount.incrementAndGet();
                            pool.submit(new LatchRunnable(latch) {
                                @Override
                                public void doTask() throws Exception {
                                    try {
                                        runner.run(elem, args);
                                    } finally {
                                        int rcnt = runCount.incrementAndGet();
                                        int dcurr = parallelism.decrementAndGet();
                                    }
                                }
                            });
                            if (curr >= parallel) {
                                break;
                            }
                        }

                    }
                    if (!iterator.hasNext()) {
                        break;
                    }
                    try {
                        Thread.sleep(30);
                    } catch (Exception e) {
                    }
                }

                if (latch != null) {
                    latch.await();
                }
            }
        };

        return ret;
    }
}
