package i2f.jce.jdk.digest;

import i2f.jce.jdk.digest.checksum.ChecksumMessageDigester;
import i2f.jce.jdk.digest.hmac.HmacMessageDigester;
import i2f.jce.jdk.digest.md.MessageDigester;
import i2f.jce.std.util.ByteUtil;

import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2024/3/27 10:39
 * @desc
 */
public class MessageDigestUtil {

    public static String md2(byte[] data) throws Exception {
        return ByteUtil.toHex(MessageDigester.MD2.digest(data));
    }

    public static String md2(InputStream is) throws Exception {
        return ByteUtil.toHex(MessageDigester.MD2.digest(is));
    }

    public static String md5(byte[] data) throws Exception {
        return ByteUtil.toHex(MessageDigester.MD5.digest(data));
    }

    public static String md5(InputStream is) throws Exception {
        return ByteUtil.toHex(MessageDigester.MD5.digest(is));
    }

    public static String sha1(byte[] data) throws Exception {
        return ByteUtil.toHex(MessageDigester.SHA_1.digest(data));
    }

    public static String sha1(InputStream is) throws Exception {
        return ByteUtil.toHex(MessageDigester.SHA_1.digest(is));
    }

    public static String sha224(byte[] data) throws Exception {
        return ByteUtil.toHex(MessageDigester.SHA_224.digest(data));
    }

    public static String sha224(InputStream is) throws Exception {
        return ByteUtil.toHex(MessageDigester.SHA_224.digest(is));
    }

    public static String sha256(byte[] data) throws Exception {
        return ByteUtil.toHex(MessageDigester.SHA_256.digest(data));
    }

    public static String sha256(InputStream is) throws Exception {
        return ByteUtil.toHex(MessageDigester.SHA_256.digest(is));
    }

    public static String sha384(byte[] data) throws Exception {
        return ByteUtil.toHex(MessageDigester.SHA_384.digest(data));
    }

    public static String sha384(InputStream is) throws Exception {
        return ByteUtil.toHex(MessageDigester.SHA_384.digest(is));
    }

    public static String sha512(byte[] data) throws Exception {
        return ByteUtil.toHex(MessageDigester.SHA_512.digest(data));
    }

    public static String sha512(InputStream is) throws Exception {
        return ByteUtil.toHex(MessageDigester.SHA_512.digest(is));
    }


    public static String hmacMd2(byte[] key, byte[] data) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_MD2.apply(key, null).digest(data));
    }

    public static String hmacMd2(byte[] key, InputStream is) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_MD2.apply(key, null).digest(is));
    }

    public static String hmacMd5(byte[] key, byte[] data) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_MD5.apply(key, null).digest(data));
    }

    public static String hmacMd5(byte[] key, InputStream is) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_MD5.apply(key, null).digest(is));
    }

    public static String hmacSha1(byte[] key, byte[] data) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_SHA_1.apply(key, null).digest(data));
    }

    public static String hmacSha1(byte[] key, InputStream is) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_SHA_1.apply(key, null).digest(is));
    }

    public static String hmacSha224(byte[] key, byte[] data) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_SHA_224.apply(key, null).digest(data));
    }

    public static String hmacSha224(byte[] key, InputStream is) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_SHA_224.apply(key, null).digest(is));
    }

    public static String hmacSha256(byte[] key, byte[] data) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_SHA_256.apply(key, null).digest(data));
    }

    public static String hmacSha256(byte[] key, InputStream is) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_SHA_256.apply(key, null).digest(is));
    }

    public static String hmacSha384(byte[] key, byte[] data) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_SHA_384.apply(key, null).digest(data));
    }

    public static String hmacSha384(byte[] key, InputStream is) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_SHA_384.apply(key, null).digest(is));
    }

    public static String hmacSha512(byte[] key, byte[] data) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_SHA_512.apply(key, null).digest(data));
    }

    public static String hmacSha512(byte[] key, InputStream is) throws Exception {
        return ByteUtil.toHex(HmacMessageDigester.HMAC_SHA_512.apply(key, null).digest(is));
    }

    public static String adler32(byte[] data) throws Exception {
        return ByteUtil.toHex(ChecksumMessageDigester.ADLER32.digest(data));
    }

    public static String crc32(InputStream is) throws Exception {
        return ByteUtil.toHex(ChecksumMessageDigester.CRC32.digest(is));
    }

    public static String hashcode(InputStream is) throws Exception {
        return ByteUtil.toHex(ChecksumMessageDigester.HASHCODE.digest(is));
    }
}
