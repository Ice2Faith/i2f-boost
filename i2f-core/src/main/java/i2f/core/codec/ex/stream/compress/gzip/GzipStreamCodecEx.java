package i2f.core.codec.ex.stream.compress.gzip;

import i2f.core.codec.ex.stream.IStreamCodecEx;
import i2f.core.codec.exception.CodecException;
import i2f.core.io.stream.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/27 16:06
 * @desc
 */
public class GzipStreamCodecEx implements IStreamCodecEx {
    public static GzipStreamCodecEx INSTANCE = new GzipStreamCodecEx();

    @Override
    public void encode(InputStream data, OutputStream enc) {
        try {
            gzipEncode(data, enc, true, true);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    public void decode(OutputStream enc, InputStream data) {
        try {
            gzipDecode(data, enc, true, true);
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
