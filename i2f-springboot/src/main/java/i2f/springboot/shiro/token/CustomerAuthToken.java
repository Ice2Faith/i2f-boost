package i2f.springboot.shiro.token;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author ltb
 * @date 2022/4/21 18:19
 * @desc
 */
@Data
@NoArgsConstructor
public class CustomerAuthToken implements AuthenticationToken {
    private String token;
    public CustomerAuthToken(String token){
        this.token=token;
    }
    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    public String getToken(){
        return token;
    }
}
