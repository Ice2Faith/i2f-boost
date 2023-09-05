package i2f.core.security.jce.bc.encrypt.asymmetric.sm2;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.encrypt.CipherUtil;
import i2f.core.security.jce.encrypt.std.asymmetric.basic.BasicAsymmetricEncryptor;
import i2f.core.security.jce.encrypt.std.asymmetric.data.AsymKeyPair;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc RSA加解密器
 */
public class BcSm2Encryptor extends BasicAsymmetricEncryptor {
    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcSm2Encryptor() {
        super(BcSm2Type.DEFAULT);
    }

    public BcSm2Encryptor(BcSm2Type type) {
        super(type);
    }

    public BcSm2Encryptor(KeyPair keyPair) {
        super(BcSm2Type.DEFAULT, keyPair);
    }

    public BcSm2Encryptor(BcSm2Type type, KeyPair keyPair) {
        super(type, keyPair);
    }

    public BcSm2Encryptor(AsymKeyPair keyPair) {
        super(BcSm2Type.DEFAULT, keyPair);
    }

    public BcSm2Encryptor(BcSm2Type type, AsymKeyPair keyPair) {
        super(type, keyPair);
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

    @Override
    public PublicKey getPublicKey(byte[] codes) throws Exception {
        PublicKey pubKey = KeyFactory.getInstance(type.algorithmName()).generatePublic(new X509EncodedKeySpec(codes));
        return pubKey;
    }

    @Override
    public PrivateKey getPrivateKey(byte[] codes) throws Exception {
        PrivateKey priKey = KeyFactory.getInstance(type.algorithmName()).generatePrivate(new PKCS8EncodedKeySpec(codes));
        return priKey;
    }

}
