package i2f.core.security.secret.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/10/19 15:19
 * @desc
 */
@Data
@NoArgsConstructor
public class SecretHeader {
    public byte[] randomKey;
    public byte[] nonce;
    public byte[] signature;
}
