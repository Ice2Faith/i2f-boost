package i2f.springcloud.spring.gateway.filters.global;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2024/4/1 17:28
 * @desc
 */
public class TimeStatistic {
    protected volatile AtomicLong curr=new AtomicLong(0);
    protected volatile AtomicLong count=new AtomicLong(0);
    protected volatile AtomicLong sum=new AtomicLong(0);
    protected volatile AtomicLong max=new AtomicLong(Long.MIN_VALUE);
    protected volatile AtomicLong min=new AtomicLong(Long.MAX_VALUE);

    public TimeStatistic reset(){
        curr.set(0);
        count.set(0);
        sum.set(0);
        max.set(Long.MIN_VALUE);
        min.set(Long.MAX_VALUE);
        return this;
    }

    public TimeStatistic add(long diff){
        curr.set(diff);
        count.incrementAndGet();
        sum.addAndGet(diff);
        if(diff>max.get()){
            max.set(diff);
        }
        if(diff<min.get()){
            min.set(diff);
        }
        return this;
    }

    public AtomicLong getCount() {
        return count;
    }

    public void setCount(AtomicLong count) {
        this.count = count;
    }

    public AtomicLong getSum() {
        return sum;
    }

    public void setSum(AtomicLong sum) {
        this.sum = sum;
    }

    public AtomicLong getMax() {
        return max;
    }

    public void setMax(AtomicLong max) {
        this.max = max;
    }

    public AtomicLong getMin() {
        return min;
    }

    public void setMin(AtomicLong min) {
        this.min = min;
    }

    public String formatString(){
        return String.format("curr: %5d, avg: %8.2f, min: %5d, max: %5d, count: %5d",
                curr.get(),(sum.get()*1.0/count.get()),min.get(),max.get(),count.get());
    }

    @Override
    public String toString() {
        return "TimeStatistic{" +
                "count=" + count +
                ", sum=" + sum +
                ", max=" + max +
                ", min=" + min +
                '}';
    }
}
