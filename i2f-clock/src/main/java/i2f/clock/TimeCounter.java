package i2f.clock;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2024/3/25 14:06
 * @desc
 */
public class TimeCounter {
    protected AtomicLong beginTs=new AtomicLong();
    protected AtomicLong endTs=new AtomicLong();
    protected AtomicLong sumTs=new AtomicLong();
    protected AtomicLong count=new AtomicLong();
    protected AtomicLong maxTs=new AtomicLong();
    protected AtomicLong minTs=new AtomicLong();

    public TimeCounter(){
        clear();
        begin();
    }

    public TimeCounter clear(){
        beginTs.set(0);
        endTs.set(0);
        sumTs.set(0);
        count.set(0);
        maxTs.set(Long.MIN_VALUE);
        minTs.set(Long.MAX_VALUE);
        return this;
    }

    public TimeCounter begin(){
        long ts= SystemClock.currentTimeMillis();
        beginTs.set(ts);
        return this;
    }

    public long end(){
        long ts= SystemClock.currentTimeMillis();
        endTs.set(ts);

        long diff=endTs.get()-beginTs.get();
        sumTs.getAndAdd(diff);
        count.incrementAndGet();

        if(diff>maxTs.get()){
            maxTs.set(diff);
        }

        if(diff<minTs.get()){
            minTs.set(diff);
        }

        return diff;
    }

    public long diff(){
        return endTs.get()-beginTs.get();
    }

    public long sum(){
        return sumTs.get();
    }

    public long count(){
        return count.get();
    }

    public double avg(){
        return sumTs.get()*1.0/count.get();
    }

    public long max(){
        return maxTs.get();
    }

    public long min(){
        return minTs.get();
    }

    public String toCompareRateString(TimeCounter counter){
        return String.format("(this:other)={diff=%7.2f%%, avg=%7.2f%%, max=%7.2f%%, min=%7.2f%%, sum=%7.2f%%, count=%7.2f%%}",
                (this.diff()*1.0/(counter.diff())*100.0),
                (this.avg()*1.0/counter.avg()*100.0),
                (this.max()*1.0/counter.max()*100.0),
                (this.min()*1.0/counter.min()*100.0),
                (this.sum()*1.0/counter.sum()*100.0),
                (this.count()*1.0/counter.count()*100.0)
                );
    }

    @Override
    public String toString() {
        return String.format("TimeCounter{diff=%4d, avg=%7.2f, max=%4d, min=%4d, sum=%6d, count=%3d}",
                (endTs.get()-beginTs.get()),
                (sumTs.get()*1.0/count.get()),
                maxTs.get(),
                minTs.get(),
                sumTs.get(),
                count.get());
    }
}
