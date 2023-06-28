package i2f.core.codec.ex.stream.compress.deflate;

import i2f.core.codec.ex.stream.IStreamCodecEx;
import i2f.core.codec.exception.CodecException;
import i2f.core.io.stream.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/27 16:12
 * @desc
 */
public class DeflateStreamCodecEx implements IStreamCodecEx {
    public static DeflateStreamCodecEx INSTANCE = new DeflateStreamCodecEx();

    @Override
    public void encode(InputStream data, OutputStream enc) {
        try {
            deflateEncode(data, enc, true, true);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    public void decode(OutputStream enc, InputStream data) {
        try {
            deflateDecode(data, enc, true, true);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }


    public static void deflateEncode(InputStream is, OutputStream os, boolean closeOs, boolean closeIs) throws IOException {
        DeflaterOutputStream gos = new DeflaterOutputStream(os);
        StreamUtil.streamCopy(is, gos, closeOs, closeIs);
    }

    public static void deflateDecode(InputStream is, OutputStream os, boolean closeOs, boolean closeIs) throws IOException {
        InflaterInputStream gis = new InflaterInputStream(is);
        StreamUtil.streamCopy(gis, os, closeOs, closeIs);
    }
}
