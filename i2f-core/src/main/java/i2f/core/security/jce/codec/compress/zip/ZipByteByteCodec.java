package i2f.core.security.jce.codec.compress.zip;

import i2f.core.io.stream.StreamUtil;
import i2f.core.io.stream.impl.BlackHoleOutputStream;
import i2f.core.security.jce.codec.compress.IByteByteCodec;
import i2f.core.security.jce.codec.exception.CodecException;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/27 9:41
 * @desc
 */
public class ZipByteByteCodec implements IByteByteCodec {
    @Override
    public byte[] encode(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            zipEncode(bis, bos, true, true);
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
            zipDecode(bis, bos, true, true);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    public static void zipEncode(InputStream is, OutputStream os, boolean closeOs, boolean closeIs) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        zos.putNextEntry(new ZipEntry("data"));
        StreamUtil.streamCopy(is, zos, false, false);
        zos.closeEntry();
        if (closeOs) {
            zos.close();
        }
        if (closeIs) {
            is.close();
        }
    }

    public static void zipDecode(InputStream is, OutputStream os, boolean closeOs, boolean closeIs) throws IOException {
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry = null;
        BlackHoleOutputStream bhos = new BlackHoleOutputStream();
        while ((entry = zis.getNextEntry()) != null) {
            if ("data".equals(entry.getName())) {
                StreamUtil.streamCopy(zis, os, false, false);
            } else {
                StreamUtil.streamCopy(zis, bhos, false, false);
            }
            zis.closeEntry();
        }
        if (closeOs) {
            os.close();
        }
        if (closeIs) {
            zis.close();
        }
    }
}
