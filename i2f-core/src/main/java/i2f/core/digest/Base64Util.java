package i2f.core.digest;

import i2f.core.annotations.remark.Author;
import i2f.core.security.jce.codec.CodecUtil;

@Author("i2f")
public class Base64Util {
    public static String encode(byte[] data){
        return CodecUtil.toBase64(data);
    }
    public static byte[] decode(String base64){
        return CodecUtil.ofBase64(base64);
    }

    public static byte[] decodeUrl(String base64){
        return CodecUtil.ofBase64Url(base64);
    }
    public static String encodeUrl(byte[] data){
        return CodecUtil.toBase64Url(data);
    }
}
