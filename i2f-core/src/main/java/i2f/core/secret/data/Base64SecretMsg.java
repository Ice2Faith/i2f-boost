package i2f.core.secret.data;

import i2f.core.secret.util.SecretUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/10/19 19:05
 * @desc
 */
@Data
@NoArgsConstructor
public class Base64SecretMsg {
    public String randomKey;
    public String nonce;
    public String signature;
    public String msg;
    public String publicKey;

    public i2f.core.secret.data.SecretMsg convert() {
        i2f.core.secret.data.SecretMsg ret = new i2f.core.secret.data.SecretMsg();
        ret.randomKey = SecretUtil.parseBase64(randomKey);
        ret.nonce = SecretUtil.parseBase64(nonce);
        ret.signature = SecretUtil.parseBase64(signature);
        ret.msg = SecretUtil.parseBase64(msg);
        ret.publicKey = SecretUtil.parseBase64(publicKey);

        return ret;
    }
}
