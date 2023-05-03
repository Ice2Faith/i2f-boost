package i2f.core.streaming.keyed.process;

import i2f.core.streaming.parallel.AtomicCountDownLatch;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:13
 * @desc
 */
public abstract class KeyedStreamingProcessCollectParallelWrapper<K, IN, OUT> extends KeyedStreamingProcessCollectWrapper<K, IN, OUT> {
    @Override
    public void collect(K key, Iterator<IN> iterator, Consumer<OUT> consumer) {
        if (getContext().isParallel()) {
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            while (iterator.hasNext()) {
                IN val = iterator.next();
                getContext().getPool().submit(() -> {
                    try {
                        handle(key, val, consumer);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        latch.down();
                    }
                });
            }
            latch.finish();
            try {
                latch.await();
            } catch (Exception e) {
                throw new IllegalStateException("streaming parallel await error : " + e.getMessage(), e);
            }
        } else {
            while (iterator.hasNext()) {
                IN val = iterator.next();
                handle(key, val, consumer);
            }
        }
    }

    public abstract void handle(K key, IN elem, Consumer<OUT> consumer);
}
