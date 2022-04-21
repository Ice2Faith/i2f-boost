package i2f.springboot.shiro.def;

import i2f.springboot.shiro.IShiroUser;
import i2f.springboot.shiro.token.CustomerTokenRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;

/**
 * @author ltb
 * @date 2022/4/21 18:32
 * @desc
 */
@Slf4j
public class DefaultCustomerTokenRealm extends CustomerTokenRealm {
    @Override
    protected IShiroUser getShiroUser(String token) throws AuthenticationException {
        return null;
    }
}
