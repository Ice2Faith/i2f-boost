package i2f.core.security.jce.digest;

import i2f.core.security.jce.digest.hmac.HmacMessageDigester;
import i2f.core.security.jce.digest.md.MdMessageDigester;
import i2f.core.security.jce.digest.md.MdType;
import i2f.core.security.jce.digest.sha.ShaMessageDigester;
import i2f.core.security.jce.digest.sha.ShaType;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;

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
        return mds(data, type, null);
    }

    public static byte[] mds(byte[] data, IMessageDigestType type, String providerName) throws Exception {
        return mds(new ByteArrayInputStream(data), type, providerName);
    }

    public static byte[] mds(InputStream is, IMessageDigestType type) throws Exception {
        return mds(is, type, null);
    }

    public static byte[] mds(InputStream is, IMessageDigestType type, String providerName) throws Exception {
        MessageDigest md = messageDigestOf(type, providerName);
        return getMds(is, md);
    }

    public static MessageDigest messageDigestOf(IMessageDigestType type, String providerName) throws Exception {
        return messageDigestOf(type.type(), providerName);
    }

    public static MessageDigest messageDigestOf(String type, String providerName) throws Exception {
        if ("".equals(providerName)) {
            providerName = null;
        }
        if (providerName != null) {
            Provider provider = Security.getProvider(providerName);
            if (provider == null) {
                providerName = null;
            }
        }
        MessageDigest md = null;
        if (providerName != null) {
            md = MessageDigest.getInstance(type, providerName);
        } else {
            md = MessageDigest.getInstance(type);
        }
        return md;
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

    public static Mac hmacInstance(IMessageDigestType type, byte[] key) throws Exception {
        return hmacInstance(type, key, null);
    }

    public static Mac hmacInstance(IMessageDigestType type, byte[] key, String providerName) throws Exception {
        String hmacName = "Hmac" + type.type().replaceAll("-", "");
        SecretKey skey = new SecretKeySpec(key, hmacName);
        Mac mac = macOf(hmacName, providerName);
        mac.init(skey);
        return mac;
    }

    public static Mac macOf(String type, String providerName) throws Exception {
        if ("".equals(providerName)) {
            providerName = null;
        }
        if (providerName != null) {
            Provider provider = Security.getProvider(providerName);
            if (provider == null) {
                providerName = null;
            }
        }
        Mac mac = null;
        if (providerName != null) {
            mac = Mac.getInstance(type, providerName);
        } else {
            mac = Mac.getInstance(type);
        }
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
