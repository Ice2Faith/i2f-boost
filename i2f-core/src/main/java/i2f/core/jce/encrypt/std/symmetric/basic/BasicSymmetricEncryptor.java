package i2f.core.jce.encrypt.std.symmetric.basic;

import i2f.core.jce.encrypt.CipherUtil;
import i2f.core.jce.encrypt.CipherWorker;
import i2f.core.jce.encrypt.IEncryptType;
import i2f.core.jce.encrypt.std.symmetric.ISymmetricCipherProvider;
import i2f.core.jce.encrypt.std.symmetric.SymmetricEncryptor;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.Cipher;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc 基本加解密器，使用SecretKeySpec作为秘钥生成器
 */
@Getter
@Setter
public class BasicSymmetricEncryptor implements SymmetricEncryptor, ISymmetricCipherProvider {

    protected IEncryptType type;
    protected String providerName;
    protected byte[] secretBytes;
    protected byte[] vectorBytes;

    public BasicSymmetricEncryptor(IEncryptType type, byte[] secretBytes) {
        this.type = type;
        this.secretBytes = secretBytes;
    }

    public BasicSymmetricEncryptor(IEncryptType type, byte[] secretBytes, byte[] vectorBytes) {
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
