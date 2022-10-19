package i2f.core.secret.api.encrypt;

import i2f.core.secret.api.key.IKeyPair;

/**
 * @author ltb
 * @date 2022/10/19 14:53
 * @desc asymmetrical 非对称的
 */
public interface IAsymmetricalEncryptor extends IEncryptor {
    byte[] encrypt(byte[] data, IKeyPair key);

    byte[] decrypt(byte[] data, IKeyPair key);
}
