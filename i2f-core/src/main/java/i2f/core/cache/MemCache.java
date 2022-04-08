package i2f.core.cache;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.notice.Nullable;
import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/3/22 19:52
 * @desc
 */
@Author("i2f")
@Remark("base on memory cache implement using ConcurrentHashMap")
public class MemCache<T> implements ICache<T>{
    @Name("cacheData")
    @Remark("key:cacheKey,val:cacheValue")
    protected volatile ConcurrentHashMap<T,Object> cacheData=new ConcurrentHashMap<>();
    @Name("cacheExpire")
    @Remark("key:cacheKey,val:expireArriveTimestamp")
    protected volatile ConcurrentHashMap<T,Long> cacheExpire=new ConcurrentHashMap<>();

    protected volatile ScheduledExecutorService pool= Executors.newSingleThreadScheduledExecutor();

    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                pool.shutdownNow();
            }
        }));
        pool.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                cleanExpire();
            }
        },30,30,TimeUnit.SECONDS);
    }

    public Map<T, Object> getCacheData(){
        return cacheData;
    }
    public Map<T,Long> getCacheExpire(){
        return cacheExpire;
    }

    public void cleanExpire(){
        Set<T> keys=new HashSet<>();
        for(Map.Entry<T,Long> item : cacheExpire.entrySet()){
            if(System.currentTimeMillis()>item.getValue()){
                keys.add(item.getKey());
            }
        }
        for(T item : keys){
            cacheExpire.remove(item);
            cacheData.remove(item);
        }
    }

    @Override
    public Object set(@Name("key") T key, @Name("val") @Nullable Object val) {
        cacheData.put(key, val);
        return key;
    }

    @Override
    public boolean exists(@Name("key") T key) {
        if(cacheExpire.containsKey(key)){
            Long arriveTime=cacheExpire.get(key);
            long time=System.currentTimeMillis();
            if(time>arriveTime){
                cacheExpire.remove(key);
                return false;
            }else{
                return true;
            }
        }else{
            boolean ret=cacheData.containsKey(key);
            if(!ret){
                cacheExpire.remove(key);
            }
            return ret;
        }
    }

    @Override
    public Object get(@Name("key") T key) {
        if(exists(key)){
            return cacheData.get(key);
        }
        return null;
    }

    @Override
    public Object set(@Name("key")T key, @Name("expire") long expire, @Name("timeUint") TimeUnit timeUnit, @Name("value") @Nullable Object val) {
        long milli=timeUnit.toMillis(expire);
        cacheData.put(key,val);
        cacheExpire.put(key,System.currentTimeMillis()+milli);
        return key;
    }

    @Override
    public Object expire(@Name("key")T key, @Name("expire")long expire,  @Name("timeUint") TimeUnit timeUnit) {
        if(exists(key)){
            long milli=timeUnit.toMillis(expire);
            cacheExpire.put(key,System.currentTimeMillis()+milli);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Set<T> keys(){
        Set<T> ret=new HashSet<>();
        Iterator<T> it=cacheData.keySet().iterator();
        while(it.hasNext()){
            T item = it.next();
            if(exists(item)){
                ret.add(item);
            }
        }
        return ret;
    }

    @Override
    public Object remove(@Name("key")T key) {
        Object ret=null;
        if(exists(key)){
            ret=get(key);
        }
        cacheData.remove(key);
        cacheExpire.remove(key);
        return ret;
    }

    @Override
    public Object clean() {
        int ret=cacheData.size();
        cacheData.clear();
        cacheExpire.clear();
        return ret;
    }
}
