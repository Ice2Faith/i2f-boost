package i2f.core.jce.encrypt.symmetric.basic;

import i2f.core.jce.encrypt.CipherUtil;
import i2f.core.jce.encrypt.CipherWorker;
import i2f.core.jce.encrypt.IChiperProvider;
import i2f.core.jce.encrypt.IEncryptType;
import i2f.core.jce.encrypt.symmetric.SymmetricEncryptor;

import javax.crypto.Cipher;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc 基本加解密器，使用SecretKeySpec作为秘钥生成器
 */
public class BasicEncryptor implements SymmetricEncryptor, IChiperProvider {

    protected IEncryptType type;
    protected String providerName;
    protected byte[] secretBytes;
    protected byte[] vectorBytes;

    public BasicEncryptor(IEncryptType type, byte[] secretBytes) {
        this.type = type;
        this.secretBytes = secretBytes;
    }

    public BasicEncryptor(IEncryptType type, byte[] secretBytes, byte[] vectorBytes) {
        this.type = type;
        this.secretBytes = secretBytes;
        this.vectorBytes = vectorBytes;
    }

    @Override
    public Cipher getEncryptCipher() throws Exception {
        return getCipher(true);
    }

    @Override
    public Cipher getDecryptCipher() throws Exception {
        return getCipher(false);
    }

    public Cipher getCipher(boolean encryptMode) throws Exception {
        Cipher cipher = CipherUtil.getCipher(type, providerName, encryptMode, secretBytes, vectorBytes,
                false, null, -1, -1);
        return cipher;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = getEncryptCipher();
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = getDecryptCipher();
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }
}
