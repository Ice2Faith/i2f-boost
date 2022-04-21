package i2f.springboot.shiro.token;

import i2f.springboot.shiro.IShiroUser;
import i2f.springboot.shiro.impl.BasicShiroUserRealm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;

/**
 * @author ltb
 * @date 2022/4/21 16:15
 * @desc
 */
public abstract class CustomerTokenRealm extends BasicShiroUserRealm {

    @Override
    public boolean isSupportToken(AuthenticationToken token) {
        return token!=null && token instanceof CustomerAuthToken;
    }

    @Override
    public IShiroUser findShiroUser(AuthenticationToken auth) throws AuthenticationException {
        CustomerAuthToken authToken=(CustomerAuthToken)auth;
        String token= authToken.getToken();
        IShiroUser user= getShiroUser(token);
        if(user==null){
            throw new UnknownAccountException("account token is not exists.");
        }
        return user;
    }

    protected abstract IShiroUser getShiroUser(String token) throws AuthenticationException;
}
