package i2f.liteflow.util;

import java.io.*;

/**
 * @author Ice2Faith
 * @date 2023/6/7 15:32
 * @desc
 */
public class Io {
    public static void streamCopy(InputStream is, OutputStream os,boolean closeIs,boolean closeOs) throws IOException {
        byte[] buf=new byte[1024*16];
        int len=0;
        while((len=is.read(buf))>0){
            os.write(buf,0,len);
        }
        os.flush();
        if(closeIs){
            is.close();
        }
        if(closeOs){
            os.close();
        }
    }

    public static void streamCopy(InputStream is,OutputStream os,boolean closeOs) throws IOException {
        streamCopy(is,os,true,closeOs);
    }

    public static void streamCopy(InputStream is,OutputStream os) throws IOException {
        streamCopy(is,os,true,true);
    }

    public static byte[] readBytes(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        streamCopy(is,bos,true,true);
        return bos.toByteArray();
    }

    public static void writeBytes(byte[] bytes,OutputStream os,boolean closeOs) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        streamCopy(bis,os,true,closeOs);
    }

    public static String readString(InputStream is,String charset) throws IOException {
        byte[] bytes = readBytes(is);
        return new String(bytes,charset);
    }

    public static void writeString(String str,String charset,OutputStream os,boolean closeOs) throws IOException {
        writeBytes(str.getBytes(charset),os,closeOs);
    }
}
