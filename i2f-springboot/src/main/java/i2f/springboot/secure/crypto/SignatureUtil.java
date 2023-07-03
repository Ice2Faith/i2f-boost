package i2f.springboot.secure.crypto;


import i2f.core.codec.CodecUtil;
import i2f.core.security.jce.digest.std.IMessageDigester;

/**
 * @author ltb
 * @date 2022/7/1 13:54
 * @desc
 */
public class SignatureUtil {
    public static String sign(String text) {
        IMessageDigester digester = SecureProvider.messageDigesterSupplier.get();
        return digester.mdsText(CodecUtil.toUtf8(CodecUtil.toBase64(CodecUtil.toUtf8(text))));
    }

}
