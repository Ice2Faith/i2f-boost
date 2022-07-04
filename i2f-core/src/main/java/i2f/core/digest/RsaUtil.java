package i2f.core.digest;

import i2f.core.annotations.remark.Author;
import i2f.core.str.StringUtil;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;

//私钥加密，公钥解密，网络传输中，传输公钥和密文
@Author("i2f")
public class RsaUtil {
    public static int UPDATE_SIZE=116;//require lower than 117
    public static int DEFAULT_KEY_SIZE=1024;
    public static String CHAR_SET_NAME="UTF-8";

    public static RsaKey genRsaKeyData() throws NoSuchAlgorithmException{
        return genRsaKeyData(DEFAULT_KEY_SIZE);
    }
    public static RsaKey genRsaKeyData(int size) throws NoSuchAlgorithmException {
        return getRsaKeyData(genRsaKeyPair(size));
    }
    public static KeyPair genRsaKeyPair() throws NoSuchAlgorithmException {
        return genRsaKeyPair(DEFAULT_KEY_SIZE);
    }
    public static KeyPair genRsaKeyPair(int size) throws NoSuchAlgorithmException {
        KeyPairGenerator generator=KeyPairGenerator.getInstance("RSA");
        generator.initialize(size,new SecureRandom());
        return generator.generateKeyPair();
    }
    public static RsaKey getRsaKeyData(KeyPair keyPair){
        return new RsaKey(keyPair);
    }

    /**
     * 获取RSA
     * @return
     */
    public static Cipher rsaCipher(){
        try{
            Cipher cipher=Cipher.getInstance("RSA");
            return cipher;
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public static byte[] doCipherWorker(Cipher cipher,byte[] data){
        byte[] result=new byte[data.length];
        int plen=0;
        while(plen<data.length){
            int cplen=UPDATE_SIZE;
            if(data.length-plen<UPDATE_SIZE){
                cplen=data.length-plen;
            }
            byte[] part=new byte[cplen];
            System.arraycopy(data,plen,part,0,cplen);
            byte[] enPart=cipher.update(part);
            System.arraycopy(part,0,result,plen,cplen);
            plen+=UPDATE_SIZE;
        }
        return result;
    }

    public static byte[] rsaEncrypt(byte[] data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher=rsaCipher();
        cipher.init(Cipher.ENCRYPT_MODE,privateKey);
        return doCipherWorker(cipher,data);
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
            return doCipherWorker(cipher,data);
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
            return doCipherWorker(cipher,data);
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
            return doCipherWorker(cipher,data);
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
            return doCipherWorker(cipher,data);
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
        byte[] enc= Base64Util.decode(bs64);
        byte[] dec=privateKeyDecrypt(key,enc);
        return StringUtil.ofUtf8(dec);
    }

    /**
     * 私钥，对输入的字符串加密为base64字符串
     * 适用于已经使用JSON序列化的数据进行加密后使用base64传输
     * @param key
     * @param text
     * @return
     */
    public static String privateKeyEncryptBase64(RsaKey key,String text){
        byte[] data=StringUtil.toUtf8(text);
        byte[] enc=privateKeyEncrypt(key,data);
        return Base64Util.encode(enc);
    }

    /**
     * 公钥，对输入base64解密为string字符串
     * 适用于使用JSON序列化的数据加密后使用base64传输
     * @param key
     * @param bs64
     * @return
     */
    public static String publicKeyDecryptBase64(RsaKey key,String bs64){
        byte[] enc=Base64Util.decode(bs64);
        byte[] dec=publicKeyDecrypt(key,enc);
        return StringUtil.ofUtf8(dec);
    }

    /**
     * 公钥，对输入的字符串加密为base64字符串
     * 适用于已经使用JSON序列化的数据进行加密后使用base64传输
     * @param key
     * @param text
     * @return
     */
    public static String publicKeyEncryptBase64(RsaKey key,String text){
        byte[] data=StringUtil.toUtf8(text);
        byte[] enc=publicKeyEncrypt(key,data);
        return Base64Util.encode(enc);
    }

}
