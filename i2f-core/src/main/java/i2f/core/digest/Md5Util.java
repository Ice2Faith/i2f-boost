package i2f.core.digest;

import i2f.core.annotations.remark.Author;
import i2f.core.codec.CodecUtil;
import i2f.core.security.jce.digest.MessageDigestUtil;

import java.io.*;

/**
 * @author ltb
 * @date 2022/3/19 15:30
 * @desc
 */
@Author("i2f")
public class Md5Util {
    /**
     * 计算字符串的MD5
     *
     * @param str
     * @return
     */
    public static String makeMD5(String str) {
        return MessageDigestUtil.MD5.mdsText(CodecUtil.toUtf8(str));
    }

    /**
     * 获取二进制数据的MD5值
     * 对于字符串请如下使用
     * String md5=makeMD5("aa".getBytes());
     *
     * @param data 字节数组，二进制值
     * @return MD5串
     */
    public static String makeMD5(byte[] data) {
        return MessageDigestUtil.MD5.mdsText(data);
    }


    public static String makeMD5(InputStream is) {
        return MessageDigestUtil.MD5.mdsText(is);
    }

    /**
     * 获取文件的MD5值
     * 如下使用
     * String md5=makeMD5(new File("C:\\aaa.txt"));
     *
     * @param file 被计算的文件
     * @return 文件的MD5值
     */
    public static String makeMD5(File file) {
        try {
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            String ret = MessageDigestUtil.MD5.mdsText(fis);
            fis.close();
            return ret;
        } catch (IOException e) {

        }
        return "";
    }
}
