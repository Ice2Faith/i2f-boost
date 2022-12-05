package i2f.core.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCountDownLatch {
    private volatile AtomicInteger count = new AtomicInteger(0);

    public void count() {
        this.count.incrementAndGet();
    }

    public void down() {
        this.count.decrementAndGet();
    }

    public long await() throws InterruptedException {
        long btime = System.currentTimeMillis();
        while (true) {
            Thread.sleep(1);
            if (this.count.get() <= 0) {
                break;
            }
        }
        long etime = System.currentTimeMillis();
        return etime - btime;
    }
}
