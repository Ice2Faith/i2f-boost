package i2f.stream.thread;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCountDownLatch {
    private volatile AtomicInteger count = new AtomicInteger(0);
    private final AtomicBoolean finish=new AtomicBoolean(false);

    public void begin(){
        this.count.set(0);
        this.finish.set(false);
    }

    public void finish(){
        this.finish.set(true);
    }

    public boolean finished(){
        return this.finish.get();
    }

    public int current(){
        return this.count.get();
    }

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
            if(this.finish.get()){
                if (this.count.get() <= 0) {
                    break;
                }
            }
        }
        long etime = System.currentTimeMillis();
        return etime - btime;
    }
}
