package i2f.core.security.jce.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2023/6/19 17:13
 * @desc
 */
public class CipherUtil {
    public static SecretKey genSecretKey(IEncryptType type, byte[] secretBytes, int genSecretLen) throws Exception {
        return genSecretKey(type, secretBytes, genSecretLen, null);
    }

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
        if(secretBytes!=null){
            random.setSeed(secretBytes);
        }
        kgen.init(genSecretLen, random);
        return kgen.generateKey();
    }

    public static IvParameterSpec genIvParameterSpec(IEncryptType type, byte[] vectorBytes, int genVectorLen) throws Exception {
        return genIvParameterSpec(type, vectorBytes, genVectorLen, null);
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
        if(vectorBytes!=null){
            random.setSeed(vectorBytes);
        }
        kgen.init(genVectorLen, random);
        IvParameterSpec iv = new IvParameterSpec(kgen.generateKey().getEncoded());
        return iv;
    }

    public static KeyPair genKeyPair(IEncryptType type, byte[] secretBytes, int genSecretLen) throws Exception {
        return genKeyPair(type, secretBytes, genSecretLen, null);
    }

    /**
     * 根据slatKey获取公匙，传入的slatKey作为SecureRandom的随机种子
     * 若使用new SecureRandom()创建公匙，则需要记录下私匙，解密时使用
     */
    public static KeyPair genKeyPair(IEncryptType type, byte[] secretBytes, int genSecretLen, String secureRandomAlgorithmName) throws Exception {
        return genKeyPair(type, null, secretBytes, genSecretLen, secureRandomAlgorithmName);
    }

    public static KeyPair genKeyPair(IEncryptType type, String providerName, byte[] secretBytes, int genSecretLen, String secureRandomAlgorithmName) throws Exception {
        if (secureRandomAlgorithmName == null || "".equals(secureRandomAlgorithmName)) {
            secureRandomAlgorithmName = "SHA1PRNG";
        }
        if ("".equals(providerName)) {
            providerName = null;
        }
        if (providerName != null) {
            Provider provider = Security.getProvider(providerName);
            if (provider == null) {
                providerName = null;
            }
        }
        KeyPairGenerator keyPairGenerator = null;
        if (providerName != null) {
            keyPairGenerator = KeyPairGenerator.getInstance(type.algorithmName(), providerName);
        } else {
            keyPairGenerator = KeyPairGenerator.getInstance(type.algorithmName());
        }
        SecureRandom random = SecureRandom.getInstance(secureRandomAlgorithmName);
        if(secretBytes!=null){
            random.setSeed(secretBytes);
        }
        keyPairGenerator.initialize(genSecretLen, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static Cipher getCipher(IEncryptType type, String providerName, boolean encryptMode, byte[] secretBytes, byte[] vectorBytes) throws Exception {
        return getCipher(type, providerName, encryptMode, secretBytes, vectorBytes,
                false, null, -1, -1);
    }

    public static Cipher cipherOf(IEncryptType type, String providerName) throws Exception {
        return cipherOf(type.type(), providerName);
    }

    public static Cipher cipherOf(String type, String providerName) throws Exception {
        if ("".equals(providerName)) {
            providerName = null;
        }
        if (providerName != null) {
            Provider provider = Security.getProvider(providerName);
            if (provider == null) {
                providerName = null;
            }
        }
        Cipher cipher = null;
        if (providerName == null) {
            cipher = Cipher.getInstance(type);
        } else {
            cipher = Cipher.getInstance(type, providerName);
        }
        return cipher;
    }

    public static Cipher getCipher(IEncryptType type, String providerName, boolean encryptMode, byte[] secretBytes, byte[] vectorBytes,
                                   boolean useKeygen, String secureRandomAlgorithmName, int secretBytesLen, int vectorBytesLen) throws Exception {

        int mode = encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        Cipher cipher = cipherOf(type, providerName);
        SecretKey secretKey = null;
        IvParameterSpec iv = null;
        if (useKeygen) {
            secretKey = genSecretKey(type, secretBytes, secretBytesLen, secureRandomAlgorithmName);

            if (type.requireVector()) {
                iv = genIvParameterSpec(type, vectorBytes, vectorBytesLen, secureRandomAlgorithmName);
            }
        } else {
            if (secretBytes == null || !type.supportSecretBytesLen(secretBytes.length * 8)) {
                throw new UnsupportedOperationException("secret bytes length must in " + Arrays.toString(type.secretBytesLen()));
            }
            secretKey = new SecretKeySpec(secretBytes, type.algorithmName());
            if (type.requireVector()) {
                if (vectorBytes == null || !type.supportVectorBytesLen(vectorBytes.length * 8)) {
                    throw new UnsupportedOperationException("vector bytes length must in " + Arrays.toString(type.vectorBytesLen()));
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
