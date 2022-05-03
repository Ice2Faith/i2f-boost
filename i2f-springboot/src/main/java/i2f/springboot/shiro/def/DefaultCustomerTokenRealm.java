package i2f.springboot.shiro.def;

import i2f.springboot.shiro.IShiroUser;
import i2f.springboot.shiro.ShiroConfig;
import i2f.springboot.shiro.token.AbstractShiroTokenHolder;
import i2f.springboot.shiro.token.CustomerTokenRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/4/21 18:32
 * @desc
 */
@ConditionalOnBean(ShiroConfig.class)
@ConditionalOnMissingBean(CustomerTokenRealm.class)
@Component
@Slf4j
public class DefaultCustomerTokenRealm extends CustomerTokenRealm implements InitializingBean {

    @Autowired
    private AbstractShiroTokenHolder tokenHolder;

    @Override
    protected IShiroUser getShiroUser(String token) throws AuthenticationException {
        IShiroUser user=tokenHolder.getToken(token);
        if(user!=null){
            return user;
        }
        throw new AuthenticationException("invalid token.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultCustomerTokenRealm config done.");
    }
}
