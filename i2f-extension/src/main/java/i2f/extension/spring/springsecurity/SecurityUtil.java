package i2f.extension.spring.springsecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @author ltb
 * @date 2022/3/30 14:59
 * @desc
 */
public class SecurityUtil {
    public static SecurityContext getSecurityContext(){
        return SecurityContextHolder.getContext();
    }
    public static Authentication getAuthentication(){
        return getSecurityContext().getAuthentication();
    }
    public static<T> T getPrincipal(){
        return (T)getAuthentication().getPrincipal();
    }
    public static Collection<? extends GrantedAuthority> getAuthorities(){
        return getAuthentication().getAuthorities();
    }
}
