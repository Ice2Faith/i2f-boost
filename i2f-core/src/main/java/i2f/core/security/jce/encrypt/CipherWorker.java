package i2f.core.security.jce.encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ltb
 * @date 2022/6/8 9:03
 * @desc
 */
public class CipherWorker {
    public static byte[] work(byte[] data, Cipher cipher) throws BadPaddingException, IllegalBlockSizeException {
        return cipher.doFinal(data);
    }
    public static byte[] work(byte[] data,int batchSize,Cipher cipher) throws IOException, BadPaddingException, IllegalBlockSizeException {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        int i=0;
        while((i+batchSize)<data.length){
            byte[] edata=cipher.update(data,i,batchSize);
            bos.write(edata);
            i+=batchSize;
        }
        if(i<data.length){
            byte[] edata=cipher.doFinal(data,i,data.length-i);
            bos.write(edata);
        }
        byte[] enc=bos.toByteArray();
        return enc;
    }
    public static void work(InputStream is, OutputStream os,int batchSize,Cipher cipher) throws IOException, BadPaddingException, IllegalBlockSizeException {
        byte[] buf=new byte[batchSize];
        int len=0;
        while((len=is.read(buf))>0){
            byte[] edata= cipher.update(buf,0,len);
            os.write(edata);
        }
        byte[] edata=cipher.doFinal();
        os.write(edata);
        os.flush();
    }


    public static byte[] handleNoPaddingEncryptFormat(Cipher cipher, String content) throws Exception {
        return handleNoPaddingEncryptFormat(cipher, content.getBytes("UTF-8"));
    }

    /**
     * <p>NoPadding加密模式, 加密内容必须是 8byte的倍数, 不足8位则末位补足0</p>
     * <p>加密算法不提供该补码方式, 需要代码完成该补码方式</p>
     * @param cipher
     * @param data ：加密内容
     * @return 符合加密的内容(byte[])
     */
    public static byte[] handleNoPaddingEncryptFormat(Cipher cipher, byte[] data) throws Exception {
        int blockSize = cipher.getBlockSize();
        int plaintextLength = data.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - plaintextLength % blockSize);
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(data, 0, plaintext, 0, data.length);
        return plaintext;
    }
}
