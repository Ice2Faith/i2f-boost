package i2f.core.digest;

import i2f.core.annotations.remark.Author;

import java.util.Base64;

@Author("i2f")
public class Base64Util {
    public static String encode(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }
    public static byte[] decode(String base64){
        return Base64.getDecoder().decode(base64);
    }
    public static byte[] decode(byte[] base64){
        return Base64.getDecoder().decode(base64);
    }

    public static byte[] decodeUrl(String base64){
        return Base64.getUrlDecoder().decode(base64);
    }
    public static String encodeUrl(byte[] data){
        return Base64.getUrlEncoder().encodeToString(data);
    }
}
