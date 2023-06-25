package i2f.core.io.stream.encrypt;

import i2f.core.io.stream.StreamUtil;

import java.io.*;

/**
 * @author ltb
 * @date 2022/5/20 10:28
 * @desc
 */
public class EncryptStreamUtil {
    public static void streamCopyEncrypt(InputStream is, OutputStream os,IEncryptor enc) throws IOException {
        EncryptOutputStream eos=new EncryptOutputStream(os,enc);
        StreamUtil.streamCopy(is,eos,true,true);
    }
    public static byte[] bytesEncrypt(byte[] bts,IEncryptor enc) throws IOException {
        ByteArrayInputStream bis=new ByteArrayInputStream(bts);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        streamCopyEncrypt(bis,bos,enc);
        return bos.toByteArray();
    }
    public static byte[] stringEncrypt(String str,IEncryptor enc) throws IOException {
        byte[] data=bytesEncrypt(str.getBytes("UTF-8"),enc);
        return data;
    }
    public static String stringDecrypt(byte[] bts,IEncryptor enc) throws IOException {
        byte[] data=bytesEncrypt(bts,enc);
        return new String(data,"UTF-8");
    }
}
