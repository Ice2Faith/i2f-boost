package i2f.extension.spring.springboot.config.security.def.token;

import i2f.core.cache.ICache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/7 11:05
 * @desc
 */
@Slf4j
public abstract class AbstractTokenHolder {
    public static final String TOKEN_PREFIX="TOKEN_";
    public static final String TOKEN_USER_PREFIX="TKUSER_";

    protected abstract int getExpireTime();
    protected abstract TimeUnit getExpireTimeUnit();
    protected abstract ICache<String> getCache();

    public void setToken(String token, UserDetails obj){
        getCache().set(TOKEN_PREFIX+token,getExpireTime(),getExpireTimeUnit(),obj);
    }

    public UserDetails getToken(String token){
        return (UserDetails) getCache().get(TOKEN_PREFIX+token);
    }

    public Object refreshToken(String token){
        return getCache().expire(TOKEN_PREFIX+token,getExpireTime(),getExpireTimeUnit());
    }

    public Object removeToken(String token){
        return getCache().remove(TOKEN_PREFIX+token);
    }

    public void setSingleToken(String username,String token,UserDetails obj){
        String oldToken=(String)getCache().get(TOKEN_USER_PREFIX+username);
        if(oldToken!=null && !"".equals(oldToken)){
            removeToken(oldToken);
        }
        getCache().set(TOKEN_USER_PREFIX+username,token);
        setToken(token,obj);
    }

    public void removeSingleToken(String username,String token){
        String oldToken=(String)getCache().get(TOKEN_USER_PREFIX+username);
        if(oldToken!=null && !"".equals(oldToken)){
            removeToken(token);
        }
        getCache().remove(TOKEN_USER_PREFIX+username);
    }
}
