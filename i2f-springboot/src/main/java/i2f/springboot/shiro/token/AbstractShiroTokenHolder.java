package i2f.springboot.shiro.token;

import i2f.core.cache.ICache;
import i2f.springboot.shiro.IShiroUser;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/7 11:05
 * @desc
 */
@Slf4j
public abstract class AbstractShiroTokenHolder {
    public static final String TOKEN_PREFIX="SHIRO_TOKEN_";
    public static final String TOKEN_USER_PREFIX="SHIRO_TKUSER_";

    public String getTokenPrefix(){
        return TOKEN_PREFIX;
    }
    public String getTokenUserPrefix(){
        return TOKEN_USER_PREFIX;
    }

    protected abstract int getExpireTime();
    protected abstract TimeUnit getExpireTimeUnit();
    protected abstract ICache<String> getCache();

    public void setToken(String token, IShiroUser obj){
        getCache().set(getTokenPrefix()+token,getExpireTime(),getExpireTimeUnit(),obj);
    }

    public IShiroUser getToken(String token){
        return (IShiroUser) getCache().get(getTokenPrefix()+token);
    }

    public Object refreshToken(String token){
        return getCache().expire(getTokenPrefix()+token,getExpireTime(),getExpireTimeUnit());
    }

    public Object removeToken(String token){
        return getCache().remove(getTokenPrefix()+token);
    }

    public void setSingleToken(String username,String token,IShiroUser obj){
        String oldToken=(String)getCache().get(getTokenUserPrefix()+username);
        if(oldToken!=null && !"".equals(oldToken)){
            removeToken(oldToken);
        }
        getCache().set(getTokenUserPrefix()+username,token);
        setToken(token,obj);
    }

    public void removeSingleToken(String username,String token){
        String oldToken=(String)getCache().get(getTokenUserPrefix()+username);
        if(oldToken!=null && !"".equals(oldToken)){
            removeToken(token);
        }
        getCache().remove(getTokenUserPrefix()+username);
    }
}
