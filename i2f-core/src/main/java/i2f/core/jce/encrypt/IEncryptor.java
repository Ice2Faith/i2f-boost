package i2f.core.jce.encrypt;

/**
 * @author ltb
 * @date 2022/6/8 9:00
 * @desc
 */
public interface IEncryptor {
    byte[] encrypt(byte[] data) throws Exception;
    byte[] decrypt(byte[] data) throws Exception;
}
