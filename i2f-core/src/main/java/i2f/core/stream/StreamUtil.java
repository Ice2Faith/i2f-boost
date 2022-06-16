package i2f.core.stream;

import i2f.core.annotations.remark.Author;
import i2f.core.interfaces.IMap;

import java.io.*;

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
        long rdSize = 0;
        while (rdSize != size) {
            int bt = is.read();
            if (bt >= 0) {
                os.write(bt);
                rdSize++;
            } else {
                break;
            }
        }

        os.flush();

        if (closeOs) {
            os.close();
        }

        if (closeIs) {
            is.close();
        }

        return rdSize;
    }

    public static void convertByteStream(InputStream is, OutputStream os, IMap<Byte, Byte> mapper) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        int bt = -1;
        while ((bt = bis.read()) >= 0) {
            byte wbt = (byte) (bt & 0x0ff);
            if (mapper != null) {
                wbt = mapper.map(wbt);
            }
            bos.write(wbt);
        }
        bos.flush();
    }

    public static byte[] readBytes(InputStream is,boolean closeIs) throws IOException {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        streamCopy(is,bos,true,closeIs);
        return bos.toByteArray();
    }
    public static String readString(Reader reader,boolean closeIs) throws IOException {
        StringBuilder builder=new StringBuilder();
        int ch=0;
        while((ch=reader.read())>=0){
            builder.append((char)ch);
        }
        if(closeIs){
            reader.close();
        }
        return builder.toString();
    }
    public static String readString(InputStream is,String charset,boolean closeIs) throws IOException {
        InputStreamReader reader=new InputStreamReader(is,charset);
        return readString(reader,closeIs);
    }

    public static void writeBytes(byte[] data,OutputStream os,boolean closeOs) throws IOException {
        ByteArrayInputStream bis=new ByteArrayInputStream(data);
        streamCopy(bis,os,closeOs,true);
    }

    public static void writeString(String str,OutputStream os,String charset,boolean closeOs) throws IOException {
        byte[] data=str.getBytes(charset);
        writeBytes(data,os,closeOs);
    }

    public static void writeString(String str,Writer os,boolean closeOs) throws IOException {
        os.write(str);
        if(closeOs){
            os.close();
        }
    }
}
