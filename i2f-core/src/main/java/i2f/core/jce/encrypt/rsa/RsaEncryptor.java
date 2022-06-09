package i2f.core.jce.encrypt.rsa;

import i2f.core.jce.encrypt.CipherWorker;
import i2f.core.jce.encrypt.IChiperProvider;
import i2f.core.jce.encrypt.IEncryptor;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc RSA加解密器
 */
public class RsaEncryptor implements IEncryptor, IChiperProvider {

    protected RsaType type;
    protected byte[] secretBytes;
    protected byte[] vectorBytes;
    public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    public RsaEncryptor(byte[] secretBytes) {
        this.type = RsaType.ECB_PKCS1PADDING;
        this.secretBytes = secretBytes;
    }
    public RsaEncryptor(RsaType type, byte[] secretBytes) {
        this.type = type;
        this.secretBytes = secretBytes;
    }

    public RsaEncryptor(RsaType type, byte[] secretBytes, byte[] vectorBytes) {
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
        if(this.secretBytes==null ){
            throw new UnsupportedOperationException("secret bytes length must be not null");
        }
        Cipher cipher = Cipher.getInstance(type.type());
        if (encryptMode) {
            byte[] codes=getPublicKey(this.secretBytes);
            RSAPublicKey pubKey=getRsaPublicKey(codes);
            cipher.init(mode,pubKey);
        }else{
            byte[] codes=getPrivateKey(this.secretBytes);
            RSAPrivateKey priKey=getRsaPrivateKey(codes);
            cipher.init(mode,priKey);
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

    public RSAPublicKey getRsaPublicKey(byte[] codes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(type.algorithmName()).generatePublic(new X509EncodedKeySpec(codes));
        return pubKey;
    }

    public RSAPrivateKey getRsaPrivateKey(byte[] codes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(type.algorithmName()).generatePrivate(new PKCS8EncodedKeySpec(codes));
        return priKey;
    }


    /**
     * 根据slatKey获取公匙，传入的slatKey作为SecureRandom的随机种子
     * 若使用new SecureRandom()创建公匙，则需要记录下私匙，解密时使用
     */
    public byte[] getPublicKey(byte[] secretBytes) throws Exception {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance(type.algorithmName());
        SecureRandom random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
        random.setSeed(secretBytes);
        keyPairGenerator.initialize(type.secretBytesLen(), random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair.getPublic().getEncoded();
    }

    /**
     * 根据slatKey获取私匙，传入的slatKey作为SecureRandom的随机种子
     */
    public byte[] getPrivateKey(byte[] secretBytes) throws Exception {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance(type.algorithmName());
        SecureRandom random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
        random.setSeed(secretBytes);
        keyPairGenerator.initialize(type.secretBytesLen(), random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair.getPrivate().getEncoded();
    }
}
