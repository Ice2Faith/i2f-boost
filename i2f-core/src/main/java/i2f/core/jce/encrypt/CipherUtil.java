package i2f.core.jce.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

/**
 * @author Ice2Faith
 * @date 2023/6/19 17:13
 * @desc
 */
public class CipherUtil {
    /**
     * 获取加密的密匙，传入的slatKey可以是任意长度的，作为SecureRandom的随机种子，
     * 而在KeyGenerator初始化时设置密匙的长度128bit(16位byte)
     */
    public static SecretKey genSecretKey(IEncryptType type, byte[] secretBytes, int genSecretLen, String secureRandomAlgorithmName) throws Exception {
        if (secureRandomAlgorithmName == null || "".equals(secureRandomAlgorithmName)) {
            secureRandomAlgorithmName = "SHA1PRNG";
        }
        KeyGenerator kgen = KeyGenerator.getInstance(type.algorithmName());
        SecureRandom random = SecureRandom.getInstance(secureRandomAlgorithmName);
        random.setSeed(secretBytes);
        kgen.init(genSecretLen, random);
        return kgen.generateKey();
    }

    /**
     * 获取加密的向量
     */
    public static IvParameterSpec genIvParameterSpec(IEncryptType type, byte[] vectorBytes, int genVectorLen, String secureRandomAlgorithmName) throws Exception {
        if (secureRandomAlgorithmName == null || "".equals(secureRandomAlgorithmName)) {
            secureRandomAlgorithmName = "SHA1PRNG";
        }
        KeyGenerator kgen = KeyGenerator.getInstance(type.algorithmName());
        SecureRandom random = SecureRandom.getInstance(secureRandomAlgorithmName);
        random.setSeed(vectorBytes);
        kgen.init(genVectorLen, random);
        IvParameterSpec iv = new IvParameterSpec(kgen.generateKey().getEncoded());
        return iv;
    }

    public static Cipher getCipher(IEncryptType type, String providerName, boolean encryptMode, byte[] secretBytes, byte[] vectorBytes) throws Exception {
        return getCipher(type, providerName, encryptMode, secretBytes, vectorBytes,
                false, null, -1, -1);
    }

    public static Cipher getCipher(IEncryptType type, String providerName, boolean encryptMode, byte[] secretBytes, byte[] vectorBytes,
                                   boolean useKeygen, String secureRandomAlgorithmName, int secretBytesLen, int vectorBytesLen) throws Exception {
        if ("".equals(providerName)) {
            providerName = null;
        }
        if (providerName != null) {
            Provider provider = Security.getProvider(providerName);
            if (provider == null) {
                providerName = null;
            }
        }
        int mode = encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        Cipher cipher = null;
        if (providerName == null) {
            cipher = Cipher.getInstance(type.type());
        } else {
            cipher = Cipher.getInstance(type.type(), providerName);
        }
        SecretKey secretKey = null;
        IvParameterSpec iv = null;
        if (useKeygen) {
            secretKey = genSecretKey(type, secretBytes, secretBytesLen, secureRandomAlgorithmName);

            if (type.requireVector()) {
                iv = genIvParameterSpec(type, vectorBytes, vectorBytesLen, secureRandomAlgorithmName);
            }
        } else {
            if (secretBytes == null || !type.supportSecretBytesLen(secretBytes.length / 8)) {
                throw new UnsupportedOperationException("secret bytes length must be " + type.secretBytesLen());
            }
            secretKey = new SecretKeySpec(secretBytes, type.algorithmName());
            if (type.requireVector()) {
                if (vectorBytes == null || !type.supportVectorBytesLen(vectorBytes.length / 8)) {
                    throw new UnsupportedOperationException("vector bytes length must be " + type.vectorBytesLen());
                }
                iv = new IvParameterSpec(vectorBytes);
            }
        }
        if (type.requireVector()) {
            cipher.init(mode, secretKey, iv);
        } else {
            cipher.init(mode, secretKey);
        }
        return cipher;
    }
}
