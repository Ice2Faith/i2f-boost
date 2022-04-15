package i2f.springboot.security.def;

import i2f.springboot.security.model.SecurityUser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
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
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "i2f.springboot.config.security.default-impl-login")
public class DefaultUserDetailsService implements UserDetailsService, InitializingBean {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    private String username="admin";
    private String password="admin";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("DefaultUserDetailsService load user:"+username);
        if(username.equals(username)){
            UserDetails details=new SecurityUser(username, passwordEncoder.encode(password), new ArrayList<GrantedAuthority>());
            return details;
        }
        throw new UsernameNotFoundException("user["+username+"] not found in system");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultUserDetailsService enable, default username:admin,password:admin");
    }
}
