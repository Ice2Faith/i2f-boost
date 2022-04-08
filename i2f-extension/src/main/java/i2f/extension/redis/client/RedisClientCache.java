package i2f.extension.redis.client;

import i2f.core.cache.ICache;
import i2f.core.text.IFormatTextProcessor;
import i2f.extension.json.gson.GsonJsonProcessor;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/3/26 22:23
 * @desc
 */
public class RedisClientCache implements ICache<String> {
    private IFormatTextProcessor processor=new GsonJsonProcessor();
    @Override
    public Object set(String key, Object val) {
        try{
            return RedisUtil.set(key,processor.toText(val));
        }catch(Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }
    }

    @Override
    public boolean exists(String key) {
        return RedisUtil.hasKey(key);
    }

    @Override
    public Object get(String key) {
        return RedisUtil.get(key);
    }

    @Override
    public Object set(String key, long expire, TimeUnit timeUnit, Object val) {
        try{
            return RedisUtil.set(key,processor.toText(val),(int)timeUnit.toSeconds(expire));
        }catch(Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }
    }

    @Override
    public Object expire(String key, long expire, TimeUnit timeUnit) {
        return RedisUtil.updateKeyTimeout(key,(int)timeUnit.toSeconds(expire));
    }

    @Override
    public Object remove(String key) {
        return RedisUtil.del(key);
    }

    @Override
    public Set<String> keys() {
        return RedisUtil.keys("*");
    }

    @Override
    public Object clean() {
        return RedisUtil.clearRedis();
    }
}
