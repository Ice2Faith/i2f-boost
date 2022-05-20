package i2f.core.stream.checksum;

import i2f.core.stream.StreamUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;

/**
 * @author ltb
 * @date 2022/5/20 9:28
 * @desc
 */
public class CheckedStreamUtil {
    public static String hexChecksum(Checksum sum){
        return formatChecksum(sum,"%x");
    }
    public static String formatChecksum(Checksum sum,String format){
        return String.format(format,sum.getValue());
    }
    public static Checksum streamCopyAdler32Checksum(InputStream is, OutputStream os) throws IOException {
        return streamCopyChecksum(is,os,new Adler32());
    }
    public static Checksum streamCopyChecksum(InputStream is, OutputStream os, Checksum type) throws IOException {
        CheckedOutputStream cos = new CheckedOutputStream(os, type);
        StreamUtil.streamCopy(is, os, true, true);
        return cos.getChecksum();
    }

    public static Checksum streamAdler32Checksum(InputStream is) throws IOException {
        return streamChecksum(is,new Adler32());
    }
    public static Checksum stringAdler32Checksum(String str) throws IOException {
        return stringChecksum(str,new Adler32());
    }
    public static Checksum stringChecksum(String str,Checksum type) throws IOException {
        return bytesChecksum(str.getBytes("UTF-8"),type);
    }
    public static Checksum bytesAdler32Checksum(byte[] bytes) throws IOException {
        return bytesChecksum(bytes,new Adler32());
    }
    public static Checksum bytesChecksum(byte[] bytes,Checksum type) throws IOException {
        ByteArrayInputStream bis=new ByteArrayInputStream(bytes);
        return streamChecksum(bis,type);
    }
    public static Checksum streamChecksum(InputStream is,Checksum type) throws IOException {
        CheckedInputStream cis=new CheckedInputStream(is,type);
        byte[] buf=new byte[4096];
        int len=0;
        while((len=cis.read(buf))>0){ }
        cis.close();
        return cis.getChecksum();
    }

}
