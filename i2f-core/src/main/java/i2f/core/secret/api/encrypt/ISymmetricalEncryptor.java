package i2f.core.secret.api.encrypt;

/**
 * @author ltb
 * @date 2022/10/19 14:53
 * @desc symmetrical 对称的
 */
public interface ISymmetricalEncryptor extends IEncryptor {
    byte[] encrypt(byte[] data, byte[] key);

    byte[] decrypt(byte[] data, byte[] key);
}
