package i2f.core.secret.impl.ram.api;

import i2f.core.secret.api.key.IKeyPair;
import i2f.core.secret.data.SecretKeyPair;
import i2f.core.secret.exception.SecretException;
import i2f.core.secret.util.SecretUtil;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.crypto.Cipher;
import java.security.*;

/**
 * @author ltb
 * @date 2022/10/19 17:58
 * @desc
 */
public class RsaProvider {
    public boolean usePkcs1Bc = false;

    public RsaProvider() {
    }

    public RsaProvider(boolean usePkcs1Bc) {
        this.usePkcs1Bc = usePkcs1Bc;
    }

    public static final String JCE_BouncyCastleProvider = "org.bouncycastle.jce.provider.BouncyCastleProvider";

    public Class getBouncyCastleProvider() {
        return SecretUtil.getClassByName(JCE_BouncyCastleProvider);
    }

    public Provider getPkcs1Provider() {
        Class clazz = getBouncyCastleProvider();
        if (clazz == null) {
            throw new SecretException("Pkcs1 Not Found for class:" + JCE_BouncyCastleProvider);
        }
        try {
            return (Provider) clazz.newInstance();
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    public void registryPkcs1Provider() {
        Provider provider = getPkcs1Provider();
        Security.addProvider(provider);
    }

    public KeyPair makeRsaKey(int size) {
        try {
            if (usePkcs1Bc) {
                registryPkcs1Provider();
                SecureRandom rand = new SecureRandom();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
                generator.initialize(size, rand);
                KeyPair keyPair = generator.generateKeyPair();
                return keyPair;
            } else {
                SecureRandom rand = new SecureRandom();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(size, rand);
                KeyPair keyPair = generator.generateKeyPair();
                return keyPair;
            }
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    public IKeyPair adaptRsaKeyPair(KeyPair pair) {
        SecretKeyPair ret = new SecretKeyPair();
        ret.publicKey = pair.getPublic().getEncoded();
        ret.privateKey = pair.getPrivate().getEncoded();
        return ret;
    }

    public KeyPair adaptRsaKeyPair(IKeyPair pair) {
        try {
            PublicKey publicKey = new RSAPublicKeyImpl(pair.publicKey());
            PrivateKey privateKey = null;
            if (pair.privateKey() != null) {
                privateKey = RSAPrivateCrtKeyImpl.newKey(pair.privateKey());
            }
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    public Cipher rsaCipher() {
        try {
            if (usePkcs1Bc) {
                registryPkcs1Provider();
                Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
                return cipher;
            } else {
                Cipher cipher = Cipher.getInstance("RSA");
                return cipher;
            }
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    public byte[] encrypt(byte[] data, IKeyPair key) {
        try {
            Cipher cipher = rsaCipher();
            cipher.init(Cipher.ENCRYPT_MODE, adaptRsaKeyPair(key).getPrivate());
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    public byte[] decrypt(byte[] data, IKeyPair key) {
        try {
            Cipher cipher = rsaCipher();
            cipher.init(Cipher.DECRYPT_MODE, adaptRsaKeyPair(key).getPublic());
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }
}
