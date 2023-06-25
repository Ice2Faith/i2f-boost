package i2f.core.security.secret.data;

import i2f.core.security.secret.api.key.IKeyPair;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/10/19 15:45
 * @desc
 */
@Data
@NoArgsConstructor
public class SecretKeyPair implements IKeyPair {
    public byte[] publicKey;
    public byte[] privateKey;

    public SecretKeyPair(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public SecretKeyPair(byte[] publicKey, byte[] privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public byte[] publicKey() {
        return publicKey;
    }

    @Override
    public byte[] privateKey() {
        return privateKey;
    }
}
