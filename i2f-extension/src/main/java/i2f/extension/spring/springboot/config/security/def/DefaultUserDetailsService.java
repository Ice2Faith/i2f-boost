package i2f.extension.spring.springboot.config.security.def;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author ltb
 * @date 2022/4/7 10:56
 * @desc
 */
@ConditionalOnMissingBean(UserDetailsService.class)
@Slf4j
@Component
public class DefaultUserDetailsService implements UserDetailsService, InitializingBean {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("DefaultUserDetailsService load user:"+username);
        if("admin".equals(username)){
            UserDetails details=new User(username, passwordEncoder.encode("admin"), new ArrayList<GrantedAuthority>());
            return details;
        }
        throw new UsernameNotFoundException("user["+username+"] not found in system");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultUserDetailsService enable, default username:admin,password:admin");
    }
}
