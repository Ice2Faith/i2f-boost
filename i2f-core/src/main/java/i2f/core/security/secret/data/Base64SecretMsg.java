package i2f.core.security.secret.data;

import i2f.core.security.secret.util.SecretUtil;
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

    public SecretMsg convert() {
        SecretMsg ret = new SecretMsg();
        if (randomKey != null) {
            ret.randomKey = SecretUtil.parseBase64(SecretUtil.descapeBase64(randomKey));
        }
        if (nonce != null) {
            ret.nonce = SecretUtil.parseBase64(SecretUtil.descapeBase64(nonce));
        }
        if (signature != null) {
            ret.signature = SecretUtil.parseBase64(SecretUtil.descapeBase64(signature));
        }
        if (msg != null) {
            ret.msg = SecretUtil.parseBase64(msg);
        }
        if (publicKey != null) {
            ret.publicKey = SecretUtil.parseBase64(SecretUtil.descapeBase64(publicKey));
        }

        return ret;
    }
}
