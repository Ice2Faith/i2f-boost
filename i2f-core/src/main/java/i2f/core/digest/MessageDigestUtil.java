package i2f.core.digest;

import i2f.core.exception.BoostException;
import i2f.core.security.jce.codec.CodecUtil;

import java.security.MessageDigest;

/**
 * @author ltb
 * @date 2022/5/21 20:05
 * @desc
 */
public class MessageDigestUtil {

    public static final String MD5="MD5";
    public static final String SHA_1="SHA-1";
    public static final String SHA_256="SHA-256";
    public static final String SHA_384="SHA-384";
    public static final String SHA_512="SHA-512";

    public static byte[] getMds(byte[] data,MessageDigest md)  {
        int len=16;
        md.reset();
        int i=0;
        while((i+len)<data.length){
            md.update(data,i,len);
            i+=len;
        }
        if(i<data.length){
            md.update(data,i,data.length-i);
        }
        return md.digest();
    }

    public static String getMdsAsHex(byte[] data,MessageDigest md){
        byte[] dgs=getMds(data,md);
        return CodecUtil.toHexString(dgs);
    }

    public static MessageDigest getMessageDigestInstance(String name){
        try{
            return MessageDigest.getInstance(name);
        }catch(Exception e){
            throw new BoostException(e.getMessage(),e);
        }
    }

    public static String getMd5(byte[] data) {
        MessageDigest md = getMessageDigestInstance(MD5);
        return getMdsAsHex(data,md);
    }

    public static String getSha1(byte[] data) {
        MessageDigest md = getMessageDigestInstance(SHA_1);
        return getMdsAsHex(data,md);
    }

    public static String getSha256(byte[] data) {
        MessageDigest md = getMessageDigestInstance(SHA_256);
        return getMdsAsHex(data,md);
    }

    public static String getSha384(byte[] data) {
        MessageDigest md = getMessageDigestInstance(SHA_384);
        return getMdsAsHex(data,md);
    }

    public static String getSha512(byte[] data) {
        MessageDigest md = getMessageDigestInstance(SHA_512);
        return getMdsAsHex(data,md);
    }
}
