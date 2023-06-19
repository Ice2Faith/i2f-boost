package i2f.core.jce.codec;


import i2f.core.annotations.remark.Author;
import i2f.core.jce.codec.bytes.base64.Base64MimeStringByteCodec;
import i2f.core.jce.codec.bytes.base64.Base64StringByteCodec;
import i2f.core.jce.codec.bytes.base64.Base64UrlStringByteCodec;
import i2f.core.jce.codec.bytes.raw.BinStringByteCodec;
import i2f.core.jce.codec.bytes.raw.DecStringByteCodec;
import i2f.core.jce.codec.bytes.raw.HexStringByteCodec;
import i2f.core.jce.codec.bytes.raw.OtcStringByteCodec;
import i2f.core.jce.codec.str.url.UrlStringStringCodec;

/**
 * @author ltb
 * @date 2022/3/19 15:26
 * @desc
 */
@Author("i2f")
public class CodecUtil {

    public static String toHexString(byte[] data) {
        return HexStringByteCodec.INSTANCE.encode(data);
    }

    public static String toHexString(byte[] data, String separator) {
        return new HexStringByteCodec(separator).encode(data);
    }

    public static byte[] parseHexString(String data) {
        return HexStringByteCodec.INSTANCE.decode(data);
    }

    public static byte[] parseHexString(String data, String separator) {
        return new HexStringByteCodec(separator).decode(data);
    }


    public static String toOtcString(byte[] data) {
        return OtcStringByteCodec.INSTANCE.encode(data);
    }

    public static String toOtcString(byte[] data, String separator) {
        return new OtcStringByteCodec(separator).encode(data);
    }

    public static byte[] parseOtcString(String data) {
        return OtcStringByteCodec.INSTANCE.decode(data);
    }

    public static byte[] parseOtcString(String data, String separator) {
        return new OtcStringByteCodec(separator).decode(data);
    }


    public static String toBinString(byte[] data) {
        return BinStringByteCodec.INSTANCE.encode(data);
    }

    public static String toBinString(byte[] data, String separator) {
        return new BinStringByteCodec(separator).encode(data);
    }

    public static byte[] parseBinString(String data) {
        return BinStringByteCodec.INSTANCE.decode(data);
    }

    public static byte[] parseBinString(String data, String separator) {
        return new BinStringByteCodec(separator).decode(data);
    }


    public static String toDecString(byte[] data) {
        return DecStringByteCodec.INSTANCE.encode(data);
    }

    public static String toDecString(byte[] data, String separator) {
        return new DecStringByteCodec(separator).encode(data);
    }

    public static byte[] parseDecString(String data) {
        return DecStringByteCodec.INSTANCE.decode(data);
    }

    public static byte[] parseDecString(String data, String separator) {
        return new DecStringByteCodec(separator).decode(data);
    }

    public static String toBase64(byte[] data) {
        return Base64StringByteCodec.INSTANCE.encode(data);
    }

    public static byte[] parseBase64(String data) {
        return Base64StringByteCodec.INSTANCE.decode(data);
    }

    public static String toBase64Url(byte[] data) {
        return Base64UrlStringByteCodec.INSTANCE.encode(data);
    }

    public static byte[] parseBase64Url(String data) {
        return Base64UrlStringByteCodec.INSTANCE.decode(data);
    }

    public static String toBase64Mime(byte[] data) {
        return Base64MimeStringByteCodec.INSTANCE.encode(data);
    }

    public static byte[] parseBase64Mime(String data) {
        return Base64MimeStringByteCodec.INSTANCE.decode(data);
    }

    public static String toUrl(String data) {
        return UrlStringStringCodec.INSTANCE.encode(data);
    }

    public static String parseUrl(String data) {
        return UrlStringStringCodec.INSTANCE.decode(data);
    }
}
