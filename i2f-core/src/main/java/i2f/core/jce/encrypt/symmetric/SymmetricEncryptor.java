package i2f.core.jce.encrypt.symmetric;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:00
 * @desc
 */
public interface SymmetricEncryptor {
    byte[] encrypt(byte[] data) throws Exception;

    byte[] decrypt(byte[] data) throws Exception;
}
