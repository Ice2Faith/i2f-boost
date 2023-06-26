package i2f.core.security.jce.encrypt.asymmetric.rsa;

import i2f.core.security.jce.encrypt.CipherUtil;
import i2f.core.security.jce.encrypt.CipherWorker;
import i2f.core.security.jce.encrypt.std.asymmetric.basic.BasicAsymmetricEncryptor;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc RSA加解密器
 */
public class RsaEncryptor extends BasicAsymmetricEncryptor {
    public RsaEncryptor(byte[] publicBytes, byte[] privateBytes) {
        super(RsaType.ECB_PKCS1PADDING, publicBytes, privateBytes);
    }

    public RsaEncryptor(RsaType type, byte[] publicBytes, byte[] privateBytes) {
        super(type, publicBytes, privateBytes);
    }

    public RsaEncryptor(KeyPair keyPair) {
        this(keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());
    }

    public RsaEncryptor(RsaType type, KeyPair keyPair) {
        this(type, keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());
    }

    @Override
    public byte[] publicDecrypt(byte[] data) throws Exception {
        Cipher cipher = getDecryptCipher(false);
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }

    @Override
    public byte[] privateEncrypt(byte[] data) throws Exception {
        Cipher cipher = getEncryptCipher(true);
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }

    @Override
    public Cipher getCipher(boolean encryptMode, boolean isPrivate) throws Exception {
        int mode = encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        Cipher cipher = CipherUtil.cipherOf(type, providerName);
        if (encryptMode) {
            if (isPrivate) {
                PrivateKey priKey = getPrivateKey(privateBytes);
                cipher.init(mode, priKey);
            } else {
                PublicKey pubKey = getPublicKey(publicBytes);
                cipher.init(mode, pubKey);
            }
        } else {
            if (isPrivate) {
                PrivateKey priKey = getPrivateKey(privateBytes);
                cipher.init(mode, priKey);
            } else {
                PublicKey pubKey = getPublicKey(publicBytes);
                cipher.init(mode, pubKey);
            }
        }
        return cipher;
    }

    public PublicKey getPublicKey(byte[] codes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey pubKey = KeyFactory.getInstance(type.algorithmName()).generatePublic(new X509EncodedKeySpec(codes));
        return pubKey;
    }

    public PrivateKey getPrivateKey(byte[] codes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey priKey = KeyFactory.getInstance(type.algorithmName()).generatePrivate(new PKCS8EncodedKeySpec(codes));
        return priKey;
    }

}
