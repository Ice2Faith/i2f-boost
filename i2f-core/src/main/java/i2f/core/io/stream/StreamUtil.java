package i2f.core.io.stream;

import i2f.core.annotations.remark.Author;
import i2f.core.io.file.FileUtil;
import i2f.core.io.stream.impl.TempFileInputStream;
import i2f.core.lang.functional.common.IMapper;

import java.io.*;
import java.net.URL;

/**
 * @author ltb
 * @date 2022/3/19 15:25
 * @desc
 */
@Author("i2f")
public class StreamUtil {
    public static void broadcastStream(InputStream is, boolean closeIs, boolean closeOs, OutputStream... oss) throws IOException {
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            for (OutputStream item : oss) {
                item.write(buf, 0, len);
            }
        }
        if (closeIs) {
            is.close();
        }
        for (OutputStream item : oss) {
            item.flush();
            if (closeOs) {
                item.close();
            }
        }
    }

    public static OutputStream streamCopy(OutputStream os, boolean autoClose, InputStream... iss) throws IOException {
        return streamCopy(os, autoClose, autoClose, iss);
    }

    public static OutputStream streamCopy(OutputStream os, boolean closeOs, boolean closeIs, InputStream... iss) throws IOException {
        for (InputStream item : iss) {
            streamCopy(item, os, false);
            if (closeIs) {
                item.close();
            }
        }
        if (closeOs) {
            os.close();
        }
        return os;
    }

    public static void streamCopy(InputStream is, OutputStream os) throws IOException {
        streamCopy(is, os, true, true);
    }

    public static OutputStream streamCopy(InputStream is, OutputStream os, boolean autoClose) throws IOException {
        return streamCopy(is, os, autoClose, autoClose);
    }

    public static OutputStream streamCopy(InputStream is, OutputStream os, boolean closeOs, boolean closeIs) throws IOException {
        InputStream bis = (is instanceof BufferedInputStream) ? is : new BufferedInputStream(is);
        OutputStream bos = (os instanceof BufferedOutputStream) ? os : new BufferedOutputStream(os);

        int len = 0;
        byte[] buf = new byte[8192];
        while ((len = bis.read(buf)) > 0) {
            bos.write(buf, 0, len);
        }

        bos.flush();

        if (closeOs) {
            bos.close();
        }
        if (closeIs) {
            bis.close();
        }

        return bos;
    }

    public static long streamCopySize(InputStream is, OutputStream os, long size, boolean autoCloseOs) throws IOException {
        return streamCopyRange(is, os, 0, size, autoCloseOs);
    }

    public static long streamCopyRange(InputStream is, OutputStream os, long offset, long size, boolean autoCloseOs) throws IOException {
        return streamCopyRange(is, os, offset, size, autoCloseOs, false);
    }

    public static long streamCopyRange(InputStream is, OutputStream os, long offset, long size, boolean closeOs, boolean closeIs) throws IOException {
        if (offset > 0) {
            is.skip(offset);
        }
        int batchCount = 1024 * 16;
        byte[] buf = new byte[batchCount];
        long count = 0;
        int len = 0;
        while ((count + batchCount) <= size) {
            len = is.read(buf);
            if (len <= 0) {
                break;
            }
            os.write(buf, 0, len);
            count += len;
        }
        int diffCount = (int) (size - count);
        if (diffCount > 0) {
            byte[] data = new byte[diffCount];
            len = is.read(data);
            if (len > 0) {
                os.write(data, 0, len);
                count += len;
            }
        }

        os.flush();

        if (closeOs) {
            os.close();
        }

        if (closeIs) {
            is.close();
        }

        return count;
    }

    public static void convertByteStream(InputStream is, OutputStream os, IMapper<Byte, Byte> mapper) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        int bt = -1;
        while ((bt = bis.read()) >= 0) {
            byte wbt = (byte) (bt & 0x0ff);
            if (mapper != null) {
                wbt = mapper.get(wbt);
            }
            bos.write(wbt);
        }
        bos.flush();
    }

    public static byte[] readBytes(InputStream is, int size, boolean closeIs) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        streamCopyRange(is, bos, 0, size, true, closeIs);
        return bos.toByteArray();
    }

    public static byte[] readBytes(InputStream is, int offset, int size, boolean closeIs) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        streamCopyRange(is, bos, offset, size, true, closeIs);
        return bos.toByteArray();
    }

    public static byte[] readBytes(InputStream is, boolean closeIs) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        streamCopy(is, bos, true, closeIs);
        return bos.toByteArray();
    }

    public static String readString(Reader reader, boolean closeIs) throws IOException {
        StringBuilder builder = new StringBuilder();
        int ch = 0;
        while ((ch = reader.read()) >= 0) {
            builder.append((char) ch);
        }
        if (closeIs) {
            reader.close();
        }
        return builder.toString();
    }

    public static String readString(InputStream is, String charset, boolean closeIs) throws IOException {
        InputStreamReader reader = new InputStreamReader(is, charset);
        return readString(reader, closeIs);
    }

    public static void writeBytes(byte[] data, OutputStream os, boolean closeOs) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        streamCopy(bis, os, closeOs, true);
    }

    public static void writeString(String str, OutputStream os, String charset, boolean closeOs) throws IOException {
        byte[] data = str.getBytes(charset);
        writeBytes(data, os, closeOs);
    }

    public static void writeString(String str, Writer os, boolean closeOs) throws IOException {
        os.write(str);
        if (closeOs) {
            os.close();
        }
    }

    public static byte[] readBytes(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        return readBytes(is, true);
    }

    public static byte[] readBytes(File file, int size) throws IOException {
        InputStream is = new FileInputStream(file);
        return readBytes(is, size, true);
    }

    public static byte[] readBytes(File file, int offset, int size) throws IOException {
        InputStream is = new FileInputStream(file);
        return readBytes(is, offset, size, true);
    }

    public static byte[] readBytes(URL url) throws IOException {
        InputStream is = url.openStream();
        return readBytes(is, true);
    }

    public static byte[] readBytes(URL url, int size) throws IOException {
        InputStream is = url.openStream();
        return readBytes(is, size, true);
    }

    public static byte[] readBytes(URL url, int offset, int size) throws IOException {
        InputStream is = url.openStream();
        return readBytes(is, offset, size, true);
    }

    public static String readString(File file, String charset) throws IOException {
        InputStream is = new FileInputStream(file);
        return readString(is, charset, true);
    }

    public static String readString(URL url, String charset) throws IOException {
        InputStream is = url.openStream();
        return readString(is, charset, true);
    }

    public static void writeBytes(byte[] data, File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        OutputStream os = new FileOutputStream(file);
        writeBytes(data, os, true);
    }

    public static void writeString(String str, String charset, File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        OutputStream os = new FileOutputStream(file);
        writeString(str, os, charset, true);
    }

    public static void writeBytes(InputStream is, File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        OutputStream os = new FileOutputStream(file);
        streamCopy(is, os, true, true);
    }

    public static void writeBytes(URL url, File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(file);
        streamCopy(is, os, true, true);
    }

    public static InputStream localStream(InputStream is, boolean force) throws IOException {
        return localStream(is, -1, force);
    }

    public static InputStream localStream(InputStream is, int memLimit, boolean force) throws IOException {
        if (!force) {
            if (is instanceof ByteArrayInputStream) {
                return is;
            }
            if (is instanceof FileInputStream) {
                return is;
            }
        }
        return localStream(is, memLimit);
    }

    public static InputStream localStream(InputStream is) throws IOException {
        return localStream(is, -1);
    }

    public static InputStream localStream(InputStream is, int memLimit) throws IOException {
        int defaultMaxLimit = Integer.MAX_VALUE - 1024;
        if (memLimit < 0) {
            memLimit = defaultMaxLimit;
        }
        memLimit = Math.min(memLimit, defaultMaxLimit);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        long count = streamCopyRange(is, bos, 0, memLimit, false, false);

        if (count >= memLimit) {
            File file = FileUtil.getTempFile();
            OutputStream fos = new FileOutputStream(file);

            bos.flush();
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            streamCopy(bis, fos, false, true);

            streamCopy(is, fos, true, true);

            return new TempFileInputStream(file);
        } else {
            return new ByteArrayInputStream(bos.toByteArray());
        }
    }

}
