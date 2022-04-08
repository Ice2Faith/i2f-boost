package i2f.extension.redis.inject.pool;

import i2f.core.cache.ICache;
import i2f.core.text.IFormatTextProcessor;
import i2f.extension.json.gson.GsonJsonProcessor;
import i2f.extension.redis.client.RedisUtil;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/3/26 22:23
 * @desc
 */
public class RedisPoolCache implements ICache<String> {
    private IFormatTextProcessor processor=new GsonJsonProcessor();
    private RedisPoolUtil redisPoolUtil;
    @Override
    public Object set(String key, Object val) {
        try{
            return redisPoolUtil.set(key,processor.toText(val));
        }catch(Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }
    }

    @Override
    public boolean exists(String key) {
        return redisPoolUtil.hasKey(key);
    }

    @Override
    public Object get(String key) {
        return redisPoolUtil.get(key);
    }

    @Override
    public Object set(String key, long expire, TimeUnit timeUnit, Object val) {
        try{
            return redisPoolUtil.set(key,processor.toText(val),(int)timeUnit.toSeconds(expire));
        }catch(Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }
    }

    @Override
    public Object expire(String key, long expire, TimeUnit timeUnit) {
        return redisPoolUtil.updateKeyTimeout(key,(int)timeUnit.toSeconds(expire));
    }

    @Override
    public Object remove(String key) {
        return redisPoolUtil.del(key);
    }

    @Override
    public Set<String> keys() {
        return redisPoolUtil.keys("*");
    }

    @Override
    public Object clean() {
        return redisPoolUtil.clearRedis();
    }
}
