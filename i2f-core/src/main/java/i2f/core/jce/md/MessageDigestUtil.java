package i2f.core.jce.md;

import i2f.core.jce.md.hmac.HmacMessageDigestor;
import i2f.core.jce.md.md.MdMessageDigestor;
import i2f.core.jce.md.md.MdType;
import i2f.core.jce.md.sha.ShaMessageDigestor;
import i2f.core.jce.md.sha.ShaType;

/**
 * @author ltb
 * @date 2022/6/9 18:08
 * @desc
 */
public class MessageDigestUtil {
    public static MdMessageDigestor md(){
        return new MdMessageDigestor();
    }

    public static MdMessageDigestor md(MdType type) {
        return new MdMessageDigestor(type);
    }

    public static ShaMessageDigestor sha() {
        return new ShaMessageDigestor();
    }

    public static ShaMessageDigestor sha(ShaType type) {
        return new ShaMessageDigestor(type);
    }

    public static HmacMessageDigestor hmac(byte[] key) {
        return new HmacMessageDigestor(key);
    }

    public static HmacMessageDigestor hmac(IMdType type, byte[] key) {
        return new HmacMessageDigestor(type, key);
    }

}
