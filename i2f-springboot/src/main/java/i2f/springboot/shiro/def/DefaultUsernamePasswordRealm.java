package i2f.springboot.shiro.def;

import i2f.springboot.shiro.HashedCredentialsMatcherEncoder;
import i2f.springboot.shiro.IShiroUser;
import i2f.springboot.shiro.impl.UsernamePasswordRealm;
import i2f.springboot.shiro.model.ShiroUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/4/21 17:37
 * @desc
 */
@ConditionalOnMissingBean(UsernamePasswordRealm.class)
@Slf4j
@Component
@ConfigurationProperties("i2f.springboot.config.shiro.default")
public class DefaultUsernamePasswordRealm extends UsernamePasswordRealm implements InitializingBean {

    private String users="admin/admin=ROLE_admin";

    private Map<String, ShiroUser> userMap=new ConcurrentHashMap<>();

    @Autowired
    private HashedCredentialsMatcherEncoder matcherEncoder;

    @Override
    protected IShiroUser getShiroUser(String username) {
        return userMap.get(username);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultUsernamePasswordRealm config done.");
        if(users==null || "".equals(users)){
            return;
        }

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
            Set<String> roles=new HashSet<>();
            Set<String> permissions=new HashSet<>();
            for(String item : uauths){
                auths.add(item);
                if(item.startsWith("ROLE_")){
                    roles.add(item.substring("ROLE_".length()));
                }else{
                    permissions.add(item);
                }
            }

            ShiroUser shiroUser=new ShiroUser();
            shiroUser.setUsername(username);
            shiroUser.setPassword(password);
            shiroUser.setSalt(username);
            shiroUser.setRoles(roles);
            shiroUser.setPermissions(permissions);
            userMap.put(username,shiroUser);
            log.info("DefaultUsernamePasswordRealm find user:"+username);

            // 使用颜值hash密码，以便后续登录
            shiroUser.setPassword(matcherEncoder.encode(shiroUser.getPassword(),shiroUser.getSalt()));
        }
    }
}
