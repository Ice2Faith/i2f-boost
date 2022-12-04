package i2f.springboot.shiro.def;

import i2f.core.cache.ICache;
import i2f.core.cache.impl.mem.MemCache;
import i2f.springboot.shiro.ShiroConfig;
import i2f.springboot.shiro.token.AbstractShiroTokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/7 11:05
 * @desc
 */
@ConditionalOnBean(ShiroConfig.class)
@ConditionalOnMissingBean(AbstractShiroTokenHolder.class)
@Slf4j
@Component
public class DefaultShiroTokenHolder extends AbstractShiroTokenHolder {
    public static final int EXPIRE_TIME=30;
    public static final TimeUnit EXPIRE_TIME_UNIT=TimeUnit.MINUTES;

    protected ICache<String, Object> cache = new MemCache<>();

    @Override
    protected int getExpireTime() {
        return EXPIRE_TIME;
    }

    @Override
    protected TimeUnit getExpireTimeUnit() {
        return EXPIRE_TIME_UNIT;
    }

    @Override
    protected ICache<String, Object> getCache() {
        return cache;
    }
}
