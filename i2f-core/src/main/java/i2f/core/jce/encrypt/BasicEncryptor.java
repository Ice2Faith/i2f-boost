package i2f.core.jce.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc 基本加解密器，使用SecretKeySpec作为秘钥生成器
 */
public class BasicEncryptor implements IEncryptor, IChiperProvider {

    protected IEncryptType type;
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
        int mode=encryptMode?Cipher.ENCRYPT_MODE:Cipher.DECRYPT_MODE;
        if(this.secretBytes==null || this.secretBytes.length!= type.secretBytesLen()){
            throw new UnsupportedOperationException("secret bytes length must be "+ type.secretBytesLen());
        }
        Cipher cipher = Cipher.getInstance(type.type());
        SecretKey secretKey = new SecretKeySpec(this.secretBytes, type.algorithmName());
        if (type.requireVector()) {
            if(this.vectorBytes==null || this.vectorBytes.length!= type.vectorBytesLen()){
                throw new UnsupportedOperationException("vector bytes length must be "+ type.vectorBytesLen());
            }
            IvParameterSpec iv=new IvParameterSpec(vectorBytes);
            cipher.init(mode,secretKey,iv);
        }else{
            cipher.init(mode,secretKey);
        }
        return cipher;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher=getEncryptCipher();
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher=getDecryptCipher();
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }
}
