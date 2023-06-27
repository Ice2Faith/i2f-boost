package i2f.core.security.jce.encrypt.std.symmetric;

import i2f.core.security.jce.codec.CodecUtil;
import i2f.core.security.jce.codec.compress.IByteByteCodec;
import i2f.core.security.jce.codec.exception.CodecException;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:00
 * @desc
 */
public interface SymmetricEncryptor extends IByteByteCodec {
    byte[] encrypt(byte[] data) throws Exception;

    byte[] decrypt(byte[] data) throws Exception;

    @Override
    default byte[] encode(byte[] data) {
        try {
            return encrypt(data);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    default byte[] decode(byte[] enc) {
        try {
            return decrypt(enc);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    default String encryptAsBase64(byte[] data) throws Exception {
        byte[] enc = encrypt(data);
        return CodecUtil.toBase64(enc);
    }

    default byte[] decryptByBase64(String enc) throws Exception {
        byte[] data = CodecUtil.ofBase64(enc);
        return decrypt(data);
    }
}
