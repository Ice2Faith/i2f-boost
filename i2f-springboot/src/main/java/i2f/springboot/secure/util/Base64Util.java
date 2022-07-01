package i2f.springboot.secure.util;

import java.util.Base64;

/**
 * @author ltb
 * @date 2022/6/28 15:26
 * @desc
 */
public class Base64Util {
    public static String to(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }
    public static byte[] from(String bs4){
        return Base64.getDecoder().decode(bs4);
    }

    public static String toUrl(byte[] data){
        return Base64.getUrlEncoder().encodeToString(data);
    }
    public static byte[] fromUrl(String bs4){
        return Base64.getUrlDecoder().decode(bs4);
    }
}

