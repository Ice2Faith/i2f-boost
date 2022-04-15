package i2f.springboot.security.def.token;

import i2f.core.cache.ICache;
import i2f.springboot.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/7 11:05
 * @desc
 */
@Slf4j
public class RedisTokenHolder extends AbstractTokenHolder implements InitializingBean {
    public static final int EXPIRE_TIME=30;
    public static final TimeUnit EXPIRE_TIME_UNIT=TimeUnit.MINUTES;

    @Autowired
    protected RedisCache redisCache;

    protected ICache<String> cache;

    @Override
    protected int getExpireTime() {
        return EXPIRE_TIME;
    }

    @Override
    protected TimeUnit getExpireTimeUnit() {
        return EXPIRE_TIME_UNIT;
    }

    @Override
    protected ICache<String> getCache() {
        return cache;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cache=redisCache;
        log.info("RedisTokenHolder config done.");
    }
}
