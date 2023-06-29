package i2f.springboot.secure.crypto;

import i2f.core.codec.CodecUtil;
import i2f.core.digest.Base64Util;
import i2f.core.security.jce.encrypt.std.symmetric.SymmetricEncryptor;

/**
 * @author ltb
 * @date 2021/9/2
 */
public class SymmetricUtil {
    public static final String CHAR_SET = "UTF-8";

    public static String genKey(int size) {
        byte[] bytes = SecureProvider.symmetricKeySupplier.get(size);
        return CodecUtil.ofUtf8(bytes);
    }

    public static String encrypt(byte[] data, String key) {
        try {
            SymmetricEncryptor encryptor = SecureProvider.symmetricEncryptorSupplier.get(key.getBytes());
            byte[] sdata = encryptor.encrypt(data);
            return Base64Util.encode(sdata);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String data, String key) {
        try {
            byte[] sdata = Base64Util.decode(data);
            SymmetricEncryptor encryptor = SecureProvider.symmetricEncryptorSupplier.get(key.getBytes());
            return encryptor.decrypt(sdata);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String data, byte[] key) {
        try {
            byte[] sdata = Base64Util.decode(data);
            SymmetricEncryptor encryptor = SecureProvider.symmetricEncryptorSupplier.get(key);
            return encryptor.decrypt(sdata);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptJsonBytesAfterBase64(byte[] data, String key) {
        try {
            String json = Base64Util.encode(data);
            return encrypt(json.getBytes(CHAR_SET), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptJsonAfterBase64(String json, String key) {
        try {
            json = Base64Util.encode(json.getBytes(CHAR_SET));
            return encrypt(json.getBytes(CHAR_SET), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptJsonBeforeBase64(String data, String key) {
        try {
            byte[] sdata = decrypt(data, key);
            String json = new String(sdata, CHAR_SET);
            json = new String(Base64Util.decode(json), CHAR_SET);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
