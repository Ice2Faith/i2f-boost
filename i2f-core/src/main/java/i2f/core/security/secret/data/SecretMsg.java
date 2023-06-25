package i2f.core.security.secret.data;

import i2f.core.security.secret.util.SecretUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/10/19 15:19
 * @desc
 */
@Data
@NoArgsConstructor
public class SecretMsg extends SecretHeader {
    public byte[] msg;
    public byte[] publicKey;

    public Base64SecretMsg convert() {
        Base64SecretMsg ret = new Base64SecretMsg();
        if (randomKey != null) {
            ret.randomKey = SecretUtil.escapeBase64(SecretUtil.toBase64(randomKey));
        }
        if (nonce != null) {
            ret.nonce = SecretUtil.escapeBase64(SecretUtil.toBase64(nonce));
        }
        if (signature != null) {
            ret.signature = SecretUtil.escapeBase64(SecretUtil.toBase64(signature));
        }
        if (msg != null) {
            ret.msg = SecretUtil.toBase64(msg);
        }
        if (publicKey != null) {
            ret.publicKey = SecretUtil.escapeBase64(SecretUtil.toBase64(publicKey));
        }
        return ret;
    }
}
