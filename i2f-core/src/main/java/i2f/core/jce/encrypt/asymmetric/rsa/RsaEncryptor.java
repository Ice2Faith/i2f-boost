package i2f.core.jce.encrypt.asymmetric.rsa;

import i2f.core.jce.encrypt.CipherUtil;
import i2f.core.jce.encrypt.std.asymmetric.basic.BasicAsymmetricEncryptor;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
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
public class RsaEncryptor extends BasicAsymmetricEncryptor {
    public RsaEncryptor(byte[] publicBytes, byte[] privateBytes) {
        super(RsaType.ECB_PKCS1PADDING, publicBytes, privateBytes);
    }

    public RsaEncryptor(RsaType type, byte[] publicBytes, byte[] privateBytes) {
        super(type, publicBytes, privateBytes);
    }

    @Override
    public Cipher getCipher(boolean encryptMode, boolean isPrivate) throws Exception {
        int mode = encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        Cipher cipher = CipherUtil.cipherOf(type, providerName);
        if (encryptMode) {
            if (isPrivate) {
                RSAPrivateKey priKey = getRsaPrivateKey(privateBytes);
                cipher.init(mode, priKey);
            } else {
                RSAPublicKey pubKey = getRsaPublicKey(publicBytes);
                cipher.init(mode, pubKey);
            }
        } else {
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

    public RSAPublicKey getRsaPublicKey(byte[] codes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(type.algorithmName()).generatePublic(new X509EncodedKeySpec(codes));
        return pubKey;
    }

    public RSAPrivateKey getRsaPrivateKey(byte[] codes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(type.algorithmName()).generatePrivate(new PKCS8EncodedKeySpec(codes));
        return priKey;
    }

}
