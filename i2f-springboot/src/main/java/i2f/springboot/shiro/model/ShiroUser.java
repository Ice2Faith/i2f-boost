package i2f.springboot.shiro.model;

import i2f.springboot.shiro.IShiroUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/4/21 17:38
 * @desc
 */
@Data
@NoArgsConstructor
public class ShiroUser implements IShiroUser, Serializable {

    private String username;

    private String password;

    private String salt;

    private Set<String> roles=new HashSet<>();

    private Set<String> permissions=new HashSet<>();

}
