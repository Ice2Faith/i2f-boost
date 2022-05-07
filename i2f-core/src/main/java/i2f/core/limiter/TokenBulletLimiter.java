package i2f.core.limiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ltb
 * @date 2022/5/6 18:02
 * @desc 令牌桶限流
 * 令牌每隔一定时间产生，直到达到最大令牌数量
 * 消费一次拿走一个令牌，当没有令牌可拿时，触发限流
 */
public class TokenBulletLimiter implements ILimiter{
    protected int productTime;
    protected TimeUnit productTimeUnit;
    protected int maxTokenCount;
    protected ScheduledExecutorService pool;
    protected ConcurrentHashMap<String,AtomicInteger> counters=new ConcurrentHashMap<>();

    public TokenBulletLimiter(int productTime,TimeUnit productTimeUnit){
        this.productTime=productTime;
        this.productTimeUnit=productTimeUnit;
        this.maxTokenCount=Integer.MAX_VALUE;
        this.pool= Executors.newSingleThreadScheduledExecutor();
        this.init();
    }

    public TokenBulletLimiter(int productTime,TimeUnit productTimeUnit,int maxTokenCount){
        this.productTime=productTime;
        this.productTimeUnit=productTimeUnit;
        this.maxTokenCount=maxTokenCount;
        this.pool= Executors.newSingleThreadScheduledExecutor();
        this.init();
    }

    public TokenBulletLimiter(int productTime,TimeUnit productTimeUnit,int maxTokenCount,ScheduledExecutorService pool){
        this.productTime=productTime;
        this.productTimeUnit=productTimeUnit;
        this.maxTokenCount=maxTokenCount;
        this.pool=pool;
        this.init();
    }

    public void init(){
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    for(Map.Entry<String,AtomicInteger> item : counters.entrySet()){
                        if(item.getValue().get()<maxTokenCount){
                            item.getValue().incrementAndGet();
                        }
                    }
                }
            }
        },0,productTime,productTimeUnit);
    }

    @Override
    public boolean hasLimit(String key, Object... args) {
        if(!counters.containsKey(key)){
            return false;
        }
        return counters.get(key).get()<=0;
    }

    @Override
    public void limit(String key, Object... args) {
        synchronized (this){
            if(!counters.containsKey(key)){
                counters.put(key,new AtomicInteger(0));
            }
            if(counters.get(key).get()>0){
                counters.get(key).decrementAndGet();
            }
        }
    }

    @Override
    public void unlimited(String key, Object... args) {
        synchronized (this){
            if(!counters.containsKey(key)){
                return;
            }
            counters.remove(key);
        }
    }
}
