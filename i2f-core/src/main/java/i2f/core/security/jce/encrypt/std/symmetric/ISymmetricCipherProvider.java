package i2f.core.security.jce.encrypt.std.symmetric;

import javax.crypto.Cipher;

/**
 * @author ltb
 * @date 2022/6/8 10:15
 * @desc
 */
public interface ISymmetricCipherProvider {
    Cipher getEncryptCipher() throws Exception;

    Cipher getDecryptCipher() throws Exception;
}
