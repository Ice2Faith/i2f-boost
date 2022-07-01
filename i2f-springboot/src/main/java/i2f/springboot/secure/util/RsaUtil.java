package i2f.springboot.secure.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;

/**
 * @author ltb
 * @date 2022/6/28 15:08
 * @desc
 */
public class RsaUtil {
    private static final BouncyCastleProvider provider=new BouncyCastleProvider();
    public static void registryProvider(){
        Security.addProvider(provider);
    }
    public static RsaKey makeKeyPair(){
        return makeKeyPair(1024);
    }

    /**
     * 创建密钥对
     * @param size
     * @return
     */
    public static RsaKey makeKeyPair(int size){
        try{
            registryProvider();
            SecureRandom rand=new SecureRandom();
            KeyPairGenerator generator=KeyPairGenerator.getInstance("RSA","BC");
            generator.initialize(size,rand);
            KeyPair keyPair= generator.generateKeyPair();
            return new RsaKey(keyPair);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 获取RSA
     * @return
     */
    public static Cipher rsaCipher(){
        try{
            Security.addProvider(provider);
            Cipher cipher=Cipher.getInstance("RSA/None/PKCS1Padding","BC");
            return cipher;
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 私钥解密
     * @param key
     * @param data
     * @return
     */
    public static byte[] privateKeyDecrypt(RsaKey key,byte[] data){
        try{
            Cipher cipher=rsaCipher();
            cipher.init(Cipher.DECRYPT_MODE,key.privateKey());
            return cipher.doFinal(data);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 私钥加密
     * @param key
     * @param data
     * @return
     */
    public static byte[] privateKeyEncrypt(RsaKey key,byte[] data){
        try{
            Cipher cipher=rsaCipher();
            cipher.init(Cipher.ENCRYPT_MODE,key.privateKey());
            return cipher.doFinal(data);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 公钥解密
     * @param key
     * @param data
     * @return
     */
    public static byte[] publicKeyDecrypt(RsaKey key,byte[] data){
        try{
            Cipher cipher=rsaCipher();
            cipher.init(Cipher.DECRYPT_MODE,key.publicKey());
            return cipher.doFinal(data);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 公钥加密
     * @param key
     * @param data
     * @return
     */
    public static byte[] publicKeyEncrypt(RsaKey key,byte[] data){
        try{
            Cipher cipher=rsaCipher();
            cipher.init(Cipher.ENCRYPT_MODE,key.publicKey());
            return cipher.doFinal(data);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 转为UTF-8字符串
     * @param data
     * @return
     */
    public static String utf8String(byte[] data){
        try{
            return new String(data,"UTF-8");
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 转为UTF-8字节
     * @param text
     * @return
     */
    public static byte[] utf8Bytes(String text){
        try{
            return text.getBytes("UTF-8");
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 私钥，对输入base64解密为string字符串
     * 适用于使用JSON序列化的数据加密后使用base64传输
     * @param key
     * @param bs64
     * @return
     */
    public static String privateKeyDecryptBase64(RsaKey key,String bs64){
        byte[] enc=Base64Util.from(bs64);
        byte[] dec=privateKeyDecrypt(key,enc);
        return utf8String(dec);
    }

    /**
     * 私钥，对输入的字符串加密为base64字符串
     * 适用于已经使用JSON序列化的数据进行加密后使用base64传输
     * @param key
     * @param text
     * @return
     */
    public static String privateKeyEncryptBase64(RsaKey key,String text){
       byte[] data=utf8Bytes(text);
       byte[] enc=privateKeyEncrypt(key,data);
       return Base64Util.to(enc);
    }

    /**
     * 公钥，对输入base64解密为string字符串
     * 适用于使用JSON序列化的数据加密后使用base64传输
     * @param key
     * @param bs64
     * @return
     */
    public static String publicKeyDecryptBase64(RsaKey key,String bs64){
        byte[] enc=Base64Util.from(bs64);
        byte[] dec=publicKeyDecrypt(key,enc);
        return utf8String(dec);
    }

    /**
     * 公钥，对输入的字符串加密为base64字符串
     * 适用于已经使用JSON序列化的数据进行加密后使用base64传输
     * @param key
     * @param text
     * @return
     */
    public static String publicKeyEncryptBase64(RsaKey key,String text){
        byte[] data=utf8Bytes(text);
        byte[] enc=publicKeyEncrypt(key,data);
        return Base64Util.to(enc);
    }

}
