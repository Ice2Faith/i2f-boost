package i2f.core.secret.api.encrypt;

/**
 * @author ltb
 * @date 2022/10/19 15:13
 * @desc
 */
public interface IEncryptor {
    byte[] encrypt(byte[] data);

    byte[] decrypt(byte[] data);
}
