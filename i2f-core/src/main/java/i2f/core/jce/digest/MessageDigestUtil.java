package i2f.core.jce.digest;

import i2f.core.jce.digest.hmac.HmacMessageDigester;
import i2f.core.jce.digest.md.MdMessageDigester;
import i2f.core.jce.digest.md.MdType;
import i2f.core.jce.digest.sha.ShaMessageDigester;
import i2f.core.jce.digest.sha.ShaType;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ltb
 * @date 2022/6/9 18:08
 * @desc
 */
public class MessageDigestUtil {
    public static MdMessageDigester MD2 = new MdMessageDigester(MdType.MD2);
    public static MdMessageDigester MD5 = new MdMessageDigester(MdType.MD5);

    public static ShaMessageDigester SHA1 = new ShaMessageDigester(ShaType.SHA1);
    public static ShaMessageDigester SHA224 = new ShaMessageDigester(ShaType.SHA224);
    public static ShaMessageDigester SHA256 = new ShaMessageDigester(ShaType.SHA256);
    public static ShaMessageDigester SHA384 = new ShaMessageDigester(ShaType.SHA384);
    public static ShaMessageDigester SHA512 = new ShaMessageDigester(ShaType.SHA512);

    public static MdMessageDigester md(MdType type) {
        return new MdMessageDigester(type);
    }

    public static ShaMessageDigester sha(ShaType type) {
        return new ShaMessageDigester(type);
    }

    public static HmacMessageDigester hmac(IMessageDigestType type, byte[] key) {
        return new HmacMessageDigester(type, key);
    }


    public static byte[] mds(byte[] data, IMessageDigestType type) throws Exception {
        return mds(new ByteArrayInputStream(data), type);
    }

    public static byte[] mds(InputStream is, IMessageDigestType type) throws Exception {
        MessageDigest md = MessageDigest.getInstance(type.type());
        return getMds(is, md);
    }

    public static byte[] getMds(InputStream is, MessageDigest md) throws IOException {
        byte[] buf = new byte[16];
        int len = 0;
        md.reset();
        while ((len = is.read(buf)) > 0) {
            md.update(buf, 0, len);
        }
        return md.digest();
    }

    public static Mac hmacInstance(IMessageDigestType type, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        String hmacName = "Hmac" + type.type().replaceAll("-", "");
        SecretKey skey = new SecretKeySpec(key, hmacName);
        Mac mac = Mac.getInstance(hmacName);
        mac.init(skey);
        return mac;
    }

    public static byte[] getHmacs(InputStream is, Mac mac) throws IOException {
        byte[] buf = new byte[16];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            mac.update(buf, 0, len);
        }
        return mac.doFinal();
    }
}
