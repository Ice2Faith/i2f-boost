package i2f.jce.std.encrypt;

/**
 * @author Ice2Faith
 * @date 2024/3/27 9:12
 * @desc
 */
public interface IEncryptor {
    byte[] encrypt(byte[] data) throws Exception;

    byte[] decrypt(byte[] data) throws Exception;
}
