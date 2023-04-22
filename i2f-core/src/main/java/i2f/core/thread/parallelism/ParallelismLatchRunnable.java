package i2f.core.thread.parallelism;

import i2f.core.thread.LatchRunnable;
import lombok.Data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2023/4/14 10:51
 * @desc
 */
@Data
public abstract class ParallelismLatchRunnable extends LatchRunnable {
    protected final AtomicInteger runCount = new AtomicInteger(0);
    protected final AtomicInteger submitCount = new AtomicInteger(0);
    protected final AtomicInteger parallelism = new AtomicInteger(0);

    public ParallelismLatchRunnable() {
    }

    public ParallelismLatchRunnable(CountDownLatch latch) {
        super(latch);
    }
}
