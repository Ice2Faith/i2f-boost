package i2f.springboot.shiro.impl;

import i2f.springboot.shiro.IShiroUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author ltb
 * @date 2022/4/21 16:15
 * @desc
 */
public abstract class UsernamePasswordRealm extends BasicShiroUserRealm {

    @Override
    public boolean isSupportToken(AuthenticationToken token) {
        return token!=null && token instanceof UsernamePasswordToken;
    }

    @Override
    public IShiroUser findShiroUser(AuthenticationToken auth) throws AuthenticationException {
        UsernamePasswordToken token=(UsernamePasswordToken)auth;
        String username= token.getUsername();
        IShiroUser user= getShiroUser(username);
        if(user==null){
            throw new UnknownAccountException("account ["+username+"] is not exists.");
        }
        return user;
    }

    protected abstract IShiroUser getShiroUser(String username) throws AuthenticationException;
}
