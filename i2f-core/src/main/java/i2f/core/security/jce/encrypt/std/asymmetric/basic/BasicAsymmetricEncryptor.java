package i2f.core.security.jce.encrypt.std.asymmetric.basic;

import i2f.core.security.jce.encrypt.CipherWorker;
import i2f.core.security.jce.encrypt.IEncryptType;
import i2f.core.security.jce.encrypt.std.asymmetric.AsymmetricEncryptor;
import i2f.core.security.jce.encrypt.std.asymmetric.IAsymmetricCipherProvider;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

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

    public BasicAsymmetricEncryptor(IEncryptType type, KeyPair keyPair) {
        this.type = type;
        if (keyPair != null) {
            if (keyPair.getPublic() != null) {
                this.publicBytes = keyPair.getPublic().getEncoded();
            }
            if (keyPair.getPrivate() != null) {
                this.privateBytes = keyPair.getPrivate().getEncoded();
            }
        }
    }

    public BasicAsymmetricEncryptor(IEncryptType type, PublicKey publicKey, PrivateKey privateKey) {
        this.type = type;
        if (publicKey != null) {
            this.publicBytes = publicKey.getEncoded();
        }
        if (privateKey != null) {
            this.privateBytes = privateKey.getEncoded();
        }
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
