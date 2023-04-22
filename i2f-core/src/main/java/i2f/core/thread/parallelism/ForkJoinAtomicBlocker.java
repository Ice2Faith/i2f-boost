package i2f.core.thread.parallelism;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2023/4/14 10:28
 * @desc
 */
public class ForkJoinAtomicBlocker implements ForkJoinPool.ManagedBlocker {
    private final AtomicBoolean flag = new AtomicBoolean(true);

    public static ForkJoinAtomicBlocker fork() throws InterruptedException {
        ForkJoinAtomicBlocker blocker = new ForkJoinAtomicBlocker();
        ForkJoinPool.managedBlock(blocker);
        return blocker;
    }

    public void begin() {
        flag.set(true);
    }

    public void done() {
        flag.set(false);
    }

    @Override
    public boolean block() throws InterruptedException {
        int maxCount = 100;
        while (!isReleasable()) {
            Thread.sleep(0);
            maxCount--;
            if (maxCount < 0) {
                break;
            }
        }
        return !isReleasable();
    }

    @Override
    public boolean isReleasable() {
        return !flag.get();
    }
}
