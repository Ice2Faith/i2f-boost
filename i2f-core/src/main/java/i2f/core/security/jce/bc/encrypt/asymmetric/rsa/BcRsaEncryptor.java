package i2f.core.security.jce.bc.encrypt.asymmetric.rsa;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.encrypt.CipherUtil;
import i2f.core.security.jce.encrypt.CipherWorker;
import i2f.core.security.jce.encrypt.std.asymmetric.basic.BasicAsymmetricEncryptor;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc RSA加解密器
 */
public class BcRsaEncryptor extends BasicAsymmetricEncryptor {
    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcRsaEncryptor(byte[] publicBytes, byte[] privateBytes) {
        super(BcRsaType.ECB_PKCS1PADDING, publicBytes, privateBytes);
    }

    public BcRsaEncryptor(BcRsaType type, byte[] publicBytes, byte[] privateBytes) {
        super(type, publicBytes, privateBytes);
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
