package i2f.core.security.jce.encrypt.std.asymmetric;

import javax.crypto.Cipher;

/**
 * @author ltb
 * @date 2022/6/8 10:15
 * @desc
 */
public interface IAsymmetricCipherProvider {
    Cipher getEncryptCipher(boolean isPrivate) throws Exception;

    Cipher getDecryptCipher(boolean isPrivate) throws Exception;
}
