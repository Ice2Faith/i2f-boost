package i2f.core.security.jce.codec.compress.gzip;

import i2f.core.io.stream.StreamUtil;
import i2f.core.security.jce.codec.compress.IByteByteCodec;
import i2f.core.security.jce.codec.exception.CodecException;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/27 9:41
 * @desc
 */
public class GzipByteByteCodec implements IByteByteCodec {
    @Override
    public byte[] encode(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            gzipEncode(bis, bos, true, true);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] decode(byte[] enc) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(enc);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            gzipDecode(bis, bos, true, true);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    public static void gzipEncode(InputStream is, OutputStream os, boolean closeOs, boolean closeIs) throws IOException {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        StreamUtil.streamCopy(is, gos, closeOs, closeIs);
    }

    public static void gzipDecode(InputStream is, OutputStream os, boolean closeOs, boolean closeIs) throws IOException {
        GZIPInputStream gis = new GZIPInputStream(is);
        StreamUtil.streamCopy(gis, os, closeOs, closeIs);
    }
}
