package i2f.springboot.shiro.impl;

import i2f.springboot.shiro.IShiroUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/4/21 16:15
 * @desc
 */
public abstract class BasicShiroUserRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        Iterator it= principal.iterator();
        IShiroUser user=null;
        while(it.hasNext()){
            Object obj=it.next();
            if(obj instanceof IShiroUser){
                user=(IShiroUser)obj;
                break;
            }
        }
        if(user==null){
            return null;
        }
        SimpleAuthorizationInfo authorizes=new SimpleAuthorizationInfo();
        authorizes.setRoles(user.getRoles());
        authorizes.setStringPermissions(user.getPermissions());
        return authorizes;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        IShiroUser user=findShiroUser(auth);
        if(user==null){
            throw new UnknownAccountException("account is not exists.");
        }
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user,user.getPassword(), ByteSource.Util.bytes(user.getSalt()),getName());
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return isSupportToken(token);
    }

    public abstract boolean isSupportToken(AuthenticationToken token);

    public abstract IShiroUser findShiroUser(AuthenticationToken auth) throws AuthenticationException;
}
