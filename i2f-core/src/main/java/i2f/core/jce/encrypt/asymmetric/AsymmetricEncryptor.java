package i2f.core.jce.encrypt.asymmetric;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:00
 * @desc
 */
public interface AsymmetricEncryptor {
    byte[] publicEncrypt(byte[] data) throws Exception;

    byte[] publicDecrypt(byte[] data) throws Exception;

    byte[] privateEncrypt(byte[] data) throws Exception;

    byte[] privateDecrypt(byte[] data) throws Exception;
}
