package i2f.core.jce.encrypt.asymmetric.rsa;

import i2f.core.jce.encrypt.CipherWorker;
import i2f.core.jce.encrypt.asymmetric.AsymmetricEncryptor;

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
public class RsaEncryptor implements AsymmetricEncryptor {

    protected RsaType type;
    protected byte[] publicBytes;
    protected byte[] privateBytes;
    public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";

    public RsaEncryptor(RsaType type, byte[] publicBytes, byte[] privateBytes) {
        this.type = type;
        this.publicBytes = publicBytes;
        this.privateBytes = privateBytes;
    }

    @Override
    public byte[] publicEncrypt(byte[] data) throws Exception {
        return encrypt(data, false);
    }

    @Override
    public byte[] publicDecrypt(byte[] data) throws Exception {
        return decrypt(data, false);
    }

    @Override
    public byte[] privateEncrypt(byte[] data) throws Exception {
        return encrypt(data, true);
    }

    @Override
    public byte[] privateDecrypt(byte[] data) throws Exception {
        return decrypt(data, true);
    }

    public Cipher getEncryptCipher(boolean isPrivate) throws Exception {
        return getCipher(true, isPrivate);
    }

    public Cipher getDecryptCipher(boolean isPrivate) throws Exception {
        return getCipher(false, isPrivate);
    }

    public Cipher getCipher(boolean encryptMode, boolean isPrivate) throws Exception {
        int mode = encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        Cipher cipher = Cipher.getInstance(type.type());
        if (encryptMode) {
            if (isPrivate) {
                RSAPrivateKey priKey = getRsaPrivateKey(privateBytes);
                cipher.init(mode, priKey);
            } else {
                RSAPublicKey pubKey = getRsaPublicKey(publicBytes);
                cipher.init(mode, pubKey);
            }
        }else {
            if (isPrivate) {
                RSAPrivateKey priKey = getRsaPrivateKey(privateBytes);
                cipher.init(mode, priKey);
            } else {
                RSAPublicKey pubKey = getRsaPublicKey(publicBytes);
                cipher.init(mode, pubKey);
            }
        }
        return cipher;
    }

    public byte[] encrypt(byte[] data, boolean isPrivate) throws Exception {
        Cipher cipher = getEncryptCipher(isPrivate);
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data, boolean isPrivate) throws Exception {
        Cipher cipher = getDecryptCipher(isPrivate);
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
    public static KeyPair genKeyPair(RsaType type, byte[] secretBytes) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(type.algorithmName());
        SecureRandom random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
        random.setSeed(secretBytes);
        keyPairGenerator.initialize(type.secretBytesLen()[0], random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }


}
