package i2f.core.jce.encrypt;

import javax.crypto.Cipher;

/**
 * @author ltb
 * @date 2022/6/8 10:15
 * @desc
 */
public interface IChiperProvider {
    Cipher getEncryptCipher() throws Exception;
    Cipher getDecryptCipher() throws Exception;
}
