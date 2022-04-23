package i2f.springboot.shiro.def;

import i2f.springboot.shiro.IShiroUser;
import i2f.springboot.shiro.token.CustomerTokenRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/4/21 18:32
 * @desc
 */
@ConditionalOnMissingBean(CustomerTokenRealm.class)
@Component
@Slf4j
public class DefaultCustomerTokenRealm extends CustomerTokenRealm implements InitializingBean {
    public static volatile Map<String,IShiroUser> onlineUser=new ConcurrentHashMap<>();
    @Override
    protected IShiroUser getShiroUser(String token) throws AuthenticationException {
        if(onlineUser.containsKey(token)){
            return onlineUser.get(token);
        }
        throw new AuthenticationException("invalid token.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultCustomerTokenRealm config done.");
    }
}
