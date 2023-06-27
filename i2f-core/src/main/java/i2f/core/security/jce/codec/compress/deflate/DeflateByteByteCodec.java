package i2f.core.security.jce.codec.compress.deflate;

import i2f.core.io.stream.StreamUtil;
import i2f.core.security.jce.codec.compress.IByteByteCodec;
import i2f.core.security.jce.codec.exception.CodecException;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/27 9:41
 * @desc
 */
public class DeflateByteByteCodec implements IByteByteCodec {
    @Override
    public byte[] encode(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            deflateEncode(bis, bos, true, true);
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
            deflateDecode(bis, bos, true, true);
            return bos.toByteArray();
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
