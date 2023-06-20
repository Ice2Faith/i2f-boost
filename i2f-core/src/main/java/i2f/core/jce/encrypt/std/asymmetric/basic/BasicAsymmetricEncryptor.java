package i2f.core.jce.encrypt.std.asymmetric.basic;

import i2f.core.jce.encrypt.CipherWorker;
import i2f.core.jce.encrypt.IEncryptType;
import i2f.core.jce.encrypt.std.asymmetric.AsymmetricEncryptor;
import i2f.core.jce.encrypt.std.asymmetric.IAsymmetricCipherProvider;

import javax.crypto.Cipher;

/**
 * @author Ice2Faith
 * @date 2023/6/20 10:50
 * @desc
 */
public abstract class BasicAsymmetricEncryptor implements AsymmetricEncryptor, IAsymmetricCipherProvider {
    protected IEncryptType type;
    protected byte[] publicBytes;
    protected byte[] privateBytes;
    protected String providerName;

    public BasicAsymmetricEncryptor(IEncryptType type, byte[] publicBytes, byte[] privateBytes) {
        this.type = type;
        this.publicBytes = publicBytes;
        this.privateBytes = privateBytes;
    }

    @Override
    public byte[] publicEncrypt(byte[] data) throws Exception {
        Cipher cipher = getEncryptCipher(false);
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }

    @Override
    public byte[] privateDecrypt(byte[] data) throws Exception {
        Cipher cipher = getDecryptCipher(true);
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }


    @Override
    public Cipher getEncryptCipher(boolean isPrivate) throws Exception {
        return getCipher(true, isPrivate);
    }

    @Override
    public Cipher getDecryptCipher(boolean isPrivate) throws Exception {
        return getCipher(false, isPrivate);
    }

    public abstract Cipher getCipher(boolean encryptMode, boolean isPrivate) throws Exception;

}
