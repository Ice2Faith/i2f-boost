package i2f.springboot.shiro;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @author ltb
 * @date 2022/4/21 17:59
 * @desc
 */
@Data
@NoArgsConstructor
public class HashedCredentialsMatcherEncoder {
    private HashedCredentialsMatcher matcher;
    public HashedCredentialsMatcherEncoder(HashedCredentialsMatcher matcher){
        this.matcher=matcher;
    }
    public String encode(Object data,Object salt){
        return new SimpleHash(matcher.getHashAlgorithmName(),data,salt, matcher.getHashIterations()).toString();
    }
}
