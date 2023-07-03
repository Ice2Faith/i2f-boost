package i2f.core.digest;

import i2f.core.annotations.remark.Author;
import i2f.core.security.jce.encrypt.CipherUtil;
import i2f.core.security.jce.encrypt.asymmetric.rsa.RsaEncryptor;
import i2f.core.security.jce.encrypt.asymmetric.rsa.RsaType;
import i2f.core.type.str.Strings;

import javax.crypto.Cipher;
import java.security.KeyPair;

//私钥加密，公钥解密，网络传输中，传输公钥和密文
@Author("i2f")
public class RsaUtil {
    public static int UPDATE_SIZE=116;//require lower than 117
    public static int DEFAULT_KEY_SIZE=1024;
    public static String CHAR_SET_NAME = "UTF-8";
    public static RsaType RSA_TYPE = RsaType.DEFAULT;

    public static AsymmetricKeyPair genRsaKeyData() throws Exception {
        return genRsaKeyData(DEFAULT_KEY_SIZE);
    }

    public static AsymmetricKeyPair genRsaKeyData(int size) throws Exception {
        return getRsaKeyData(genRsaKeyPair(size));
    }

    public static KeyPair genRsaKeyPair() throws Exception {
        return genRsaKeyPair(DEFAULT_KEY_SIZE);
    }

    public static KeyPair genRsaKeyPair(int size) throws Exception {
        return CipherUtil.genKeyPair(RSA_TYPE, null, null, size, null);
    }

    public static AsymmetricKeyPair getRsaKeyData(KeyPair keyPair) {
        return new AsymmetricKeyPair(keyPair);
    }


    public static byte[] doCipherWorker(Cipher cipher, byte[] data) {
        byte[] result = new byte[data.length];
        int plen = 0;
        while (plen < data.length) {
            int cplen = UPDATE_SIZE;
            if (data.length - plen < UPDATE_SIZE) {
                cplen = data.length - plen;
            }
            byte[] part=new byte[cplen];
            System.arraycopy(data,plen,part,0,cplen);
            byte[] enPart=cipher.update(part);
            System.arraycopy(part,0,result,plen,cplen);
            plen+=UPDATE_SIZE;
        }
        return result;
    }


    /**
     * 私钥解密
     *
     * @param key
     * @param data
     * @return
     */
    public static byte[] privateKeyDecrypt(AsymmetricKeyPair key, byte[] data) {
        try {
            RsaEncryptor encryptor = new RsaEncryptor(RSA_TYPE, key.publicKeyBytes(), key.privateKeyBytes());
            Cipher cipher = encryptor.getCipher(false, true);
            return doCipherWorker(cipher, data);
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
    public static byte[] privateKeyEncrypt(AsymmetricKeyPair key, byte[] data) {
        try {
            RsaEncryptor encryptor = new RsaEncryptor(RSA_TYPE, key.publicKeyBytes(), key.privateKeyBytes());
            Cipher cipher = encryptor.getCipher(true, true);
            return doCipherWorker(cipher, data);
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
    public static byte[] publicKeyDecrypt(AsymmetricKeyPair key, byte[] data) {
        try {
            RsaEncryptor encryptor = new RsaEncryptor(RSA_TYPE, key.publicKeyBytes(), key.privateKeyBytes());
            Cipher cipher = encryptor.getCipher(false, false);
            return doCipherWorker(cipher, data);
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
    public static byte[] publicKeyEncrypt(AsymmetricKeyPair key, byte[] data) {
        try {
            RsaEncryptor encryptor = new RsaEncryptor(RSA_TYPE, key.publicKeyBytes(), key.privateKeyBytes());
            Cipher cipher = encryptor.getCipher(true, false);
            return doCipherWorker(cipher, data);
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
    public static String privateKeyDecryptBase64(AsymmetricKeyPair key, String bs64) {
        byte[] enc = Base64Util.decode(bs64);
        byte[] dec = privateKeyDecrypt(key, enc);
        return Strings.ofUtf8(dec);
    }

    /**
     * 私钥，对输入的字符串加密为base64字符串
     * 适用于已经使用JSON序列化的数据进行加密后使用base64传输
     *
     * @param key
     * @param text
     * @return
     */
    public static String privateKeyEncryptBase64(AsymmetricKeyPair key, String text) {
        byte[] data = Strings.toUtf8(text);
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
    public static String publicKeyDecryptBase64(AsymmetricKeyPair key, String bs64) {
        byte[] enc = Base64Util.decode(bs64);
        byte[] dec = publicKeyDecrypt(key, enc);
        return Strings.ofUtf8(dec);
    }

    /**
     * 公钥，对输入的字符串加密为base64字符串
     * 适用于已经使用JSON序列化的数据进行加密后使用base64传输
     *
     * @param key
     * @param text
     * @return
     */
    public static String publicKeyEncryptBase64(AsymmetricKeyPair key, String text) {
        byte[] data = Strings.toUtf8(text);
        byte[] enc = publicKeyEncrypt(key, data);
        return Base64Util.encode(enc);
    }

}
