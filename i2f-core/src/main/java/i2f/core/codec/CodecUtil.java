package i2f.core.codec;


import i2f.core.annotations.remark.Author;
import i2f.core.codec.bytes.base64.Base64MimeStringByteCodec;
import i2f.core.codec.bytes.base64.Base64StringByteCodec;
import i2f.core.codec.bytes.base64.Base64UrlStringByteCodec;
import i2f.core.codec.bytes.charset.CharsetStringByteCodec;
import i2f.core.codec.bytes.raw.BinStringByteCodec;
import i2f.core.codec.bytes.raw.DecStringByteCodec;
import i2f.core.codec.bytes.raw.HexStringByteCodec;
import i2f.core.codec.bytes.raw.OtcStringByteCodec;
import i2f.core.codec.collection.id.IdPackCodec;
import i2f.core.codec.compress.deflate.DeflateByteByteCodec;
import i2f.core.codec.compress.gzip.GzipByteByteCodec;
import i2f.core.codec.compress.zip.ZipByteByteCodec;
import i2f.core.codec.ex.stream.compress.deflate.DeflateStreamCodecEx;
import i2f.core.codec.ex.stream.compress.gzip.GzipStreamCodecEx;
import i2f.core.codec.ex.stream.compress.zip.ZipStreamCodecEx;
import i2f.core.codec.str.url.UrlStringStringCodec;
import i2f.core.serialize.bytes.jdk.JdkBytesObjectSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

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

    public static byte[] ofHexString(String data) {
        return HexStringByteCodec.INSTANCE.decode(data);
    }

    public static byte[] ofHexString(String data, String separator) {
        return new HexStringByteCodec(separator).decode(data);
    }


    public static String toOtcString(byte[] data) {
        return OtcStringByteCodec.INSTANCE.encode(data);
    }

    public static String toOtcString(byte[] data, String separator) {
        return new OtcStringByteCodec(separator).encode(data);
    }

    public static byte[] ofOtcString(String data) {
        return OtcStringByteCodec.INSTANCE.decode(data);
    }

    public static byte[] ofOtcString(String data, String separator) {
        return new OtcStringByteCodec(separator).decode(data);
    }


    public static String toBinString(byte[] data) {
        return BinStringByteCodec.INSTANCE.encode(data);
    }

    public static String toBinString(byte[] data, String separator) {
        return new BinStringByteCodec(separator).encode(data);
    }

    public static byte[] ofBinString(String data) {
        return BinStringByteCodec.INSTANCE.decode(data);
    }

    public static byte[] ofBinString(String data, String separator) {
        return new BinStringByteCodec(separator).decode(data);
    }


    public static String toDecString(byte[] data) {
        return DecStringByteCodec.INSTANCE.encode(data);
    }

    public static String toDecString(byte[] data, String separator) {
        return new DecStringByteCodec(separator).encode(data);
    }

    public static byte[] ofDecString(String data) {
        return DecStringByteCodec.INSTANCE.decode(data);
    }

    public static byte[] ofDecString(String data, String separator) {
        return new DecStringByteCodec(separator).decode(data);
    }

    public static String toBase64(byte[] data) {
        return Base64StringByteCodec.INSTANCE.encode(data);
    }

    public static byte[] ofBase64(String data) {
        return Base64StringByteCodec.INSTANCE.decode(data);
    }

    public static String toBase64Url(byte[] data) {
        return Base64UrlStringByteCodec.INSTANCE.encode(data);
    }

    public static byte[] ofBase64Url(String data) {
        return Base64UrlStringByteCodec.INSTANCE.decode(data);
    }

    public static String toBase64Mime(byte[] data) {
        return Base64MimeStringByteCodec.INSTANCE.encode(data);
    }

    public static byte[] ofBase64Mime(String data) {
        return Base64MimeStringByteCodec.INSTANCE.decode(data);
    }

    public static String toUrl(String data) {
        return UrlStringStringCodec.INSTANCE.encode(data);
    }

    public static String ofUrl(String data) {
        return UrlStringStringCodec.INSTANCE.decode(data);
    }

    public static String ofUtf8(byte[] data) {
        return CharsetStringByteCodec.UTF8.encode(data);
    }

    public static byte[] toUtf8(String data) {
        return CharsetStringByteCodec.UTF8.decode(data);
    }

    public static String ofGbk(byte[] data) {
        return CharsetStringByteCodec.GBK.encode(data);
    }

    public static byte[] toGbk(String data) {
        return CharsetStringByteCodec.GBK.decode(data);
    }

    public static String ofIso88591(byte[] data) {
        return CharsetStringByteCodec.ISO88591.encode(data);
    }

    public static byte[] toIso88591(String data) {
        return CharsetStringByteCodec.ISO88591.decode(data);
    }


    public static byte[] toGzip(byte[] data) {
        return GzipByteByteCodec.INSTANCE.encode(data);
    }

    public static byte[] ofGzip(byte[] data) {
        return GzipByteByteCodec.INSTANCE.decode(data);
    }

    public static byte[] toDeflate(byte[] data) {
        return DeflateByteByteCodec.INSTANCE.encode(data);
    }

    public static byte[] ofDeflate(byte[] data) {
        return DeflateByteByteCodec.INSTANCE.decode(data);
    }

    public static byte[] toZip(byte[] data) {
        return ZipByteByteCodec.INSTANCE.encode(data);
    }

    public static byte[] ofZip(byte[] data) {
        return ZipByteByteCodec.INSTANCE.decode(data);
    }

    public static void toGzip(InputStream is, OutputStream os) {
        GzipStreamCodecEx.INSTANCE.encode(is, os);
    }

    public static void ofGzip(InputStream is, OutputStream os) {
        GzipStreamCodecEx.INSTANCE.decode(os, is);
    }

    public static void toDeflate(InputStream is, OutputStream os) {
        DeflateStreamCodecEx.INSTANCE.encode(is, os);
    }

    public static void ofDeflate(InputStream is, OutputStream os) {
        DeflateStreamCodecEx.INSTANCE.decode(os, is);
    }

    public static void toZip(InputStream is, OutputStream os) {
        ZipStreamCodecEx.INSTANCE.encode(is, os);
    }

    public static void ofZip(InputStream is, OutputStream os) {
        ZipStreamCodecEx.INSTANCE.decode(os, is);
    }

    public static String packIds(Collection<?> ids) {
        return IdPackCodec.INSTANCE_OBJ.encode(new LinkedHashSet<>(ids));
    }

    public static String packIds(Object... ids) {
        return IdPackCodec.INSTANCE_OBJ.encode(new LinkedHashSet<>(Arrays.asList(ids)));
    }

    public static Set<String> unpackIds(String str) {
        return IdPackCodec.INSTANCE_STRING.decode(str);
    }

    public static Set<Long> unpackIdsAsLong(String str) {
        return IdPackCodec.INSTANCE_LONG.decode(str);
    }

    public static Set<Integer> unpackIdsAsInt(String str) {
        return IdPackCodec.INSTANCE_INT.decode(str);
    }

    public static byte[] jdkSerialize(Object obj) {
        return JdkBytesObjectSerializer.INSTANCE.encode(obj);
    }

    public static <T> T jdkDeserialize(byte[] data) {
        return (T) JdkBytesObjectSerializer.INSTANCE.decode(data);
    }
}
