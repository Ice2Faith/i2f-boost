package i2f.core.digest;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.bc.encrypt.asymmetric.rsa.BcRsaEncryptor;
import i2f.core.security.jce.bc.encrypt.asymmetric.rsa.BcRsaType;
import i2f.core.security.jce.codec.CodecUtil;
import i2f.core.security.jce.encrypt.CipherUtil;

import java.security.KeyPair;

/**
 * @author ltb
 * @date 2022/6/28 15:08
 * @desc
 */
public class BcRsaUtil {

    public static RsaKey makeKeyPair() {
        return makeKeyPair(1024);
    }

    /**
     * 创建密钥对
     *
     * @param size
     * @return
     */
    public static RsaKey makeKeyPair(int size) {
        try {
            BouncyCastleHolder.registry();
            KeyPair keyPair = CipherUtil.genKeyPair(BcRsaType.NONE_PKCS1PADDING, BouncyCastleHolder.PROVIDER_NAME, "".getBytes(), size, null);
            return new RsaKey(keyPair);
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
    public static byte[] privateKeyDecrypt(RsaKey key, byte[] data) {
        try {
            BcRsaEncryptor encryptor = new BcRsaEncryptor(BcRsaType.NONE_PKCS1PADDING, key.publicKeyBytes(), key.privateKeyBytes());
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
    public static byte[] privateKeyEncrypt(RsaKey key, byte[] data) {
        try {
            BcRsaEncryptor encryptor = new BcRsaEncryptor(BcRsaType.NONE_PKCS1PADDING, key.publicKeyBytes(), key.privateKeyBytes());
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
    public static byte[] publicKeyDecrypt(RsaKey key, byte[] data) {
        try {
            BcRsaEncryptor encryptor = new BcRsaEncryptor(BcRsaType.NONE_PKCS1PADDING, key.publicKeyBytes(), key.privateKeyBytes());
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
    public static byte[] publicKeyEncrypt(RsaKey key, byte[] data) {
        try {
            BcRsaEncryptor encryptor = new BcRsaEncryptor(BcRsaType.NONE_PKCS1PADDING, key.publicKeyBytes(), key.privateKeyBytes());
            return encryptor.publicEncrypt(data);
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
    public static String privateKeyDecryptBase64(RsaKey key, String bs64) {
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
    public static String privateKeyEncryptBase64(RsaKey key, String text) {
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
    public static String publicKeyDecryptBase64(RsaKey key, String bs64) {
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
    public static String publicKeyEncryptBase64(RsaKey key, String text) {
        byte[] data = CodecUtil.toUtf8(text);
        byte[] enc = publicKeyEncrypt(key, data);
        return Base64Util.encode(enc);
    }
}