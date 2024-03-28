package i2f.jce.jdk.encrypt;

import i2f.jce.jdk.encrypt.asymmetric.AsymmetricType;
import i2f.jce.jdk.encrypt.symmetric.SymmetricType;
import i2f.jce.jdk.supports.CipherAlgorithm;
import i2f.jce.jdk.supports.CipherMode;
import i2f.jce.jdk.supports.CipherPadding;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author Ice2Faith
 * @date 2024/3/27 13:55
 * @desc
 */
public class Encryptor {
    public static byte[] work(byte[] data, Cipher cipher) throws Exception {
        return cipher.doFinal(data);
    }

    public static byte[] work(byte[] data, int batchSize, Cipher cipher) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int i = 0;
        while ((i + batchSize) < data.length) {
            byte[] edata = cipher.update(data, i, batchSize);
            bos.write(edata);
            i += batchSize;
        }
        if (i < data.length) {
            byte[] edata = cipher.doFinal(data, i, data.length - i);
            bos.write(edata);
        }
        byte[] enc = bos.toByteArray();
        return enc;
    }

    public static void work(InputStream is, OutputStream os, int batchSize, Cipher cipher) throws Exception {
        byte[] buf = new byte[batchSize];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            byte[] edata = cipher.update(buf, 0, len);
            os.write(edata);
        }
        byte[] edata = cipher.doFinal();
        os.write(edata);
        os.flush();
    }


    public static byte[] handleNoPaddingEncryptFormat(Cipher cipher, String content) throws Exception {
        return handleNoPaddingEncryptFormat(cipher, content.getBytes("UTF-8"));
    }

    /**
     * <p>NoPadding加密模式, 加密内容必须是 8byte的倍数, 不足8位则末位补足0</p>
     * <p>加密算法不提供该补码方式, 需要代码完成该补码方式</p>
     *
     * @param cipher
     * @param data   ：加密内容
     * @return 符合加密的内容(byte[])
     */
    public static byte[] handleNoPaddingEncryptFormat(Cipher cipher, byte[] data) throws Exception {
        int blockSize = cipher.getBlockSize();
        int plaintextLength = data.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - plaintextLength % blockSize);
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(data, 0, plaintext, 0, data.length);
        return plaintext;
    }

    public static String algorithmNameOf(CipherAlgorithm algorithm, CipherMode mode, CipherPadding padding) {
        return algorithmNameOf(algorithm.text(), mode == null ? null : mode.text(), padding == null ? null : padding.text());
    }

    public static String algorithmNameOf(String algorithm, String mode, String padding) {
        String ret = algorithm;
        if (mode != null && !"".equals(mode)) {
            ret += "/" + mode;
        }
        if (padding != null && !"".equals(padding)) {
            ret += "/" + padding;
        }
        return ret;
    }

    public static String cipherAlgorithm(String algorithm) {
        if (algorithm == null) {
            return null;
        }
        int idx = algorithm.indexOf("/");
        if (idx < 0) {
            return algorithm;
        }
        return algorithm.substring(0, idx);
    }

    public static SecretKey keyOf(byte[] keyBytes, String algorithmName) {
        return new SecretKeySpec(keyBytes, algorithmName);
    }

    public static IvParameterSpec specOf(byte[] vectorBytes) {
        return new IvParameterSpec(vectorBytes);
    }

    public static SecretKey genSecretKey(String algorithmName, byte[] secretBytes, int genSecretLen, SecureRandomAlgorithm secureRandomAlgorithm) throws Exception {
        return genSecretKey(algorithmName, secretBytes, genSecretLen, secureRandomAlgorithm.text());
    }

    public static SecretKey genSecretKey(SymmetricType algorithm, byte[] secretBytes, String secureRandomAlgorithmName) throws Exception {
        return genSecretKey(algorithm.algorithmName(), secretBytes, algorithm.secretBytesLen()[0], secureRandomAlgorithmName);
    }

    /**
     * 获取加密的密匙，传入的slatKey可以是任意长度的，作为SecureRandom的随机种子，
     * 而在KeyGenerator初始化时设置密匙的长度128bit(16位byte)
     */
    public static SecretKey genSecretKey(String algorithmName, byte[] secretBytes, int genSecretLen, String secureRandomAlgorithmName) throws Exception {
        algorithmName = cipherAlgorithm(algorithmName);
        if (secureRandomAlgorithmName == null || "".equals(secureRandomAlgorithmName)) {
            secureRandomAlgorithmName = "SHA1PRNG";
        }
        KeyGenerator kgen = KeyGenerator.getInstance(algorithmName);
        SecureRandom random = SecureRandom.getInstance(secureRandomAlgorithmName);
        if (secretBytes != null) {
            random.setSeed(secretBytes);
        }
        kgen.init(genSecretLen, random);
        return kgen.generateKey();
    }

    public static SecretKey genPbeSecretKey(String algorithmName, String providerName, byte[] secretBytes, String secureRandomAlgorithmName) throws Exception {
        algorithmName = cipherAlgorithm(algorithmName);
        if (secureRandomAlgorithmName == null || "".equals(secureRandomAlgorithmName)) {
            secureRandomAlgorithmName = "SHA1PRNG";
        }
        char[] secretChars = new char[secretBytes.length];
        for (int i = 0; i < secretBytes.length; i++) {
            secretChars[i] = (char) secretBytes[i];
        }
        PBEKeySpec keySpec = new PBEKeySpec(secretChars);
        SecretKeyFactory factory = null;
        if (providerName == null || "".equals(providerName)) {
            factory = SecretKeyFactory.getInstance(algorithmName);
        } else {
            factory = SecretKeyFactory.getInstance(algorithmName, providerName);
        }
        return factory.generateSecret(keySpec);
    }

    public static AlgorithmParameterSpec genParameterSpec(String algorithmName, byte[] vectorBytes, int genVectorLen, SecureRandomAlgorithm secureRandomAlgorithm) throws Exception {
        return genParameterSpec(algorithmName, vectorBytes, genVectorLen, secureRandomAlgorithm.text());
    }

    public static AlgorithmParameterSpec genParameterSpec(SymmetricType algorithm, byte[] vectorBytes, String secureRandomAlgorithmName) throws Exception {
        return genParameterSpec(algorithm.algorithmName(), vectorBytes, algorithm.vectorBytesLen()[0], secureRandomAlgorithmName);
    }

    public static byte[] genKeyBytes(byte[] vectorBytes, int genVectorLen, String secureRandomAlgorithmName) throws Exception {
        if (secureRandomAlgorithmName == null || "".equals(secureRandomAlgorithmName)) {
            secureRandomAlgorithmName = "SHA1PRNG";
        }
        SecureRandom random = SecureRandom.getInstance(secureRandomAlgorithmName);
        if (vectorBytes != null) {
            random.setSeed(vectorBytes);
        }
        int len = genVectorLen / 8;
        byte[] ret = new byte[len];
        random.nextBytes(ret);
        return ret;
    }

    /**
     * 获取加密的向量
     */
    public static AlgorithmParameterSpec genParameterSpec(String algorithmName, byte[] vectorBytes, int genVectorLen, String secureRandomAlgorithmName) throws Exception {
        algorithmName = cipherAlgorithm(algorithmName);
        if (secureRandomAlgorithmName == null || "".equals(secureRandomAlgorithmName)) {
            secureRandomAlgorithmName = "SHA1PRNG";
        }
        KeyGenerator kgen = KeyGenerator.getInstance(algorithmName);
        SecureRandom random = SecureRandom.getInstance(secureRandomAlgorithmName);
        if (vectorBytes != null) {
            random.setSeed(vectorBytes);
        }
        kgen.init(genVectorLen, random);
        IvParameterSpec iv = new IvParameterSpec(kgen.generateKey().getEncoded());
        return iv;
    }

    public static AlgorithmParameterSpec genPbeParameterSpec(String algorithmName, byte[] vectorBytes, int genVectorLen, String secureRandomAlgorithmName, int iterationCount) throws Exception {
        byte[] bytes = genKeyBytes(vectorBytes, genVectorLen, secureRandomAlgorithmName);
        AlgorithmParameterSpec spec = new PBEParameterSpec(bytes, iterationCount);
        return spec;
    }

    public static KeyPair genKeyPair(String algorithmName, String providerName, byte[] secretBytes, int genSecretLen, SecureRandomAlgorithm secureRandomAlgorithm) throws Exception {
        return genKeyPair(algorithmName, providerName, secretBytes, genSecretLen, secureRandomAlgorithm.text());
    }

    public static KeyPair genKeyPair(AsymmetricType algorithm, byte[] secretBytes, String secureRandomAlgorithmName) throws Exception {
        return genKeyPair(algorithm.algorithmName(), algorithm.provider(), secretBytes, algorithm.secretBytesLen()[0], secureRandomAlgorithmName);
    }

    /**
     * 获取秘钥对
     */
    public static KeyPair genKeyPair(String algorithmName, String providerName, byte[] secretBytes, int genSecretLen, String secureRandomAlgorithmName) throws Exception {
        algorithmName = cipherAlgorithm(algorithmName);
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
            keyPairGenerator = KeyPairGenerator.getInstance(algorithmName, providerName);
        } else {
            keyPairGenerator = KeyPairGenerator.getInstance(algorithmName);
        }
        SecureRandom random = SecureRandom.getInstance(secureRandomAlgorithmName);
        if (secretBytes != null) {
            random.setSeed(secretBytes);
        }
        keyPairGenerator.initialize(genSecretLen, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static Cipher getCipher(String type, String providerName,
                                   boolean encrypt,
                                   SecretKey key, IvParameterSpec vector) {
        return getCipher(type, providerName, encrypt, key, vector);
    }

    public static Cipher getCipher(String type, String providerName,
                                   boolean encrypt,
                                   Key key, AlgorithmParameterSpec spec) throws Exception {
        int mode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        Cipher cipher = cipherOf(type, providerName);
        if (spec != null) {
            cipher.init(mode, key, spec);
        } else {
            cipher.init(mode, key);
        }
        return cipher;
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

    public static Signature signatureOf(String type, String providerName) throws Exception {
        if ("".equals(providerName)) {
            providerName = null;
        }
        if (providerName != null) {
            Provider provider = Security.getProvider(providerName);
            if (provider == null) {
                providerName = null;
            }
        }
        Signature signature = null;
        if (providerName == null) {
            signature = Signature.getInstance(type);
        } else {
            signature = Signature.getInstance(type, providerName);
        }
        return signature;
    }
}
