package i2f.extension.spring.springboot.config.security.def.token;

import i2f.extension.spring.springboot.config.security.impl.token.AbstractAuthenticationTokenFilter;
import i2f.extension.spring.springboot.config.security.impl.token.AuthenticationTokenFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ltb
 * @date 2022/4/7 9:38
 * @desc
 */
@ConditionalOnMissingBean(AuthenticationTokenFilter.class)
@Slf4j
@Component
public  class DefaultAuthenticationTokenFilter extends AbstractAuthenticationTokenFilter {

    @Autowired
    protected AbstractTokenHolder tokenHolder;

    @Override
    protected UserDetails getUserDetailByToken(String token, HttpServletRequest request, HttpServletResponse response) {
        log.info("DefaultAuthenticationTokenFilter to find token...");
        UserDetails user= (UserDetails) tokenHolder.getToken(token);
        log.info("DefaultAuthenticationTokenFilter token find:"+user);
        tokenHolder.refreshToken(token);
        return user;
    }
}
