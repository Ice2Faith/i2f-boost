package i2f.jce.sm.antherd;

import i2f.jce.sm.antherd.digest.Sm3Digester;
import i2f.jce.sm.antherd.encrypt.asymmetric.Sm2Encryptor;
import i2f.jce.sm.antherd.encrypt.symmetric.Sm4Encryptor;

/**
 * @author Ice2Faith
 * @date 2024/3/28 14:14
 * @desc
 */
public class SmUtil {
    public static String sm3(String str) throws Exception {
        return Sm3Digester.INSTANCE.digest(str);
    }

    public static String sm4Encrypt(String str,String key) throws Exception {
        return new Sm4Encryptor(key).encrypt(str);
    }

    public static String sm4Decrypt(String str,String key) throws Exception {
        return new Sm4Encryptor(key).decrypt(str);
    }

    public static String sm2Encrypt(String str,String publicKey) throws Exception {
        return new Sm2Encryptor(publicKey,null).encrypt(str);
    }

    public static String sm2Decrypt(String str,String privateKey) throws Exception {
        return new Sm2Encryptor(null,privateKey).decrypt(str);
    }

    public static String sm2Signature(String str,String privateKey) throws Exception {
        return new Sm2Encryptor(null,privateKey).sign(str);
    }

    public static boolean sm2Verify(String sign,String str,String publicKey) throws Exception {
        return new Sm2Encryptor(publicKey,null).verify(sign,str);
    }
}
