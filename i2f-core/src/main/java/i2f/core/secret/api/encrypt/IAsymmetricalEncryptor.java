package i2f.core.secret.api.encrypt;

import i2f.core.secret.api.key.IKeyPair;

/**
 * @author ltb
 * @date 2022/10/19 14:53
 * @desc asymmetrical 非对称的
 */
public interface IAsymmetricalEncryptor extends IEncryptor {
    byte[] encryptKey(byte[] data, IKeyPair key);

    byte[] decryptKey(byte[] data, IKeyPair key);

    byte[] encryptPublicKey(byte[] data, IKeyPair key);

    byte[] decryptPrivateKey(byte[] data, IKeyPair key);
}
