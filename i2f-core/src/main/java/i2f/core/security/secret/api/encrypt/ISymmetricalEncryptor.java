package i2f.core.security.secret.api.encrypt;

/**
 * @author ltb
 * @date 2022/10/19 14:53
 * @desc symmetrical 对称的
 */
public interface ISymmetricalEncryptor extends IEncryptor {
    byte[] encryptKey(byte[] data, byte[] key);

    byte[] decryptKey(byte[] data, byte[] key);
}
