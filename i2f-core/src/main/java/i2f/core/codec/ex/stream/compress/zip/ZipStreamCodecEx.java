package i2f.core.codec.ex.stream.compress.zip;

import i2f.core.codec.ex.stream.IStreamCodecEx;
import i2f.core.codec.exception.CodecException;
import i2f.core.io.stream.StreamUtil;
import i2f.core.io.stream.impl.BlackHoleOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/27 16:15
 * @desc
 */
public class ZipStreamCodecEx implements IStreamCodecEx {
    public static ZipStreamCodecEx INSTANCE = new ZipStreamCodecEx();

    @Override
    public void encode(InputStream data, OutputStream enc) {
        try {
            zipEncode(data, enc, true, true);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    public void decode(OutputStream enc, InputStream data) {
        try {
            zipDecode(data, enc, true, true);
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
