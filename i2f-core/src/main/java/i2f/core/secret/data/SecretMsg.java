package i2f.core.secret.data;

import i2f.core.secret.util.SecretUtil;
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
        ret.randomKey = SecretUtil.toBase64(randomKey);
        ret.nonce = SecretUtil.toBase64(nonce);
        ret.signature = SecretUtil.toBase64(signature);
        ret.msg = SecretUtil.toBase64(msg);
        ret.publicKey = SecretUtil.toBase64(publicKey);
        return ret;
    }
}
