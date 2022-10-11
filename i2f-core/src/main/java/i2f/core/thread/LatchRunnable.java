package i2f.core.thread;

import lombok.Data;

import java.util.concurrent.CountDownLatch;

/**
 * @author ltb
 * @date 2022/8/6 0:02
 * @desc
 */
@Data
public abstract class LatchRunnable implements Runnable {
    protected CountDownLatch latch;
    protected Throwable thr;

    public LatchRunnable() {

    }

    public LatchRunnable(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public final void run() {
        try {
            doTask();
        } catch (Throwable e) {
            thr = e;
        } finally {
            if (latch != null) {
                latch.countDown();
            }
        }
    }

    public abstract void doTask() throws Exception;
}

