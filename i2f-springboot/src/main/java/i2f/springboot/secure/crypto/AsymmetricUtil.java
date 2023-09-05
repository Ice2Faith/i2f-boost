package i2f.springboot.secure.crypto;

import i2f.core.codec.CodecUtil;
import i2f.core.digest.Base64Util;
import i2f.core.security.jce.encrypt.std.asymmetric.AsymmetricEncryptor;
import i2f.core.security.jce.encrypt.std.asymmetric.data.AsymKeyPair;

/**
 * @author ltb
 * @date 2022/6/28 15:08
 * @desc
 */
public class AsymmetricUtil {

    public static AsymKeyPair makeKeyPair() {
        return makeKeyPair(1024);
    }

    /**
     * 创建密钥对
     *
     * @param size
     * @return
     */
    public static AsymKeyPair makeKeyPair(int size) {
        try {
            AsymKeyPair keyPair = SecureProvider.asymmetricKeyPairSupplier.get(size);
            return keyPair;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 私钥解密
     *
     * @param key
     * @param data
     * @return
     */
    public static byte[] privateKeyDecrypt(AsymKeyPair key, byte[] data) {
        try {
            AsymmetricEncryptor encryptor = SecureProvider.asymmetricEncryptorSupplier.get();
            encryptor.setKeyPair(key);
            return encryptor.privateDecrypt(data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 私钥加密
     *
     * @param key
     * @param data
     * @return
     */
    public static byte[] privateKeyEncrypt(AsymKeyPair key, byte[] data) {
        try {
            AsymmetricEncryptor encryptor = SecureProvider.asymmetricEncryptorSupplier.get();
            encryptor.setKeyPair(key);
            return encryptor.privateEncrypt(data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 公钥解密
     *
     * @param key
     * @param data
     * @return
     */
    public static byte[] publicKeyDecrypt(AsymKeyPair key, byte[] data) {
        try {
            AsymmetricEncryptor encryptor = SecureProvider.asymmetricEncryptorSupplier.get();
            encryptor.setKeyPair(key);
            return encryptor.publicDecrypt(data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 公钥加密
     *
     * @param key
     * @param data
     * @return
     */
    public static byte[] publicKeyEncrypt(AsymKeyPair key, byte[] data) {
        try {
            AsymmetricEncryptor encryptor = SecureProvider.asymmetricEncryptorSupplier.get();
            encryptor.setKeyPair(key);
            return encryptor.publicEncrypt(data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] makeSign(AsymKeyPair key, byte[] data) {
        try {
            AsymmetricEncryptor encryptor = SecureProvider.asymmetricEncryptorSupplier.get();
            encryptor.setKeyPair(key);
            return encryptor.makeSign(data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String makeSignAsString(AsymKeyPair key, byte[] data) {
        try {
            AsymmetricEncryptor encryptor = SecureProvider.asymmetricEncryptorSupplier.get();
            encryptor.setKeyPair(key);
            return encryptor.makeSignAsString(data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static boolean verifySign(AsymKeyPair key, byte[] sign, byte[] data) {
        try {
            AsymmetricEncryptor encryptor = SecureProvider.asymmetricEncryptorSupplier.get();
            encryptor.setKeyPair(key);
            return encryptor.verifySign(sign, data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static boolean verifySignByString(AsymKeyPair key, String sign, byte[] data) {
        try {
            AsymmetricEncryptor encryptor = SecureProvider.asymmetricEncryptorSupplier.get();
            encryptor.setKeyPair(key);
            return encryptor.verifySignByString(sign, data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 私钥，对输入base64解密为string字符串
     * 适用于使用JSON序列化的数据加密后使用base64传输
     *
     * @param key
     * @param bs64
     * @return
     */
    public static String privateKeyDecryptBase64(AsymKeyPair key, String bs64) {
        byte[] enc = Base64Util.decode(bs64);
        byte[] dec = privateKeyDecrypt(key, enc);
        return CodecUtil.ofUtf8(dec);
    }

    /**
     * 私钥，对输入的字符串加密为base64字符串
     * 适用于已经使用JSON序列化的数据进行加密后使用base64传输
     *
     * @param key
     * @param text
     * @return
     */
    public static String privateKeyEncryptBase64(AsymKeyPair key, String text) {
        byte[] data = CodecUtil.toUtf8(text);
        byte[] enc = privateKeyEncrypt(key, data);
        return Base64Util.encode(enc);
    }

    /**
     * 公钥，对输入base64解密为string字符串
     * 适用于使用JSON序列化的数据加密后使用base64传输
     *
     * @param key
     * @param bs64
     * @return
     */
    public static String publicKeyDecryptBase64(AsymKeyPair key, String bs64) {
        byte[] enc = Base64Util.decode(bs64);
        byte[] dec = publicKeyDecrypt(key, enc);
        return CodecUtil.ofUtf8(dec);
    }

    /**
     * 公钥，对输入的字符串加密为base64字符串
     * 适用于已经使用JSON序列化的数据进行加密后使用base64传输
     *
     * @param key
     * @param text
     * @return
     */
    public static String publicKeyEncryptBase64(AsymKeyPair key, String text) {
        byte[] data = CodecUtil.toUtf8(text);
        byte[] enc = publicKeyEncrypt(key, data);
        return Base64Util.encode(enc);
    }
}
