package i2f.springboot.security.def;

import i2f.springboot.security.model.SecurityGrantedAuthority;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private String users="admin/admin=ROLE_admin";

    private Map<String,String> userMap=new HashMap<>();
    private Map<String, List<String>> userAuthorities=new HashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("DefaultUserDetailsService load user:"+username);
        if(userMap.containsKey(username)){
            List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
            List<String> userAuths=userAuthorities.get(username);
            if(userAuths!=null){
                for(String item : userAuths){
                    authorities.add(new SecurityGrantedAuthority(item));
                }
            }
            UserDetails details=new SecurityUser(username, passwordEncoder.encode(userMap.get(username)), authorities);
            return details;
        }
        throw new UsernameNotFoundException("user["+username+"] not found in system");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] userArr=users.split(";");
        for(String user : userArr){
            String[] userGroup=user.split("=",2);
            String userNamePass=userGroup[0];
            String userAuthoritys=userGroup.length>1?userGroup[1]:"";
            String[] userNamePassArr=userNamePass.split("/",2);
            String username=userNamePassArr[0];
            String password=userNamePassArr.length>1?userNamePassArr[1]:"";

            List<String> auths=new ArrayList<>();
            String[] uauths=userAuthoritys.split(",");
            for(String item : uauths){
                auths.add(item);
            }
            userMap.put(username,password);
            userAuthorities.put(username,auths);
            log.info("DefaultUserDetailsService find user:"+username);
        }
        log.info("DefaultUserDetailsService enable,config done.");
    }
}
