package i2f.springboot.shiro;

import java.util.Set;

/**
 * @author ltb
 * @date 2022/4/21 16:26
 * @desc
 */
public interface IShiroUser {
    String getUsername();
    String getPassword();
    String getSalt();
    Set<String> getRoles();
    Set<String> getPermissions();
}
