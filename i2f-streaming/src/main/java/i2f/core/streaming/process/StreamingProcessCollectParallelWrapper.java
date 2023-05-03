package i2f.core.streaming.process;

import i2f.core.streaming.parallel.AtomicCountDownLatch;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:13
 * @desc
 */
public abstract class StreamingProcessCollectParallelWrapper<IN, OUT> extends StreamingProcessCollectWrapper<IN, OUT> {
    @Override
    public void collect(Iterator<IN> iterator, Consumer<OUT> consumer) {
        if (getContext().isParallel()) {
            AtomicCountDownLatch latch = new AtomicCountDownLatch();
            while (iterator.hasNext()) {
                IN val = iterator.next();
                getContext().getPool().submit(() -> {
                    try {
                        handle(val, consumer);
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
                handle(val, consumer);
            }
        }
    }

    public abstract void handle(IN elem, Consumer<OUT> consumer);
}
