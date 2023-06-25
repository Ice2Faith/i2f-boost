package i2f.core.security.jce.encrypt.std.symmetric;

import i2f.core.security.jce.codec.CodecUtil;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:00
 * @desc
 */
public interface SymmetricEncryptor {
    byte[] encrypt(byte[] data) throws Exception;

    byte[] decrypt(byte[] data) throws Exception;

    default String encryptAsBase64(byte[] data) throws Exception {
        byte[] enc = encrypt(data);
        return CodecUtil.toBase64(enc);
    }

    default byte[] decryptByBase64(String enc) throws Exception {
        byte[] data = CodecUtil.parseBase64(enc);
        return decrypt(data);
    }
}
